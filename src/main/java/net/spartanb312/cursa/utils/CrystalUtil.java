package net.spartanb312.cursa.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorRenderManager;

public class CrystalUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.x - x;
        double yl = a.y - y;
        double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    public static boolean isReplaceable(Block block) {
        return block == Blocks.FIRE
                || block == Blocks.DOUBLE_PLANT
                || block == Blocks.VINE;
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
        for (EnumFacing enumFacing : EnumFacing.VALUES) {
            final Vec3d vec3d = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            final Vec3d vec3d2 = new Vec3d(blockPos.getX() + enumFacing.getDirectionVec().getX(), blockPos.getY() + enumFacing.getDirectionVec().getY(), blockPos.getZ() + enumFacing.getDirectionVec().getZ());
            final RayTraceResult rayTraceBlocks;
            if ((rayTraceBlocks = mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false)) != null
                    && rayTraceBlocks.typeOfHit.equals(RayTraceResult.Type.BLOCK) && rayTraceBlocks.getBlockPos().equals(blockPos)) {
                return enumFacing;
            }
        }
        if (blockPos.getY() > mc.player.posY + mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }

    public static boolean isEating() {
        return mc.player != null && (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood || mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) && mc.player.isHandActive();
    }

    @SuppressWarnings("ALL")
    public static boolean canSeeBlock(BlockPos p_Pos) {
        return mc.player == null || mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(p_Pos.getX(), p_Pos.getY(), p_Pos.getZ()), false, true, false) != null;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static double getVecDistance(BlockPos a, double posX, double posY, double posZ) {
        double x1 = a.getX() - posX;
        double y1 = a.getY() - posY;
        double z1 = a.getZ() - posZ;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

    public static double getVecDistance(BlockPos pos, Entity entity) {
        return getVecDistance(pos, entity.posX, entity.posY, entity.posZ);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (distance / 2.0f) / (2.0f + (2.0f - (float) 1));
        if (scaleDistance < 1f)
            scaleDistance = 1;
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.016666668f * 1.6f;
        GlStateManager.translate(x - ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosX(), y - ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosY(),
                z - ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosZ());
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Minecraft.getMinecraft().player.rotationPitch, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

}
