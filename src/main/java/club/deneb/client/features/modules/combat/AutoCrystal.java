package club.deneb.client.features.modules.combat;

import club.deneb.client.client.FriendManager;
import club.deneb.client.client.GuiManager;
import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.event.events.render.RenderEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.utils.*;
import club.deneb.client.utils.Timer;
import club.deneb.client.value.*;
import club.deneb.client.event.events.render.RenderModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by B_312 on 01/15/21
 */
@Module.Info(name = "AutoCrystal", category = Category.COMBAT,description = "Automatically place crystal to kill enemy")
public class AutoCrystal extends Module {

    PageValue Page_value = page("Page",1,"General","Calculation","Render");

    PageValue.Page general = newPage(Page_value,1);
    PageValue.Page calculation = newPage(Page_value,2);
    PageValue.Page render = newPage(Page_value,3);

    //General
    BooleanValue AutoSwitch_value = setting("AutoSwitch", false).page(general);
    BooleanValue TargetPlayer_value = setting("Players", true).page(general);
    BooleanValue TargetMobs_value = setting("Mobs", false).page(general);
    BooleanValue TargetAnimals_value = setting("Animals", false).page(general);
    BooleanValue Place_value = setting("Place", true).page(general);
    BooleanValue Explode_value = setting("Explode", true).page(general);
    BooleanValue MultiPlace_value = setting("MultiPlace", false).page(general);
    DoubleValue AttackSpeed_value = setting("AttackSpeed", 35d, 0, 50).page(general);
    DoubleValue PlaceSpeed_value = setting("PlaceSpeed", 35d, 0d, 50).page(general);
    DoubleValue Distance_value = setting("Distance", 7.0D, 0D, 8D).page(general);
    DoubleValue PlaceRange_value = setting("PlaceRange", 6.5D, 0D, 8D).page(general);
    DoubleValue HitRange_value = setting("HitRange", 5.5D, 0D, 8D).page(general);
    BooleanValue Rotate_value = setting("Rotate", true).page(general);
    BooleanValue RayTrace_value = setting("RayTrace", false).page(general);
    //Calculation
    BooleanValue NewPlace_value = setting("1.13Place", false).page(calculation);
    BooleanValue Wall_value = setting("Wall", true).page(calculation);
    DoubleValue WallRange_value = setting("WallRange", 3.5, 0d, 20d).page(calculation);
    BooleanValue NoSuicide_value = setting("NoSuicide", true).page(calculation);
    BooleanValue FacePlace_value = setting("FacePlace", true).page(calculation);
    DoubleValue BlastHealth_value = setting("MinHealthFace", 10d, 0d, 20d).page(calculation).b(FacePlace_value);
    DoubleValue MinDmg_value = setting("PlaceMinDamage", 4.5d, 0d, 20d).page(calculation);
    DoubleValue MaxSelf_value = setting("PlaceMaxSelf", 10d, 0d, 36d).page(calculation);
    ModeValue AttackMode_value = setting("HitMode",new ModeValue.Mode("Smart",true),new ModeValue.Mode("Always")).page(calculation);
    DoubleValue BMinDmg_value = setting("BreakMinDmg", 4.5, 0.0, 36.0).page(calculation).m(AttackMode_value,"Smart");
    DoubleValue BMaxSelf_value = setting("BreakMaxSelf", 12.0, 0.0, 36.0).page(calculation).m(AttackMode_value,"Smart");
    BooleanValue PopTotemTry = setting("PopTotemTry",true).page(calculation).m(AttackMode_value,"Smart");
    BooleanValue GhostHand_value = setting("GhostHand", false).page(calculation);
    BooleanValue PauseEating_value = setting("PauseWhileEating", false).page(calculation);
    BooleanValue ClientSideConfirm_value = setting("ClientSideConfirm",false).page(calculation);
    //Render
    BooleanValue RenderDmg_value = setting("RenderDamage", false).page(render);
    ModeValue RenderMode_value = setting("RenderBlock",new ModeValue.Mode("Solid", true),new ModeValue.Mode("Up"),new ModeValue.Mode("UpLine"),new ModeValue.Mode("Full"),new ModeValue.Mode("Outline"),new ModeValue.Mode("NoRender")).page(render);
    BooleanValue SyncGui_value = setting("SyncGui", false).page(render);
    IntValue Red_value = setting("Red", 255, 0, 255).page(render).r(SyncGui_value);
    IntValue Green_value = setting("Green", 0, 0, 255).page(render).r(SyncGui_value);
    IntValue Blue_value = setting("Blue", 0, 0, 255).page(render).r(SyncGui_value);
    IntValue Alpha_value = setting("Alpha", 70, 0, 255).page(render);
    BooleanValue Rainbow_value = setting("Rainbow", false).page(render).r(SyncGui_value);
    FloatValue RGBSpeed_value = setting("RGB Speed", 1.0f, 0.0f, 10.0f).page(render).r(SyncGui_value);
    FloatValue Saturation_value = setting("Saturation", 0.65f, 0.0f, 1.0f).page(render).r(SyncGui_value);
    FloatValue Brightness_value = setting("Brightness", 1.0f, 0.0f, 1.0f).page(render).r(SyncGui_value);

