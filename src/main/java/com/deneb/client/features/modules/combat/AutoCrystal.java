package com.deneb.client.features.modules.combat;

import com.deneb.client.client.FriendManager;
import com.deneb.client.client.GuiManager;
import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.event.events.render.RenderEvent;
import com.deneb.client.event.events.render.RenderModelEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.utils.*;
import com.deneb.client.utils.Timer;
import com.deneb.client.value.*;
import com.deneb.client.value.MValue.Mode;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by B_312 on 01/15/21
 */
@Module.Info(name = "AutoCrystal", category = Category.COMBAT)
public class AutoCrystal extends Module {

    MValue Page_value = setting("Page", new Mode("General", true), new Mode("Calculation"), new Mode("Render"));
    //General
    BValue AutoSwitch_value = setting("AutoSwitch", false).visibility(v -> Page_value.page("General"));
    BValue TargetPlayer_value = setting("Players", true).visibility(v -> Page_value.page("General"));
    BValue TargetMobs_value = setting("Mobs", false).visibility(v -> Page_value.page("General"));
    BValue TargetAnimals_value = setting("Animals", false).visibility(v -> Page_value.page("General"));
    BValue Place_value = setting("Place", true).visibility(v -> Page_value.page("General"));
    BValue Explode_value = setting("Explode", true).visibility(v -> Page_value.page("General"));
    BValue MultiPlace_value = setting("MultiPlace", false).visibility(v -> Page_value.page("General"));
    IValue AttackDelay_value = setting("AttackDelay", 35, 0, 1000).visibility(v -> Page_value.page("General"));
    DValue PlaceSpeed_value = setting("PlaceSpeed", 35d, 0d, 30d).visibility(v -> Page_value.page("General"));
    DValue Distance_value = setting("Distance", 7.0D, 0D, 8D).visibility(v -> Page_value.page("General"));
    DValue PlaceRange_value = setting("PlaceRange", 6.5D, 0D, 8D).visibility(v -> Page_value.page("General"));
    DValue HitRange_value = setting("HitRange", 5.5D, 0D, 8D).visibility(v -> Page_value.page("General"));
    DValue MinDmg_value = setting("Min Damage", 4.5d, 0d, 20d).visibility(v -> Page_value.page("General"));
    DValue MaxSelf_value = setting("MaxSelfDamage", 10d, 0d, 36d).visibility(v -> Page_value.page("General"));
    BValue Rotate_value = setting("Rotate", true).visibility(v -> Page_value.page("General"));
    BValue RayTrace_value = setting("RayTrace", false).visibility(v -> Page_value.page("General"));
    //Calculation
    BValue NewPlace_value = setting("1.13Place", false).visibility(v -> Page_value.page("Calculation"));
    BValue Wall_value = setting("Wall", true).visibility(v -> Page_value.page("Calculation"));
    DValue Walls_value = setting("WallRange", 3.5, 0d, 20d).visibility(v -> Page_value.page("Calculation"));
    BValue NoSuicide_value = setting("NoSuicide", true).visibility(v -> Page_value.page("Calculation"));
    BValue FacePlace_value = setting("FacePlace", true).visibility(v -> Page_value.page("Calculation"));
    DValue BlastHealth_value = setting("MinHealthFace", 10d, 0d, 20d).visibility(v -> FacePlace_value.getValue() && Page_value.page("Calculation"));
    DValue BMinDmg_value = setting("BreakMinDmg", 4.5, 0.0, 36.0).visibility(v -> Page_value.page("Calculation"));
    DValue BMaxSelf_value = setting("BreakMaxSelf", 12.0, 0.0, 36.0).visibility(v -> Page_value.page("Calculation"));
    MValue AttackMode_value = setting("HitMode", new Mode("Smart", true), new Mode("Always")).visibility(v -> Page_value.page("Calculation"));
    BValue GhostHand_value = setting("GhostHand", false).visibility(v -> Page_value.page("Calculation"));
    BValue PauseEating_value = setting("PauseWhileEating", false).visibility(v -> Page_value.page("Calculation"));
    //Render
    BValue RenderDmg_value = setting("RenderDamage", false).visibility(v -> Page_value.page("Render"));
    MValue RenderMode_value = setting("RenderBlock", new Mode("Solid", true), new Mode("Up"), new Mode("UpLine"), new Mode("Full"), new Mode("Outline"), new Mode("NoRender")).visibility(v -> Page_value.page("Render"));
    BValue SyncGui_value = setting("SyncGui", false).visibility(v -> Page_value.page("Render"));
    IValue Red_value = setting("Red", 255, 0, 255).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    IValue Green_value = setting("Green", 0, 0, 255).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    IValue Blue_value = setting("Blue", 0, 0, 255).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    IValue Alpha_value = setting("Alpha", 70, 0, 255).visibility(v -> Page_value.page("Render"));
    BValue Rainbow_value = setting("Rainbow", false).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    FValue RGBSpeed_value = setting("RGB Speed", 1.0f, 0.0f, 10.0f).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    FValue Saturation_value = setting("Saturation", 0.65f, 0.0f, 1.0f).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());
    FValue Brightness_value = setting("Brightness", 1.0f, 0.0f, 1.0f).visibility(v -> Page_value.page("Render") && !SyncGui_value.getValue());

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
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            mc.rightClickDelayTimer = 0;
        }
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
        if(rotating){
            event.rotating = true;
            event.pitch = (float) renderPitch;
        }
    }

    @SubscribeEvent
    public void updateRotation(RenderWorldLastEvent event){
        if(!Rotate_value.getValue()) return;
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
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
                isSpoofingAngles = false;
            }
        }
    }

    private boolean canHitCrystal(EntityEnderCrystal entity) {
        if (mc.player.getDistance(entity) > HitRange_value.getValue()) return false;
        if (AttackMode_value.getToggledMode().getName().equals("Smart")) {
            float selfDamage = calculateDamage(entity.posX, entity.posY, entity.posZ, mc.player, mc.player.getPositionVector());
            if (selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player == mc.player || FriendManager.isFriend(player.getName()) || mc.player.isDead
                        || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= 0.0f) continue;
                double minDamage = BMinDmg_value.getValue();
                double maxSelf = BMaxSelf_value.getValue();
                if (canFacePlace(player, BlastHealth_value.getValue())) {
                    minDamage = 1;
                }
                double target = calculateDamage(entity.posX, entity.posY, entity.posZ, player, player.getPositionVector());
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
            //rotating = false;
        }
    }

    private void ExplodeCrystal(EntityEnderCrystal crystal) {
        if (Rotate_value.getValue()) lookAtCrystal(crystal);
        if (breakTimer.passed(AttackDelay_value.getValue())) {
            mc.playerController.attackEntity(mc.player, crystal);
            mc.player.swingArm(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
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
            if (hasDelayRun(PlaceSpeed_value.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, facing, enumHand, 0, 0, 0));
                placeTimer.reset();
                Placements++;
            }
        }
    }

    private boolean hasDelayRun(double placeSpeed) {
        return placeTimer.passed(1000 / placeSpeed);
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

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if(placeTimer.passed(1050) || breakTimer.passed(1050)) rotating = false;
        if (PauseEating_value.getValue() && IsEating()) return;
        EntityEnderCrystal c = mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal &&
                canHitCrystal((EntityEnderCrystal) e)).map(e -> (EntityEnderCrystal) e).min(Comparator.comparing(e -> mc.player.getDistance(e))).orElse(null);
        if (Explode_value.getValue() && c != null) {
            if (mc.player.canEntityBeSeen(c) || (mc.player.getDistance(c) < Walls_value.getValue() && Wall_value.getValue()) || !Wall_value.getValue()) {
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

    private CrystalTarget Calculator() {
        double Damage = 0.5;
        BlockPos q = null;
        Entity target = null;
        List<BlockPos> default_blocks;
        default_blocks = findCrystalBlocks(PlaceRange_value.getValue());
        List<Entity> entities = getEntities();
        PrevSlot = -1;
        for (Entity entity2 : entities) {
            if (entity2 != mc.player) {
                if (((EntityLivingBase) entity2).getHealth() <= 0.0f) continue;
                target = null;
                Vec3d targetVec = new Vec3d(entity2.posX, entity2.posY, entity2.posZ);
                for (BlockPos blockPos : default_blocks) {
                    if (entity2.getDistanceSq(blockPos) >= Distance_value.getValue() * Distance_value.getValue())
                        continue;
                    if (mc.player.getDistance(blockPos.x, blockPos.y, blockPos.z) > PlaceRange_value.getValue())
                        continue;
                    double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2, targetVec);
                    if (d < Damage) continue;
                    if (d < (FacePlace_value.getValue() ? (canFacePlace((EntityLivingBase) entity2, BlastHealth_value.getValue()) ? 1 : MinDmg_value.getValue()) : MinDmg_value.getValue()))
                        continue;
                    float healthTarget = ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount();
                    float healthSelf = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                    double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, mc.player);
                    if (self > d && d < healthTarget) continue;
                    if (self - 0.5 > healthSelf && NoSuicide_value.getValue()) continue;
                    if (self > MaxSelf_value.getValue()) continue;
                    if (!Wall_value.getValue()) if (Walls_value.getValue() > 0) if (!CanSeeBlock(blockPos))
                        if (getVecDistance(blockPos, mc.player.posX, mc.player.posY, mc.player.posZ) > Walls_value.getValue())
                            continue;
                    Damage = d;
                    q = blockPos;
                    target = entity2;
                    renderBlockDmg.put(q, d);
                    if (GhostHand_value.getValue()) GhostHand();
                }
                if (target != null) break;
            }
        }
        return new CrystalTarget(q, target);
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
