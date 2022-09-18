package net.spartanb312.cursa.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.FriendManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.concurrent.repeat.RepeatUnit;
import net.spartanb312.cursa.core.event.Listener;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.event.events.render.RenderEvent;
import net.spartanb312.cursa.event.events.render.RenderModelEvent;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorCPacketPlayer;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorCPacketUseEntity;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorMinecraft;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.notification.NotificationManager;
import net.spartanb312.cursa.utils.*;
import net.spartanb312.cursa.utils.graphics.RenderUtils3D;
import net.spartanb312.cursa.utils.math.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.repeat;
import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runRepeat;
import static net.spartanb312.cursa.utils.CrystalUtil.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by B_312 on 01/15/21
 * Updated by B_312 on 10/30/21
 */
@Parallel(runnable = true)
@ModuleInfo(name = "CursaAura", category = Category.COMBAT, description = "Place crystals to kill enemies")
public strictfp class CursaAura extends Module {

    public static CursaAura INSTANCE;

    Setting<Page> page = setting("Page", Page.General);

    //General
    Setting<Boolean> autoSwitch = setting("AutoSwitch", false).whenAtMode(page, Page.General).des("主手模式下自动切换水晶");
    Setting<Integer> updateDelay = setting("UpdateDelay", 10, 1, 50).whenAtMode(page, Page.General).des("Loop updating delay");
    Setting<Boolean> targetPlayer = setting("Players", true).whenAtMode(page, Page.General).des("Player targets");
    Setting<Boolean> targetMob = setting("Mobs", false).whenAtMode(page, Page.General).des("Mob targets");
    Setting<Boolean> targetAnimal = setting("Animals", false).whenAtMode(page, Page.General).des("Animal targets");
    Setting<Boolean> autoPlace = setting("Place", true).whenAtMode(page, Page.General).des("Automatically place crystals");
    Setting<Boolean> autoExplode = setting("Explode", true).whenAtMode(page, Page.General).des("Automatically explode crystals");
    Setting<Boolean> multiPlace = setting("MultiPlace", false).whenAtMode(page, Page.General).des("Place multi crystals before explode");
    Setting<Double> attackSpeed = setting("AttackSpeed", 35d, 0, 50).whenAtMode(page, Page.General).des("Speed of breaking crystal");
    Setting<Double> placeSpeed = setting("PlaceSpeed", 35d, 0d, 50).whenAtMode(page, Page.General).des("Speed of placing crystal");
    Setting<Double> distance = setting("Distance", 7.0D, 0D, 8D).whenAtMode(page, Page.General).des("Calculation working range");
    Setting<Double> placeRange = setting("PlaceRange", 4.5D, 0D, 8D).whenAtMode(page, Page.General).des("Max place range");
    Setting<Double> hitRange = setting("HitRange", 5.5D, 0D, 8D).whenAtMode(page, Page.General).des("Max break range");
    Setting<Boolean> rotate = setting("Rotate", true).whenAtMode(page, Page.General).des("Rotation packets and animation");
    Setting<Boolean> rayTrace = setting("RayTrace", false).whenAtMode(page, Page.General).des("Ray trace block side");
    //Place
    Setting<Boolean> oneThirtyPlace = setting("1.13Place", false).whenAtMode(page, Page.Place).des("Place mode in 1.13 server");
    Setting<Boolean> wallPlace = setting("WallPlace", true).whenAtMode(page, Page.Place).des("Wall place calculation");
    Setting<Double> placeWallRange = setting("PlaceWallRange", 3.0, 0.0, 5.0).whenAtMode(page, Page.Place).des("Wall place calculation range");
    Setting<Boolean> noSuicide = setting("NoSuicide", true).whenAtMode(page, Page.Place).des("No suicide");
    Setting<Boolean> facePlace = setting("FacePlace", true).whenAtMode(page, Page.Place).des("Force place while enemies are nearly dying");
    Setting<Double> blastHealth = setting("MinHealthFace", 10d, 0d, 20d).whenAtMode(page, Page.Place).whenTrue(facePlace).des("When enemy's health is lower than this then we starting face place");
    Setting<Double> placeMinDmg = setting("PlaceMinDamage", 4.5d, 0d, 20d).whenAtMode(page, Page.Place).des("Each placement must make damage on enemy higher than this");
    Setting<Double> placeMaxSelf = setting("PlaceMaxSelf", 10d, 0d, 36d).whenAtMode(page, Page.Place).des("Each placement must make damage on self lower than this");
    Setting<Boolean> ghostHand = setting("GhostHand", false).whenAtMode(page, Page.Place).des("Ghost hand means that you don't need to switch to crystal to place");
    //Break
    Setting<AttackMode> attackMode = setting("HitMode", AttackMode.Smart).whenAtMode(page, Page.Break).des("Always or Smart(With break calculation)");
    Setting<Boolean> antiWeakness = setting("AntiWeakness", false).whenAtMode(page, Page.Break).des("Using a tool to break crystals while you having weak");
    Setting<Boolean> wallBreak = setting("WallBreak", true).whenAtMode(page, Page.Break).des("Try to break crystals behind wall");
    Setting<Double> breakWallRange = setting("BreakWallRange", 3.0, 0.0, 5.0).whenAtMode(page, Page.Break).whenTrue(wallBreak).des("Max wall range to try");
    Setting<Double> breakMinDmg = setting("BreakMinDmg", 4.5, 0.0, 36.0).whenAtMode(page, Page.Break).whenAtMode(attackMode, AttackMode.Smart).des("Each break must make damage on enemy higher than this");
    Setting<Double> breakMaxSelf = setting("BreakMaxSelf", 12.0, 0.0, 36.0).whenAtMode(page, Page.Break).whenAtMode(attackMode, AttackMode.Smart).des("Each break must make damage on self lower than this");
    Setting<Boolean> lethalTryIt = setting("LethalTryIt", true).whenAtMode(page, Page.Break).whenAtMode(attackMode, AttackMode.Smart).des("If we can pop enemy's totem,we take a risk");
    Setting<Boolean> pauseWhileEating = setting("PauseWhileEating", false).whenAtMode(page, Page.Break).des("Pause CursaAura while you are eating");
    Setting<Boolean> clientSide = setting("ClientSideConfirm", false).whenAtMode(page, Page.Break).des("Client side remove crystal that you've attacked");
    Setting<Boolean> predictExplode = setting("PredictExplode", false).whenAtMode(page, Page.Break).des("Predict next crystal's entity id and explode it(Unsafe)");
    Setting<Integer> predictExplodeCount = setting("PredictHitCount", 2, 1, 20).whenAtMode(page, Page.Break).whenTrue(predictExplode).des("Predict count");
    Setting<Boolean> inhibit = setting("Inhibit", true).whenAtMode(page, Page.Break).des("Inhibit crystal in crystal spawn packet");
    Setting<Boolean> clearClickDelay = setting("ClearClickDelay", true).whenAtMode(page, Page.Break).des("Clear minecraft click cool down");
    //Render
    Setting<Boolean> renderDmg = setting("RenderDamage", false).whenAtMode(page, Page.Render).des("Render damage on targeted block");
    Setting<RenderMode> renderMode = setting("RenderBlock", RenderMode.Full).whenAtMode(page, Page.Render).des("The render type");
    Setting<Boolean> syncGUI = setting("SyncGui", false).whenAtMode(page, Page.Render).des("Synchronize color with GUI");
    Setting<Integer> red = setting("Red", 255, 0, 255).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Custom color Red");
    Setting<Integer> green = setting("Green", 0, 0, 255).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Custom color Green");
    Setting<Integer> blue = setting("Blue", 0, 0, 255).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Custom color Blue");
    Setting<Integer> transparency = setting("Alpha", 26, 0, 255).whenAtMode(page, Page.Render).des("Custom transparency");
    Setting<Boolean> rainbow = setting("Rainbow", false).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Rainbow dynamic color");
    Setting<Float> rainbowSpeed = setting("RGB Speed", 1.0f, 0.0f, 10.0f).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Rainbow color change speed");
    Setting<Float> saturation = setting("Saturation", 0.65f, 0.0f, 1.0f).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Rainbow color saturation");
    Setting<Float> brightness = setting("Brightness", 1.0f, 0.0f, 1.0f).whenAtMode(page, Page.Render).whenFalse(syncGUI).des("Rainbow color brightness");

    enum RenderMode {Solid, Up, UpLine, Full, Outline, OFF}

    enum AttackMode {Smart, Always}

    enum Page {General, Place, Break, Render}

    transient public static float yaw;
    transient public static float pitch;
    transient public static float renderPitch;
    transient public static boolean shouldSpoofPacket;
    transient public static Entity target;
    transient private int placements = 0;
    transient private int oldSlot = -1;
    transient private int prevSlot;
    transient private boolean switchCoolDown;
    transient private boolean togglePitch = false;
    transient private BlockPos renderBlock;
    transient private static boolean rotating = false;
    transient Timer placeTimer = new Timer();
    transient Timer breakTimer = new Timer();
    transient Timer packetBreakTimer = new Timer();
    transient AtomicBoolean shouldIgnoreEntity = new AtomicBoolean(false);

    transient AtomicInteger lastEntityId = new AtomicInteger(-1);

    public final LocalTarget localTarget = new LocalTarget();
    private final HashMap<BlockPos, Double> renderBlockDmg = new HashMap<>();

    RepeatUnit placeCalculation = new RepeatUnit(() -> (int) ((1000 / placeSpeed.getValue()) - 5) / (multiPlace.getValue() ? 1 : 2), () -> {
        if (mc.player == null || mc.world == null) return;
        if (multiPlace.getValue()) localTarget.putPlaceTarget(calculator(shouldIgnoreEntity.get()));
    }).timeOut(it -> NotificationManager.error("Can't keep up!The calculation can't catch up with your place speed!Decrease PlaceSpeed plz!"));

    RepeatUnit breakCalculation = new RepeatUnit(() -> (int) ((1000 / attackSpeed.getValue()) - 5), () -> {
        if (mc.player == null || mc.world == null) return;
        localTarget.putAttackTarget(new ArrayList<>(mc.world.loadedEntityList)
                .stream()
                .filter(e -> e instanceof EntityEnderCrystal && canHitCrystal(e.getPositionVector()))
                .map(e -> (EntityEnderCrystal) e)
                .min(Comparator.comparing(e -> mc.player.getDistance(e))).orElse(null));
    }).timeOut(it -> NotificationManager.error("Can't keep up!The calculation can't catch up with your attack speed!Decrease BreakSpeed plz!"));

    List<RepeatUnit> repeatUnits = new ArrayList<>();

    public CursaAura() {
        INSTANCE = this;
        repeatUnits.add(placeCalculation);
        repeatUnits.add(breakCalculation);
        repeatUnits.add(updateAutoCrystal);
        repeatUnits.forEach(it -> {
            it.suspend();
            runRepeat(it);
        });
    }

    @Override
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || mc.world == null) return;
        if (clearClickDelay.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            ((AccessorMinecraft) mc).setRightClickDelayTimer(0);
        }
        if (event.packet instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.packet;
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                new ArrayList<>(mc.world.loadedEntityList).stream()
                        .filter(it -> it instanceof EntityEnderCrystal)
                        .filter(it -> it.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f)
                        .forEach(Entity::setDead);
            }
        }
        if (event.packet instanceof SPacketSpawnObject) {
            if (((SPacketSpawnObject) (event.packet)).getType() == 51) {
                lastEntityId.getAndUpdate(it -> Math.max(it, ((SPacketSpawnObject) (event.packet)).getEntityID()));
                if (inhibit.getValue()) packetBreak(mc.player, (SPacketSpawnObject) event.packet);
            } else {
                lastEntityId.set(-1);
            }
        }
    }

    private void packetBreak(EntityPlayerSP player, SPacketSpawnObject packet) {
        if ((1000 / attackSpeed.getValue()) > 0 && !packetBreakTimer.passed((1000 / attackSpeed.getValue()))) return;
        double distance = player.getDistance(packet.getX(), packet.getY(), packet.getZ());
        if (distance > hitRange.getValue()) return;
        Vec3d pos = new Vec3d(packet.getX(), packet.getY(), packet.getZ());
        if (wallBreak.getValue() && distance > breakWallRange.getValue() && !canSeeBlock(new BlockPos(pos)))
            return;
        if (!canHitCrystal(pos)) return;
        CPacketUseEntity attackPacket = new CPacketUseEntity();
        setEntityId(attackPacket, packet.getEntityID());
        setAction(attackPacket, CPacketUseEntity.Action.ATTACK);
        mc.player.connection.sendPacket(attackPacket);
        if (rotate.getValue()) lookAt(pos);
        packetBreakTimer.reset();
    }

    public static void setAction(CPacketUseEntity packet, CPacketUseEntity.Action action) {
        ((AccessorCPacketUseEntity) packet).setAction(action);
    }

    public static void setEntityId(CPacketUseEntity packet, int entityId) {
        ((AccessorCPacketUseEntity) packet).setId(entityId);
    }

    @Listener
    public void renderModelRotation(RenderModelEvent event) {
        if (!rotate.getValue()) return;
        if (rotating) {
            event.rotating = true;
            event.pitch = renderPitch;
        }
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null) return;
        if (!rotate.getValue()) {
            return;
        }
        if (event.packet instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.packet;
            if (shouldSpoofPacket) {
                ((AccessorCPacketPlayer) packet).setYaw(yaw);
                ((AccessorCPacketPlayer) packet).setPitch(pitch);
                shouldSpoofPacket = false;
            }
        }
    }


    /**
     * Calculation of explosion
     * If return true,then we can explode it.
     */
    private boolean canHitCrystal(Vec3d crystal) {
        if (mc.player.getDistance(crystal.x, crystal.y, crystal.z) > hitRange.getValue()) return false;
        if (attackMode.getValue().equals(AttackMode.Smart)) {
            float selfDamage = calculateDamage(crystal.x, crystal.y, crystal.z, mc.player, mc.player.getPositionVector());
            if (selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
            List<EntityPlayer> entities = new ArrayList<>(mc.world.playerEntities);
            entities = entities.stream()
                    .filter(e -> mc.player.getDistance(e) <= distance.getValue())
                    .filter(e -> mc.player != e)
                    .filter(e -> !FriendManager.isFriend(e))
                    .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                    .collect(Collectors.toList());
            for (EntityPlayer player : entities) {
                if (player.isDead || (player.getHealth() + player.getAbsorptionAmount()) <= 0.0f) continue;
                double minDamage = breakMinDmg.getValue();
                double maxSelf = breakMaxSelf.getValue();
                if (canFacePlace(player, blastHealth.getValue())) {
                    minDamage = 1;
                }
                double target = calculateDamage(crystal.x, crystal.y, crystal.z, player, player.getPositionVector());
                if (target > player.getHealth() + player.getAbsorptionAmount()
                        && selfDamage < mc.player.getHealth() + mc.player.getAbsorptionAmount()
                        && lethalTryIt.getValue()
                ) return true;
                if (selfDamage > maxSelf) continue;
                if (target < minDamage) continue;
                if (selfDamage > target) continue;
                return true;
            }
        } else return true;
        return false;
    }

    private void resetRotation() {
        if (shouldSpoofPacket) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            shouldSpoofPacket = false;
            rotating = false;
        }
    }

    private void explodeCrystal(EntityEnderCrystal crystal) {
        if (rotate.getValue()) lookAtCrystal(crystal);
        if (breakDelayRun(attackSpeed.getValue())) {
            if (crystal != null) {
                mc.playerController.attackEntity(mc.player, crystal);
                mc.player.swingArm(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                if (clientSide.getValue()) {
                    if (mc.player.canEntityBeSeen(crystal)) crystal.setDead();
                }
                mc.player.resetCooldown();
            }
            breakTimer.reset();
        }
    }

    private void placeCrystal(BlockPos targetBlock, EnumHand enumHand) {
        EnumFacing facing = rayTrace.getValue() ? enumFacing(targetBlock) : EnumFacing.UP;
        if (rotate.getValue()) lookAtPos(targetBlock, facing);
        if (placeDelayRun(placeSpeed.getValue())) {
            repeat(3, () -> {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, facing, enumHand, 0, 0, 0));
                placements++;
            });
            if (predictExplode.getValue()) {
                int syncedId = lastEntityId.get();
                AtomicInteger count = new AtomicInteger(0);
                repeat(predictExplodeCount.getValue(), () -> {
                    if (syncedId != -1) {
                        CPacketUseEntity attackPacket = new CPacketUseEntity();
                        setEntityId(attackPacket, syncedId + count.getAndIncrement() + 1);
                        setAction(attackPacket, CPacketUseEntity.Action.ATTACK);
                        mc.player.connection.sendPacket(attackPacket);
                    }
                });
            }
            placeTimer.reset();
        }
    }

    private boolean placeDelayRun(double speed) {
        return placeTimer.passed(1000 / speed);
    }

    private boolean breakDelayRun(double speed) {
        return breakTimer.passed(1000 / speed);
    }

    private List<BlockPos> findCrystalBlocks(double range) {
        NonNullList<BlockPos> newList = NonNullList.create();
        newList.addAll(BlockInteractionHelper.getSphere(getPlayerPos(), (float) range, (int) range, false, true, 0)
                .stream()
                .filter(it -> canPlaceCrystal(it, oneThirtyPlace.getValue())).collect(Collectors.toList()));
        return newList;
    }

    private void drawBlock(BlockPos blockPos, int color) {
        RenderUtils3D.prepare(GL_QUADS);
        if (!renderMode.getValue().equals(RenderMode.OFF)) {
            if (renderMode.getValue().equals(RenderMode.Solid) || renderMode.getValue().equals(RenderMode.Up)) {
                if (renderMode.getValue().equals(RenderMode.Up)) {
                    RenderUtils3D.drawBox(blockPos, color, GeometryMasks.Quad.UP);
                } else {
                    RenderUtils3D.drawBox(blockPos, color, GeometryMasks.Quad.ALL);
                }
            } else {
                if (renderMode.getValue().equals(RenderMode.Full)) {
                    RenderUtils3D.drawFullBox(blockPos, 1f, color);
                } else if (renderMode.getValue().equals(RenderMode.Outline)) {
                    RenderUtils3D.drawBoundingBox(blockPos, 2f, color);
                } else {
                    RenderUtils3D.drawBoundingBox(blockPos.add(0, 1, 0), 2f, color);
                }
            }
        }
        RenderUtils3D.release();
        if (renderDmg.getValue()) {
            if (renderBlockDmg.containsKey(blockPos)) {
                GlStateManager.pushMatrix();
                glBillboardDistanceScaled((float) blockPos.getX() + 0.5f, (float) blockPos.getY() + 0.5f, (float) blockPos.getZ() + 0.5f, mc.player);
                final double damage = renderBlockDmg.get(blockPos);
                final String damageText = "DMG: " + (Math.floor(damage) == damage ? (int) damage : String.format("%.1f", damage));
                GlStateManager.disableDepth();
                GlStateManager.translate(-(FontManager.fontRenderer.getWidth(damageText) / 2.0d), 0, 0);
                FontManager.fontRenderer.drawStringWithShadow(damageText, 0, 0, -1);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean canFacePlace(EntityLivingBase target, double blast) {
        float healthTarget = target.getHealth() + target.getAbsorptionAmount();
        return healthTarget <= blast;
    }

    public boolean canPlaceCrystal(BlockPos blockPos, boolean newPlace) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);

        Block base = mc.world.getBlockState(blockPos).getBlock();
        Block b1 = mc.world.getBlockState(boost).getBlock();
        Block b2 = mc.world.getBlockState(boost2).getBlock();

        if (base != Blocks.BEDROCK && base != Blocks.OBSIDIAN) return false;

        if (b1 != Blocks.AIR && !isReplaceable(b1)) return false;
        if (!newPlace && b2 != Blocks.AIR) return false;

        AxisAlignedBB box = new AxisAlignedBB(
                blockPos.getX(), blockPos.getY() + 1.0, blockPos.getZ(),
                blockPos.getX() + 1.0, blockPos.getY() + 3.0, blockPos.getZ() + 1.0
        );

        List<Entity> entities = new ArrayList<>(mc.world.loadedEntityList);

        for (Entity entity : entities) {
            if (shouldIgnoreEntity.get() && !multiPlace.getValue() && entity instanceof EntityEnderCrystal) continue;
            if (entity.getEntityBoundingBox().intersects(box)) return false;
        }
        return true;
    }

    public void lookAtPos(BlockPos block, EnumFacing face) {
        float[] v = RotationUtil.getRotationsBlock(block, face, false);
        float[] v2 = RotationUtil.getRotationsBlock(block.add(0, +0.5, 0), face, false);
        setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void lookAt(Vec3d vec3d) {
        float[] v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), vec3d);
        float[] v2 = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), vec3d.add(0, -0.5, 0));
        setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void lookAtCrystal(EntityEnderCrystal ent) {
        float[] v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector());
        float[] v2 = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector().add(0, -0.5, 0));
        setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void setYawAndPitch(float yaw1, float pitch1, float renderPitch1) {
        yaw = yaw1;
        pitch = pitch1;
        renderPitch = renderPitch1;
        mc.player.rotationYawHead = yaw;
        mc.player.renderYawOffset = yaw;
        shouldSpoofPacket = true;
        rotating = true;
    }

    boolean antiWeak = false;
    int weakPreSlot = -1;

    RepeatUnit updateAutoCrystal = new RepeatUnit(() -> updateDelay.getValue(), () -> {
        if (mc.player == null || mc.world == null) return;

        if (placeTimer.passed(1050) || breakTimer.passed(1050)) rotating = false;
        if (pauseWhileEating.getValue() && isEating()) return;

        localTarget.checkAll((int) (1000 / placeSpeed.getValue()), (int) (1000 / attackSpeed.getValue()));

        EntityEnderCrystal attackCrystalTarget = localTarget.getAttackTarget();

        if (autoExplode.getValue() && attackCrystalTarget != null) {
            if (!mc.player.canEntityBeSeen(attackCrystalTarget) && mc.player.getDistance(attackCrystalTarget) > breakWallRange.getValue() && wallBreak.getValue()) {
                shouldIgnoreEntity.set(false);
            } else {

                if (antiWeakness.getValue() && !antiWeak && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    antiWeak = true;
                    weakPreSlot = mc.player.inventory.currentItem;
                    int sword = ItemUtils.findItemInHotBar(Items.DIAMOND_SWORD);
                    int pickaxe = ItemUtils.findItemInHotBar(Items.DIAMOND_PICKAXE);
                    ItemUtils.switchToSlot(sword != -1 ? sword : pickaxe);
                }
                explodeCrystal(attackCrystalTarget);
                if (antiWeak) {
                    antiWeak = false;
                    ItemUtils.switchToSlot(weakPreSlot);
                    return;
                }

                if (!multiPlace.getValue()) shouldIgnoreEntity.set(true);
                if (placements >= 3) {
                    placements = 0;
                    shouldIgnoreEntity.set(true);
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
        BlockPos targetBlock = null;
        CrystalTarget placeTarget = multiPlace.getValue() ? localTarget.getPlaceTarget(shouldIgnoreEntity.get()) : calculator(shouldIgnoreEntity.get());
        if (placeTarget != null) {
            target = placeTarget.target;
            targetBlock = placeTarget.blockPos;
            if (ghostHand.getValue()) ghostHand();
        }
        if (target == null || targetBlock == null) {
            renderBlock = null;
            resetRotation();
            return;
        }
        renderBlock = targetBlock;
        if (autoPlace.getValue()) {
            EnumHand enumHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (autoSwitch.getValue()) {
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
            placeCrystal(targetBlock, enumHand);
        }
        if (rotate.getValue() && shouldSpoofPacket) {
            if (togglePitch) {
                mc.player.rotationPitch += 0.0004;
                togglePitch = false;
            } else {
                mc.player.rotationPitch -= 0.0004;
                togglePitch = true;
            }
        }
        if (prevSlot != -1 && ghostHand.getValue()) {
            mc.player.inventory.currentItem = prevSlot;
            mc.playerController.updateController();
        }
    }).timeOut(it -> NotificationManager.error("Can't keep up!The calculation can't catch up with your update speed!Increase Thread Delay plz!"));

    /**
     * Calculation of target block to place crystal
     * return a target contains coordinate and target entity.
     */
    public CrystalTarget calculator(boolean ignoredEntity) {
        double damage = 0.5;
        BlockPos tempBlock = null;
        Entity target = null;
        List<BlockPos> crystalBlocks = findCrystalBlocks(distance.getValue());
        List<Entity> entities = getEntities();
        prevSlot = -1;
        for (Entity entity2 : entities) {
            if (entity2 != mc.player) {
                if (((EntityLivingBase) entity2).getHealth() <= 0.0f) continue;
                Vec3d targetVec = new Vec3d(entity2.posX, entity2.posY, entity2.posZ);
                for (BlockPos blockPos : crystalBlocks) {
                    if (entity2.getDistanceSq(blockPos) >= distance.getValue() * distance.getValue()) continue;
                    if (mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > placeRange.getValue())
                        continue;
                    double targetDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity2, targetVec);
                    if (targetDamage < damage) continue;
                    if (targetDamage < (facePlace.getValue() ? (canFacePlace((EntityLivingBase) entity2, blastHealth.getValue()) ? 1 : placeMinDmg.getValue()) : placeMinDmg.getValue()))
                        continue;
                    float healthTarget = ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount();
                    float healthSelf = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                    double selfDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, mc.player);
                    if (selfDamage > targetDamage && targetDamage < healthTarget) continue;
                    if (selfDamage - 0.5 > healthSelf && noSuicide.getValue()) continue;
                    if (selfDamage > placeMaxSelf.getValue()) continue;
                    if (wallPlace.getValue()) {
                        if (placeWallRange.getValue() > 0 && !canSeeBlock(blockPos) && getVecDistance(blockPos, mc.player) > placeWallRange.getValue())
                            continue;
                    } else if (!canSeeBlock(blockPos)) continue;
                    damage = targetDamage;
                    tempBlock = blockPos;
                    target = entity2;
                    if (renderDmg.getValue()) renderBlockDmg.put(tempBlock, targetDamage);
                }
                if (target != null) break;
            }
        }
        return new CrystalTarget(tempBlock, target, ignoredEntity);
    }

    private void ghostHand() {
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY) continue;
                if (stack.getItem() == Items.END_CRYSTAL) {
                    prevSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = i;
                    mc.playerController.updateController();
                }
            }
        }
    }

    private List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>(mc.world.loadedEntityList);
        entities = entities.stream()
                .filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? targetAnimal.getValue() : targetMob.getValue()))
                .filter(entity -> mc.player.getDistance(entity) < distance.getValue())
                .collect(Collectors.toList());
        if (targetPlayer.getValue()) entities.addAll(mc.world.playerEntities);
        entities.removeIf(entity -> entity == mc.player || FriendManager.isFriend(entity));
        entities.sort(Comparator.comparingDouble(entity -> entity.getDistance(mc.player)));
        return entities;
    }

    public int getColor() {
        float[] tick_color = {(System.currentTimeMillis() % 11520L) / 11520.0f * rainbowSpeed.getValue()};
        int color_rgb = Color.HSBtoRGB(tick_color[0], saturation.getValue(), brightness.getValue());
        return rainbow.getValue() ?
                new Color(color_rgb >> 16 & 0xFF, color_rgb >> 8 & 0xFF, color_rgb & 0xFF, transparency.getValue()).getRGB() :
                new Color(red.getValue(), green.getValue(), blue.getValue(), transparency.getValue()).getRGB();
    }


    @Override
    public void onRenderWorld(RenderEvent event) {
        int color = syncGUI.getValue() ?
                new Color(GUIManager.getRed(), GUIManager.getGreen(), GUIManager.getBlue(), transparency.getValue()).getRGB() :
                getColor();
        if (renderBlock != null) drawBlock(renderBlock, color);
    }

    @Override
    public void onEnable() {
        antiWeak = false;
        weakPreSlot = -1;
        lastEntityId.set(-1);
        shouldIgnoreEntity.set(false);
        repeatUnits.forEach(RepeatUnit::resume);
        renderBlockDmg.clear();
    }

    @Override
    public void onDisable() {
        repeatUnits.forEach(RepeatUnit::suspend);
        rotating = false;
        resetRotation();
        placeTimer.restart();
        breakTimer.restart();
        packetBreakTimer.restart();
        renderBlock = null;
        target = null;
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    @Override
    public String getModuleInfo() {
        return target != null ? target.getName() : "";
    }

    public static class CrystalTarget {
        public BlockPos blockPos;
        public Entity target;
        public boolean ignoredEntity;

        public CrystalTarget(BlockPos block, Entity target, boolean ignoredEntity) {
            this.blockPos = block;
            this.target = target;
            this.ignoredEntity = ignoredEntity;
        }
    }

    public class LocalTarget {

        final LinkedBlockingDeque<Pair<EntityEnderCrystal, Timer>> attackTargets = new LinkedBlockingDeque<>();
        final LinkedBlockingDeque<Pair<CrystalTarget, Timer>> placeTargets = new LinkedBlockingDeque<>();

        public synchronized void checkAll(int placeDelay, int attackDelay) {
            synchronized (attackTargets) {
                synchronized (placeTargets) {
                    attackTargets.removeIf(it -> it.b.passed(attackDelay));
                    placeTargets.removeIf(it -> it.b.passed(placeDelay));
                }
            }
        }

        public synchronized void putAttackTarget(EntityEnderCrystal attackTarget) {
            synchronized (attackTargets) {
                attackTargets.add(new Pair<>(attackTarget, new Timer().reset()));
            }
        }

        public synchronized EntityEnderCrystal getAttackTarget() {
            synchronized (attackTargets) {
                Pair<EntityEnderCrystal, Timer> pair = attackTargets.pollLast();
                return pair == null ? null : pair.a;
            }
        }

        public synchronized void putPlaceTarget(CrystalTarget placeTarget) {
            synchronized (placeTargets) {
                placeTargets.add(new Pair<>(placeTarget, new Timer().reset()));
            }
        }

        public synchronized CrystalTarget getPlaceTarget(boolean ignore) {
            synchronized (placeTargets) {
                while (true) {
                    Pair<CrystalTarget, Timer> pair = placeTargets.pollLast();
                    if (pair == null) break;
                    if (pair.a.ignoredEntity == ignore) return pair.a;
                }
                return calculator(shouldIgnoreEntity.get());
            }
        }

    }

}
