package club.deneb.client.utils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean mobTypeSettings(Entity e,  Boolean mobs, Boolean passive, Boolean neutral, Boolean hostile) {
        return mobs && ((passive && isPassiveMob(e)) || (neutral && isCurrentlyNeutral(e)) || (hostile && isMobAggressive(e)));
    }

    public static boolean isPassiveMob(Entity e) {
        return e instanceof EntityAgeable || e instanceof EntityAmbientCreature || e instanceof EntitySquid;
    }

    public static boolean isCurrentlyNeutral(Entity entity) {
        return (isNeutralMob(entity) && !isMobAggressive(entity));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static double getRelativeX(float yaw){
        return MathHelper.sin(-yaw * 0.017453292F);
    }

    public static double getRelativeZ(float yaw){
        return MathHelper.cos(yaw * 0.017453292F);
    }


    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return EntityUtil.getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
        return getInterpolatedPos(entity, ticks).subtract(Wrapper.mc.getRenderManager().renderPosX,Wrapper.mc.getRenderManager().renderPosY,Wrapper.mc.getRenderManager().renderPosZ);
    }


    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }

    public static boolean isFakeLocalPlayer(Entity entity) {
        return entity != null && entity.getEntityId() == -100 && Wrapper.getPlayer() != entity;
    }

    public static boolean isAboveWater(Entity entity) { return isAboveWater(entity, false); }
    public static boolean isAboveWater(Entity entity, boolean packet){
        if (entity == null) return false;

        double y = entity.posY - (packet ? 0.03 : (EntityUtil.isPlayer(entity) ? 0.2 : 0.5)); // increasing this seems to flag more in NCP but needs to be increased so the player lands on solid water

        for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) return true;
            }

        return false;
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new double[]{yaw,pitch};
    }

    public static boolean isMobAggressive(Entity entity) {
        if(entity instanceof EntityPigZombie) {
            // arms raised = aggressive, angry = either game or we have set the anger cooldown
            if(((EntityPigZombie) entity).isArmsRaised() || ((EntityPigZombie) entity).isAngry()) {
                return true;
            }
        } else if(entity instanceof EntityWolf) {
            return ((EntityWolf) entity).isAngry() &&
                    !Wrapper.getPlayer().equals(((EntityWolf) entity).getOwner());
        } else if(entity instanceof EntityEnderman) {
            return ((EntityEnderman) entity).isScreaming();
        }
        return isHostileMob(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity));
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie ||
                entity instanceof EntityWolf ||
                entity instanceof EntityEnderman;
    }

    public static boolean isPassive(Entity e) {
        if (e instanceof EntityWolf && ((EntityWolf)e).isAngry()) {
            return false;
        }
        if (e instanceof EntityAgeable || e instanceof EntityAmbientCreature || e instanceof EntitySquid) {
            return true;
        }
        return e instanceof EntityIronGolem && ((EntityIronGolem)e).getRevengeTarget() == null;
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }


    public static String getPlayerName(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    public static List<Entity> getEntityList(){
        return Wrapper.getWorld().getLoadedEntityList();
    }

    public static BlockPos getLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static boolean isPlayerInHole() {
        BlockPos blockPos = getLocalPlayerPosFloored();

        IBlockState blockState = mc.world.getBlockState(blockPos);

        if (blockState.getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR)
            return false;

        if (mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR)
            return false;

        final BlockPos[] touchingBlocks = new BlockPos[]
                { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() };

        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks)
        {
            final IBlockState touchingState = mc.world.getBlockState(touching);
            if ((touchingState.getBlock() != Blocks.AIR) && touchingState.isFullBlock())
                validHorizontalBlocks++;
        }

        return validHorizontalBlocks >= 4;
    }
}
