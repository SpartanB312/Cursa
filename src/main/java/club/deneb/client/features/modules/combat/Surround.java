package club.deneb.client.features.modules.combat;

import club.deneb.client.utils.EntityUtil;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.BlockInteractionHelper;
import club.deneb.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Module from KamiBlue , thank you!
 */
@Module.Info(name = "Surround",category = Category.COMBAT)
public class Surround extends Module {

    Value<Boolean> autoDisable = setting("Disable on place", true);
    Value<Boolean> spoofRotations = setting("Spoof Rotations", true);
    Value<Boolean> spoofHotBar = setting("Spoof HotBar", false);
    Value<Integer> blockPerTick = setting("Blocks per Tick",4,1,20);
    Value<String> autoCenter = setting("Auto Center","TP",listOf("TP","OFF"));
    Value<Boolean> placeAnimation = setting("Place Animation", false);


    private final Vec3d[] surroundTargets = new Vec3d[]{new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D)};

    private Vec3d playerPos;
    private BlockPos basePos;
    private int offsetStep = 0;
    private int playerHotBarSlot = -1;
    private int lastHotBarSlot = -1;


    @Override
    public void onTick() {
        if (mc.player != null) {
            if (offsetStep == 0) {
                basePos = (new BlockPos(mc.player.getPositionVector())).down();
                playerHotBarSlot = mc.player.inventory.currentItem;

                if (!spoofHotBar.getValue()) {
                    lastHotBarSlot = mc.player.inventory.currentItem;
                }
            }

            for (int i = 0; i < (int) Math.floor(blockPerTick.getValue()); ++i) {

                if (offsetStep >= surroundTargets.length) {
                    endLoop();
                    return;
                }

                Vec3d offset = surroundTargets[offsetStep];
                placeBlock(new BlockPos(basePos.add(offset.x, offset.y, offset.z)));
                ++offsetStep;
            }
        }
    }

    /* Autocenter */
    private void centerPlayer(double x, double y, double z) {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
        mc.player.setPosition(x, y, z);
    }
    double getDst(Vec3d vec) {
        return playerPos.distanceTo(vec);
    }
    /* End of Autocenter */

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        /* Autocenter */
        BlockPos centerPos = mc.player.getPosition();
        playerPos = mc.player.getPositionVector();
        double y = centerPos.getY();
        double x = centerPos.getX();
        double z = centerPos.getZ();

        final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
        final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
        final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
        final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);

        if (autoCenter.toggled("TP")) {
            if (getDst(plusPlus) < getDst(plusMinus) && getDst(plusPlus) < getDst(minusMinus) && getDst(plusPlus) < getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() + 0.5;
                centerPlayer(x, y, z);
            } if (getDst(plusMinus) < getDst(plusPlus) && getDst(plusMinus) < getDst(minusMinus) && getDst(plusMinus) < getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() - 0.5;
                centerPlayer(x, y, z);
            } if (getDst(minusMinus) < getDst(plusPlus) && getDst(minusMinus) < getDst(plusMinus) && getDst(minusMinus) < getDst(minusPlus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() - 0.5;
                centerPlayer(x, y, z);
            } if (getDst(minusPlus) < getDst(plusPlus) && getDst(minusPlus) < getDst(plusMinus) && getDst(minusPlus) < getDst(minusMinus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() + 0.5;
                centerPlayer(x, y, z);
            }
        }
        /* End of Autocenter*/

        playerHotBarSlot = mc.player.inventory.currentItem;
        lastHotBarSlot = -1;

    }

    public void onDisable() {
        if (mc.player != null) {


            if (lastHotBarSlot != playerHotBarSlot && playerHotBarSlot != -1) {
                if (spoofHotBar.getValue()) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(playerHotBarSlot));
                } else {
                    mc.player.inventory.currentItem = playerHotBarSlot;
                }
            }
            playerHotBarSlot = -1;
            lastHotBarSlot = -1;
        }
    }

    private void endLoop() {
        offsetStep = 0;

        if (lastHotBarSlot != playerHotBarSlot && playerHotBarSlot != -1) {


            if (spoofHotBar.getValue()) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(playerHotBarSlot));
            } else {
                mc.player.inventory.currentItem = playerHotBarSlot;
            }

            lastHotBarSlot = playerHotBarSlot;
        }
        if (autoDisable.getValue() && EntityUtil.isPlayerInHole()) {
            disable();
        }
    }

    private void placeBlock(BlockPos blockPos) {
        if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
        } else if (!BlockInteractionHelper.checkForNeighbours(blockPos) ) {
        } else {
            if (placeAnimation.getValue()) mc.player.connection.sendPacket(new CPacketAnimation(mc.player.getActiveHand()));
            placeBlockExecute(blockPos);
        }
    }

    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    public void placeBlockExecute(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
        EnumFacing[] var3 = EnumFacing.values();

        for (EnumFacing side : var3) {
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!canBeClicked(neighbor)) {
            } else {
                Vec3d hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                if (eyesPos.squareDistanceTo(hitVec) <= 18.0625D) {
                    if (spoofRotations.getValue()) {
                        faceVectorPacketInstant(hitVec);
                    }
                    boolean needSneak = false;
                    Block blockBelow = mc.world.getBlockState(neighbor).getBlock();
                    if (BlockInteractionHelper.blackList.contains(blockBelow) || BlockInteractionHelper.shulkerList.contains(blockBelow)) {
                        needSneak = true;
                    }
                    if (needSneak) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }

                    int obiSlot = findObiInHotbar();
                    if (obiSlot == -1) {
                        disable();
                        return;
                    }

                    if (lastHotBarSlot != obiSlot) {
                        if (spoofHotBar.getValue()) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(obiSlot));
                        } else {
                            mc.player.inventory.currentItem = obiSlot;
                        }
                        lastHotBarSlot = obiSlot;
                    }

                    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    if (needSneak) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    return;
                }

            }
        }
    }

    private static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    private static IBlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    private static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = getLegitRotations(vec);
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
    }

    private static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
    }

}
