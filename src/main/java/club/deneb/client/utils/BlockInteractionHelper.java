package club.deneb.client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockInteractionHelper {
    public static final List<Block> blackList;
    public static final List<Block> shulkersList;
    public static final List<BlockPos> legPosList;
    public static final List<Block> invalid;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static AxisAlignedBB getBoundingFromPos(BlockPos pos){
        IBlockState iBlockState = Wrapper.mc.world.getBlockState(pos);
        Vec3d interp = MathUtil.interpolateEntity(Wrapper.mc.player, Wrapper.mc.getRenderPartialTicks());
        return iBlockState.getSelectedBoundingBox(Wrapper.mc.world, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z);
    }

    public enum ValidResult
    {
        NoEntityCollision,
        AlreadyBlockThere,
        NoNeighbors,
        Ok,
    }

    public static ValidResult valid(BlockPos pos)
    {
        // There are no entities to block placement,
        if (!mc.world.checkNoEntityCollision(new AxisAlignedBB(pos)))
            return ValidResult.NoEntityCollision;

        //if (mc.world.getBlockState(pos.down()).getBlock() == Blocks.WATER)
            //if (ModuleManager.Get().GetMod(LiquidInteractModule.class).isEnabled())
                //return ValidResult.Ok;

        if (!BlockInteractionHelper.checkForNeighbours(pos))
            return ValidResult.NoNeighbors;

        IBlockState l_State = mc.world.getBlockState(pos);

        if (l_State.getBlock() == Blocks.AIR)
        {
            final BlockPos[] l_Blocks =
                    { pos.north(), pos.south(), pos.east(), pos.west(), pos.up(), pos.down() };

            for (BlockPos l_Pos : l_Blocks)
            {
                IBlockState l_State2 = mc.world.getBlockState(l_Pos);

                if (l_State2.getBlock() == Blocks.AIR)
                    continue;

                for (final EnumFacing side : EnumFacing.values())
                {
                    final BlockPos neighbor = pos.offset(side);

                    boolean l_IsWater = mc.world.getBlockState(neighbor).getBlock() == Blocks.WATER;

                    if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false)
                            //|| (l_IsWater && ModuleManager.Get().GetMod(LiquidInteractModule.class).isEnabled())
                    )
                    {
                        return ValidResult.Ok;
                    }
                }
            }

            return ValidResult.NoNeighbors;
        }

        return ValidResult.AlreadyBlockThere;
    }

    public enum PlaceResult
    {
        NotReplaceable,
        Neighbors,
        CantPlace,
        Placed,
    }

    public static PlaceResult place(BlockPos pos, float p_Distance, boolean p_Rotate, boolean p_UseSlabRule)
    {
        return place(pos, p_Distance, p_Rotate, p_UseSlabRule, false);
    }

    public static PlaceResult place(BlockPos pos, float p_Distance, boolean p_Rotate, boolean p_UseSlabRule, boolean packetSwing)
    {
        IBlockState l_State = mc.world.getBlockState(pos);

        boolean l_Replaceable = l_State.getMaterial().isReplaceable();

        boolean l_IsSlabAtBlock = l_State.getBlock() instanceof BlockSlab;

        if (!l_Replaceable && !l_IsSlabAtBlock)
            return PlaceResult.NotReplaceable;
        if (!BlockInteractionHelper.checkForNeighbours(pos))
            return PlaceResult.Neighbors;

        if (!l_IsSlabAtBlock)
        {
            ValidResult l_Result = valid(pos);

            if (l_Result != ValidResult.Ok && !l_Replaceable)
                return PlaceResult.CantPlace;
        }

        if (p_UseSlabRule)
        {
            if (l_IsSlabAtBlock && !l_State.isFullCube())
                return PlaceResult.CantPlace;
        }

        final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);

        for (final EnumFacing side : EnumFacing.values())
        {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();

            boolean l_IsWater = mc.world.getBlockState(neighbor).getBlock() == Blocks.WATER;

            if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false)
                    //|| (l_IsWater && ModuleManager.Get().GetMod(LiquidInteractModule.class).isEnabled())
            )
            {
                final Vec3d hitVec = new Vec3d((Vec3i) neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.distanceTo(hitVec) <= p_Distance)
                {
                    final Block neighborPos = mc.world.getBlockState(neighbor).getBlock();

                    final boolean activated = neighborPos.onBlockActivated(mc.world, pos, mc.world.getBlockState(pos), mc.player, EnumHand.MAIN_HAND, side, 0, 0, 0);

                    if (BlockInteractionHelper.blackList.contains(neighborPos) || BlockInteractionHelper.shulkerList.contains(neighborPos) || activated)
                    {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    if (p_Rotate)
                    {
                        BlockInteractionHelper.faceVectorPacketInstant(hitVec);
                    }
                    EnumActionResult l_Result2 = mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);

                    if (l_Result2 != EnumActionResult.FAIL)
                    {
                        if (packetSwing)
                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        else
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        if (activated)
                        {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        return PlaceResult.Placed;
                    }
                }
            }
        }
        return PlaceResult.CantPlace;
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = getLegitRotations(vec);

        Wrapper.getPlayer().connection.sendPacket(new CPacketPlayer.Rotation(rotations[0],
                rotations[1], Wrapper.getPlayer().onGround));
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.getPlayer().posX,
                Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(),
                Wrapper.getPlayer().posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{
                Wrapper.getPlayer().rotationYaw
                        + MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw),
                Wrapper.getPlayer().rotationPitch + MathHelper
                        .wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch)};
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                continue;
            }
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
                return side;
            }
        }
        return null;
    }

    public static float getGroundPosY(Boolean checkLiquid){

        AxisAlignedBB boundingBox = mc.player.boundingBox;

        double yOffset = mc.player.posY - boundingBox.minY;

        double[] xArray = new double[2];
        xArray[0] = Math.floor(boundingBox.minX);
        xArray[1] = Math.floor(boundingBox.maxX);

        double[] zArray = new double[2];
        zArray[0] = Math.floor(boundingBox.minZ);
        zArray[1] = Math.floor(boundingBox.maxZ);

        while (!mc.world.collidesWithAnyBlock(boundingBox.offset(0.0, yOffset, 0.0))) {

            if (checkLiquid) {

                for (int x=0; x<=1 ; x++) {
                    for(int z=0 ; z<=0;z++) {

                        BlockPos blockPos = new BlockPos(xArray[x], (int) (mc.player.posY + yOffset), zArray[z]);

                        if (mc.world.getBlockState(blockPos).getMaterial().isLiquid()) return -1.0f;
                    }
                }
            }

            yOffset -= 0.05;

            if (mc.player.posY + yOffset < 0.0f) return -1.0f;

        }

        return (float)(boundingBox.offset(0.0, yOffset + 0.05, 0.0).minY);

    }

    public static final List<Block> shulkerList = Arrays.asList(
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.SILVER_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
    );

    public static boolean checkForNeighbours(BlockPos blockPos) {
        // check if we don't have a block adjacent to blockpos
        if (!hasNeighbour(blockPos)) {
            // find air adjacent to blockpos that does have a block adjacent to it, let's fill this first as to form a bridge between the player and the original blockpos. necessary if the player is going diagonal.
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = blockPos.offset(side);
                if (hasNeighbour(neighbour)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    public static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.offset(side);
            if (!Wrapper.getWorld().getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isShulker(Block block) {
        return shulkersList.contains(block);
    }

    public static boolean isMidAir(BlockPos pos) {
        return (!mc.world.getBlockState(pos.add(0, -1, 0)).getMaterial().isSolid() && !mc.world.getBlockState(pos.add(0, -1, 0)).getMaterial().isLiquid())
                && !mc.world.getBlockState(pos.add(0, 0, -1)).getMaterial().isSolid()
                && !mc.world.getBlockState(pos.add(1, 0, 0)).getMaterial().isSolid()
                && !mc.world.getBlockState(pos.add(0, 0, 1)).getMaterial().isSolid()
                && !mc.world.getBlockState(pos.add(-1, 0, 0)).getMaterial().isSolid();
    }

    public static boolean isTopHaveBlock(BlockPos pos) {
        return mc.world.getBlockState(pos.up()).getMaterial().isSolid();
    }


    public static boolean isTrap(BlockPos pos) {
        if (pos.getY() > 125d) {
            return false;
        }
        return mc.world.getBlockState(pos.add(0, 0, -1)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(1, 0, 0)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(0, 0, 1)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(-1, 0, 0)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(0, 1, -1)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(1, 1, 0)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(0, 1, 1)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(-1, 1, 0)).getMaterial().isSolid() && mc.world.getBlockState(pos.add(0, 3, 0)).getMaterial().isSolid();
    }


    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    private static void processRightClickBlock(BlockPos pos, EnumFacing side,
                                               Vec3d hitVec) {
        getPlayerController().processRightClickBlock(Wrapper.getPlayer(),
                mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }

    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    private static IBlockState getState(BlockPos pos) {
        return Wrapper.getWorld().getBlockState(pos);
    }

    public static boolean isObbyHole(final BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK
                | Minecraft.getMinecraft().world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK
                | Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK
                | Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK
                | Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK
                | Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN;
    }

    public static boolean isHole(BlockPos pos) {
        return isHole(pos, false);
    }

    public static boolean brokenHole(BlockPos pos) {
        boolean wtf1 = Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN;
        boolean wtf2 = Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR
                && Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN;
        boolean wtf3 = Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN;
        boolean wtf4 = Minecraft.getMinecraft().world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN
                && Minecraft.getMinecraft().world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR;
        return wtf1 || wtf2 || wtf3 || wtf4;
    }

    public static List<BlockPos> getBrokenHole(BlockPos pos){
        NonNullList<BlockPos> positions = NonNullList.create();
        BlockPos pos1 = pos.add(1, 0, 0);
        BlockPos pos2 = pos.add(0, 0, 1);
        BlockPos pos3 = pos.add(-1, 0, 0);
        BlockPos pos4 = pos.add(0, 0, -1);
        if(Minecraft.getMinecraft().world.getBlockState(pos1).getBlock() == Blocks.AIR) positions.add(new BlockPos(pos1));
        if(Minecraft.getMinecraft().world.getBlockState(pos2).getBlock() == Blocks.AIR) positions.add(new BlockPos(pos2));
        if(Minecraft.getMinecraft().world.getBlockState(pos3).getBlock() == Blocks.AIR) positions.add(new BlockPos(pos3));
        if(Minecraft.getMinecraft().world.getBlockState(pos4).getBlock() == Blocks.AIR) positions.add(new BlockPos(pos4));
        return positions;
    }


    public static boolean isHole(final BlockPos pos, final boolean holeheight) {
        if (pos.getY() > 125d) {
            return false;
        }
        final boolean isSolid = !mc.world.getBlockState(pos).getMaterial().blocksMovement()
                && !mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()
                && (!mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial().blocksMovement()
                || !holeheight) && mc.world.getBlockState(pos.add(0, -1, 0)).getMaterial().isSolid()
                && mc.world.getBlockState(pos.add(1, 0, 0)).getMaterial().isSolid()
                && mc.world.getBlockState(pos.add(0, 0, 1)).getMaterial().isSolid()
                && mc.world.getBlockState(pos.add(-1, 0, 0)).getMaterial().isSolid()
                && mc.world.getBlockState(pos.add(0, 0, -1)).getMaterial().isSolid();
        final boolean isBedrock = (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.BEDROCK)
                || mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.OBSIDIAN))
                && (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(Blocks.BEDROCK)
                || mc.world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(Blocks.OBSIDIAN))
                && (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock().equals(Blocks.BEDROCK)
                || mc.world.getBlockState(pos.add(0, 0, 1)).getBlock().equals(Blocks.OBSIDIAN))
                && (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock().equals(Blocks.BEDROCK)
                || mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock().equals(Blocks.OBSIDIAN))
                && (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock().equals(Blocks.BEDROCK)
                || mc.world.getBlockState(pos.add(0, 0, -1)).getBlock().equals(Blocks.OBSIDIAN));
        return isBedrock || isSolid;
    }

    public static List<BlockPos> getLegVec(BlockPos loc) {
        List<BlockPos> vec = new ArrayList<>();
        for (BlockPos pos : legPosList) {
            vec.add(loc.add(pos));
        }
        return vec;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private static class Offsets {
        private static final BlockPos NORTH1 = new BlockPos(0, 0, -1);
        private static final BlockPos NORTH2 = new BlockPos(0, 0, -2);
        private static final BlockPos EAST1 = new BlockPos(1, 0, 0);
        private static final BlockPos EAST2 = new BlockPos(2, 0, 0);
        private static final BlockPos SOUTH1 = new BlockPos(0, 0, 1);
        private static final BlockPos SOUTH2 = new BlockPos(0, 0, 2);
        private static final BlockPos WEST1 = new BlockPos(-1, 0, 0);
        private static final BlockPos WEST2 = new BlockPos(-2, 0, 0);
        private static final BlockPos UNDER1 = new BlockPos(0, -1, 0);
        private static final BlockPos UNDER2 = new BlockPos(0, -2, 0);

    }
    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR);
        shulkersList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        legPosList = Arrays.asList(Offsets.NORTH1, Offsets.NORTH2, Offsets.SOUTH1, Offsets.SOUTH2, Offsets.WEST1, Offsets.WEST2, Offsets.EAST1, Offsets.EAST2, Offsets.UNDER1, Offsets.UNDER2);
        invalid = Arrays.asList(Blocks.ANVIL, Blocks.AIR, Blocks.WATER, Blocks.FIRE, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.CHEST, Blocks.ENCHANTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.GRAVEL, Blocks.LADDER, Blocks.VINE, Blocks.BEACON, Blocks.JUKEBOX, Blocks.ACACIA_DOOR, Blocks.BIRCH_DOOR, Blocks.DARK_OAK_DOOR, Blocks.IRON_DOOR, Blocks.JUNGLE_DOOR, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.IRON_TRAPDOOR, Blocks.TRAPDOOR, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
    }
}