    public static double yaw;
    public static double pitch;
    public static double renderPitch;
    public static boolean isSpoofingAngles;
    public static Entity target;
    private int Placements = 0;
    private int oldSlot = -1;
    private int PrevSlot;
    private boolean switchCoolDown;
    private boolean togglePitch = false;
    private BlockPos renderBlock;
    private static boolean rotating = false;
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    private final HashMap<BlockPos, Double> renderBlockDmg = new HashMap<>();

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null) return;
        //Clear click delay
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            mc.rightClickDelayTimer = 0;
        }
        //Sounds confirm makes us update world entity faster
        if (event.packet instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.packet;
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderModelRotation(RenderModelEvent event){
        if(!Rotate_value.getValue()) return;
        //Update pitch
        if(rotating){
            event.rotating = true;
            event.pitch = (float) renderPitch;
        }
    }

    @SubscribeEvent
    public void updateRotation(RenderWorldLastEvent event){
        if(!Rotate_value.getValue()) return;
        //Update yaw
        if(rotating){
            mc.player.rotationYawHead = (float) yaw;
            mc.player.renderYawOffset = (float) yaw;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null) return;
        Packet<?> packet = event.packet;
        if (!Rotate_value.getValue()) {
            return;
        }
        //Spoof rotation packets
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
                isSpoofingAngles = false;
            }
        }
    }


    /**
     * Calculation of explosion
     * If return true,then we can explode it.
     */
    private boolean canHitCrystal(EntityEnderCrystal crystal) {
        if (mc.player.getDistance(crystal) > HitRange_value.getValue()) return false;
        if (AttackMode_value.getToggledMode().getName().equals("Smart")) {
            float selfDamage = calculateDamage(crystal.posX, crystal.posY, crystal.posZ, mc.player, mc.player.getPositionVector());
            if (selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
            List<EntityPlayer> entities = mc.world.playerEntities.stream()
                    .filter(e -> mc.player.getDistance(e) <= Distance_value.getValue())
                    .filter(e -> mc.player != e)
                    .filter(e -> !FriendManager.isFriend(e))
                    .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                    .collect(Collectors.toList());
            for (EntityPlayer player : entities) {
                if ( mc.player.isDead || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= 0.0f) continue;
                double minDamage = BMinDmg_value.getValue();
                double maxSelf =  BMaxSelf_value.getValue();
                if (canFacePlace(player, BlastHealth_value.getValue())) {
                    minDamage = 1;
                }
                double target = calculateDamage(crystal.posX, crystal.posY, crystal.posZ, player, player.getPositionVector());
                //If we can pop enemies totem,then we try it!
                if (target > player.getHealth() + player.getAbsorptionAmount()
                        && selfDamage < mc.player.getHealth() + mc.player.getAbsorptionAmount()
                        && PopTotemTry.getValue()
                ) return true;
                if (target < minDamage) continue;
                if (selfDamage > target) continue;
                if (selfDamage > maxSelf) continue;
                return true;
            }
        } else return true;
        return false;
    }

    private void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
            rotating = false;
        }
    }

    private void ExplodeCrystal(EntityEnderCrystal crystal) {
        if (Rotate_value.getValue()) lookAtCrystal(crystal);
        if (breakDelayRun(AttackSpeed_value.getValue())) {
            mc.playerController.attackEntity(mc.player, crystal);
            mc.player.swingArm(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if(ClientSideConfirm_value.getValue()){
                //This may cause deSync!
                crystal.setDead();
            }
            mc.player.resetCooldown();
            breakTimer.reset();
        }
    }

    private void PlaceCrystal(BlockPos targetBlock, EnumHand enumHand) {
        EnumFacing facing;
        if (RayTrace_value.getValue()) facing = enumFacing(targetBlock);
        else facing = EnumFacing.UP;
        if (Rotate_value.getValue()) lookAtPos(targetBlock, facing);
        for (int i = 0; i < 3; i++) {
            if (placeDelayRun(PlaceSpeed_value.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, facing, enumHand, 0, 0, 0));
                placeTimer.reset();
                Placements++;
            }
        }
    }

    private boolean placeDelayRun(double speed) {
        return placeTimer.passed(1000 / speed);
    }

    private boolean breakDelayRun(double speed) {
        return breakTimer.passed(1000 / speed);
    }

    private List<BlockPos> findCrystalBlocks(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions
                .addAll(BlockInteractionHelper.getSphere(getPlayerPos(), (float) range, (int) range, false, true, 0)
                        .stream()
                        .filter(v -> canPlaceCrystal(v, NewPlace_value.getValue())).collect(Collectors.toList()));
        return positions;
    }

    private void drawBlock(BlockPos blockPos, int color) {
        DenebTessellator.prepare(GL_QUADS);
        if (!RenderMode_value.getMode("NoRender").isToggled()) {
            if (RenderMode_value.getMode("Solid").isToggled() || RenderMode_value.getMode("Up").isToggled()) {
                if (RenderMode_value.getMode("Up").isToggled()) {
                    DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.UP);
                } else {
                    DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.ALL);
                }
            } else {
                if (RenderMode_value.getMode("Full").isToggled()) {
                    DenebTessellator.drawFullBox(blockPos, 1f, color);
                } else if (RenderMode_value.getMode("Outline").isToggled()) {
                    DenebTessellator.drawBoundingBox(blockPos, 2f, color);
                } else {
                    DenebTessellator.drawBoundingBox(blockPos.add(0, 1, 0), 2f, color);
                }
            }
        }
        DenebTessellator.release();
        if (RenderDmg_value.getValue()) {
            if (renderBlockDmg.containsKey(blockPos)) {
                GlStateManager.pushMatrix();
                glBillboardDistanceScaled((float) blockPos.getX() + 0.5f, (float) blockPos.getY() + 0.5f, (float) blockPos.getZ() + 0.5f, mc.player);
                final double damage = renderBlockDmg.get(blockPos);
                final String damageText = "DMG: " + (Math.floor(damage) == damage ? (int) damage : String.format("%.1f", damage));
                GlStateManager.disableDepth();
                GlStateManager.translate(-(fontRenderer.getStringWidth(damageText) / 2.0d), 0, 0);
                fontRenderer.drawStringWithShadow(damageText, 0, 0, -1);
                GlStateManager.popMatrix();
            }
        }
    }

    public void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (distance / 2.0f) / (2.0f + (2.0f - (float) 1));
        if (scaleDistance < 1f)
            scaleDistance = 1;
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public void glBillboard(float x, float y, float z) {
        float scale = 0.016666668f * 1.6f;
        GlStateManager.translate(x - Minecraft.getMinecraft().getRenderManager().renderPosX, y - Minecraft.getMinecraft().getRenderManager().renderPosY,
                z - Minecraft.getMinecraft().getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Minecraft.getMinecraft().player.rotationPitch, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public boolean canFacePlace(EntityLivingBase target, double blast) {
        float healthTarget = target.getHealth() + target.getAbsorptionAmount();
        return healthTarget <= blast;
    }

    public boolean canPlaceCrystal(BlockPos blockPos, boolean newPlace) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
            return false;

        if (!newPlace) {
            if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR)
                return false;
        } else if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) return false;

        return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.x - x;
        double yl = a.y - y;
        double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    public void lookAtPos(BlockPos block, EnumFacing face) {
        float[] v = RotationUtil.getRotationsBlock(block, face, false);
        float[] v2 = RotationUtil.getRotationsBlock(block.add(0,+0.5,0), face, false);
        setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void lookAtCrystal(EntityEnderCrystal ent) {
        float[] v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector());
        float[] v2 = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector().add(0,-0.5,0));
        setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void setYawAndPitch(float yaw1, float pitch1,float renderPitch1) {
        yaw = yaw1;
        pitch = pitch1;
        renderPitch = renderPitch1;
        isSpoofingAngles = true;
        rotating = true;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Vec3d vec) {
        float doubleExplosionSize = 12.0F;
        double distanceSize = getRange(vec, posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);

        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());

        double v = (1.0D - distanceSize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finalValue = 1.0;

        if (entity instanceof EntityLivingBase) {
            // we pass null as the exploder here
            //noinspection ConstantConditions
            finalValue = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finalValue;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        Vec3d offset = new Vec3d(entity.posX, entity.posY, entity.posZ);
        return calculateDamage(posX, posY, posZ, entity, offset);
    }


    private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage = damage * (1.0F - f / 25.0F);

            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage = damage - (damage / 5);
            }

            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static EnumFacing enumFacing(final BlockPos blockPos) {
        final EnumFacing[] values;
        final int length = (values = EnumFacing.values()).length;
        int i = 0;
        while (i < length) {
            final EnumFacing enumFacing = values[i];
            final Vec3d vec3d = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            final Vec3d vec3d2 = new Vec3d(blockPos.getX() + enumFacing.getDirectionVec().getX(), blockPos.getY() + enumFacing.getDirectionVec().getY(), blockPos.getZ() + enumFacing.getDirectionVec().getZ());
            final RayTraceResult rayTraceBlocks;
            if ((rayTraceBlocks = mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false)) != null
                    && rayTraceBlocks.typeOfHit.equals(RayTraceResult.Type.BLOCK) && rayTraceBlocks.getBlockPos().equals(blockPos)) {
                return enumFacing;
            }
            i++;
        }
        if (blockPos.getY() > mc.player.posY + mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }

    public static boolean IsEating() {
        return mc.player != null && (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood || mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) && mc.player.isHandActive();
    }

    public static boolean CanSeeBlock(BlockPos p_Pos) {
        if (mc.player == null)
            return true;

        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(p_Pos.getX(), p_Pos.getY(), p_Pos.getZ()), false, true, false) != null;
    }

    public static double getVecDistance(BlockPos a, double posX, double posY, double posZ) {
        double x1 = a.x - posX;
        double y1 = a.y - posY;
        double z1 = a.z - posZ;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

    @SubscribeEvent
    public void doAutoCrystal(TickEvent.RenderTickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (placeTimer.passed(1050) || breakTimer.passed(1050)) rotating = false;
        if (PauseEating_value.getValue() && IsEating()) return;
        EntityEnderCrystal c = mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal &&
                canHitCrystal((EntityEnderCrystal) e)).map(e -> (EntityEnderCrystal) e).min(Comparator.comparing(e -> mc.player.getDistance(e))).orElse(null);
        if (Explode_value.getValue() && c != null) {
            if (mc.player.canEntityBeSeen(c) || (mc.player.getDistance(c) < WallRange_value.getValue() && Wall_value.getValue()) || !Wall_value.getValue()) {
                ExplodeCrystal(c);
                if (!MultiPlace_value.getValue()) return;
                if (Placements >= 2) {
                    Placements = 0;
                    return;
                }
            }
        } else {
            resetRotation();
            if (oldSlot != -1) {
                mc.player.inventory.currentItem = oldSlot;
                oldSlot = -1;
            }
        }
        int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) offhand = true;
        else if (crystalSlot == -1) return;
        BlockPos targetBlock;
        CrystalTarget crystalTarget = Calculator();
        target = crystalTarget.target;
        targetBlock = crystalTarget.blockPos;
        if (target == null) {
            renderBlock = null;
            resetRotation();
            return;
        }
        renderBlock = targetBlock;
        if (ModuleManager.getModuleByName("AutoEz").isEnabled()) {
            AutoEz autoEz = (AutoEz) ModuleManager.getModuleByName("AutoEz");
            autoEz.addTargetedPlayer(target.getName());
        }
        if (Place_value.getValue()) {
            EnumHand enumHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (AutoSwitch_value.getValue()) {
                    mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    switchCoolDown = true;
                }
                return;
            }
            if (switchCoolDown) {
                switchCoolDown = false;
                return;
            }
            PlaceCrystal(targetBlock, enumHand);
        }
        if (Rotate_value.getValue() && isSpoofingAngles) {
            if (togglePitch) {
                mc.player.rotationPitch += 0.0004;
                togglePitch = false;
            } else {
                mc.player.rotationPitch -= 0.0004;
                togglePitch = true;
            }
        }
        if (PrevSlot != -1 && GhostHand_value.getValue()) {
            mc.player.inventory.currentItem = PrevSlot;
            mc.playerController.updateController();
        }
    }

    /**
     * Calculation of target block to place crystal
     * return a target contains coordinate and target entity.
     */
    private CrystalTarget Calculator() {
        double damage = 0.5;
        BlockPos tempBlock = null;
        Entity target = null;
        List<BlockPos> default_blocks;
        default_blocks = findCrystalBlocks(PlaceRange_value.getValue());
        List<Entity> entities = getEntities();
        PrevSlot = -1;
        for (Entity entity2 : entities) {

            //Ignore our self
            if (entity2 != mc.player) {

                //Ignore dead entity
                if (((EntityLivingBase) entity2).getHealth() <= 0.0f) continue;

                //Get the vector of target
                Vec3d targetVec = new Vec3d(entity2.posX, entity2.posY, entity2.posZ);
                for (BlockPos blockPos : default_blocks) {

                    //Ignored the target far from us
                    if (entity2.getDistanceSq(blockPos) >= Distance_value.getValue() * Distance_value.getValue())
                        continue;

                    //Ignored the target over place range
                    if (mc.player.getDistance(blockPos.x, blockPos.y, blockPos.z) > PlaceRange_value.getValue())
                        continue;

                    double targetDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2, targetVec);

                    //Ignored the target damage lower than currently highest target
                    if (targetDamage < damage) continue;

                    //Ignored target that damage lower than MinDamage we set
                    //If we can face place set the min damage to 1
                    if (targetDamage < (FacePlace_value.getValue() ? (canFacePlace((EntityLivingBase) entity2, BlastHealth_value.getValue()) ? 1 : MinDmg_value.getValue()) : MinDmg_value.getValue()))
                        continue;

                    float healthTarget = ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount();
                    float healthSelf = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                    double selfDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, mc.player);

                    //We dont want to be damaged deeper than enemy
                    if (selfDamage > targetDamage && targetDamage < healthTarget) continue;

                    //We dont want to suicide
                    if (selfDamage - 0.5 > healthSelf && NoSuicide_value.getValue()) continue;

                    //Ignored the target that damage higher than MaxSelfDamage we set
                    if (selfDamage > MaxSelf_value.getValue()) continue;

                    //Wall calculation
                    if (!Wall_value.getValue()
                            && WallRange_value.getValue() > 0
                            && !CanSeeBlock(blockPos)
                            && getVecDistance(blockPos, mc.player.posX, mc.player.posY, mc.player.posZ) >= WallRange_value.getValue()
                    ) continue;

                    damage = targetDamage;
                    tempBlock = blockPos;
                    target = entity2;

                    //Put render dmg
                    if (RenderDmg_value.getValue()) renderBlockDmg.put(tempBlock, targetDamage);

                    //GhostHand stuff
                    if (GhostHand_value.getValue()) GhostHand();

                }
                /*
                  We dont want to make our computer lag.
                  If we have target,then we stop calculating.
                 */
                if (target != null) break;
            }
        }
        return new CrystalTarget(tempBlock, target);
    }

    private void GhostHand() {
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY) continue;
                if (stack.getItem() == Items.END_CRYSTAL) {
                    PrevSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = i;
                    mc.playerController.updateController();
                }
            }
        }
    }

    private List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        if (TargetPlayer_value.getValue()) {
            entities.addAll(mc.world.playerEntities.stream()
                    .filter(entityPlayer -> !FriendManager.isFriend(entityPlayer.getName()))
                    .filter(entity -> mc.player.getDistance(entity) < Distance_value.getValue())
                    .collect(Collectors.toList()));
        }
        entities.addAll(mc.world.loadedEntityList.stream()
                .filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? TargetAnimals_value.getValue() : TargetMobs_value.getValue()))
                .filter(entity -> !(entity instanceof EntityPlayer))
                .filter(entity -> mc.player.getDistance(entity) < Distance_value.getValue())
                .collect(Collectors.toList()));

        for (Entity ite2 : new ArrayList<>(entities)) {
            if (mc.player.getDistance(ite2) > Distance_value.getValue()) entities.remove(ite2);
            if (ite2 == mc.player) entities.remove(ite2);
        }
        entities.sort(Comparator.comparingDouble(entity -> entity.getDistance(mc.player)));
        return entities;
    }

    private static class CrystalTarget {
        public BlockPos blockPos;
        public Entity target;

        public CrystalTarget(BlockPos block, Entity target) {
            this.blockPos = block;
            this.target = target;
        }
    }

    public int getColor() {
        float[] tick_color = {(System.currentTimeMillis() % 11520L) / 11520.0f * RGBSpeed_value.getValue()};
        int color_rgb = Color.HSBtoRGB(tick_color[0], Saturation_value.getValue(), Brightness_value.getValue());
        return Rainbow_value.getValue() ?
                new Color(color_rgb >> 16 & 0xFF, color_rgb >> 8 & 0xFF, color_rgb & 0xFF, Alpha_value.getValue()).getRGB() :
                new Color(Red_value.getValue(), Green_value.getValue(), Blue_value.getValue(), Alpha_value.getValue()).getRGB();
    }


    @Override
    public void onWorldRender(RenderEvent event) {
        int color = SyncGui_value.getValue() ?
                new Color(GuiManager.getINSTANCE().getRed(), GuiManager.getINSTANCE().getGreen(), GuiManager.getINSTANCE().getBlue(), Alpha_value.getValue()).getRGB() :
                getColor();
        if (renderBlock != null) drawBlock(renderBlock, color);
    }

    @Override
    public void onEnable() {
        placeTimer.reset();
        breakTimer.reset();
        renderBlockDmg.clear();
    }

    @Override
    public void onDisable() {
        rotating = false;
        resetRotation();
        renderBlock = null;
        target = null;
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    @Override
    public String getHudInfo() {
        if (target == null) return "";
        else return target.getName();
    }

}
