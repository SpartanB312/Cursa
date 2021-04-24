package club.deneb.client.utils

import club.deneb.client.features.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemFood
import net.minecraft.util.CombatRules
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Explosion
import kotlin.math.floor
import kotlin.math.sqrt

object CrystalUtil {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun calculateDamage(posX: Double, posY: Double, posZ: Double, entity: Entity, vec: Vec3d): Float {
        val doubleExplosionSize = 12.0f
        val distanceSize = getRange(vec, posX, posY, posZ) / doubleExplosionSize.toDouble()
        val vec3d = Vec3d(posX, posY, posZ)
        val blockDensity = entity.world.getBlockDensity(vec3d, entity.entityBoundingBox).toDouble()
        val v = (1.0 - distanceSize) * blockDensity
        val damage = ((v * v + v) / 2.0 * 7.0 * doubleExplosionSize.toDouble() + 1.0).toInt().toFloat()
        var finalValue = 1.0
        if (entity is EntityLivingBase) {
            finalValue = getBlastReduction(
                entity,
                getDamageMultiplied(damage),
                Explosion(Module.mc.world, null, posX, posY, posZ, 6f, false, true)
            ).toDouble()
        }
        return finalValue.toFloat()
    }

    fun calculateDamage(posX: Double, posY: Double, posZ: Double, entity: Entity): Float {
        val offset = Vec3d(entity.posX, entity.posY, entity.posZ)
        return calculateDamage(posX, posY, posZ, entity, offset)
    }

    private fun getBlastReduction(entityIn: EntityLivingBase, damageIn: Float, explosion: Explosion): Float {
        var damage = damageIn
        if (entityIn is EntityPlayer) {
            val ds = DamageSource.causeExplosionDamage(explosion)
            damage = CombatRules.getDamageAfterAbsorb(
                damage,
                entityIn.totalArmorValue.toFloat(),
                entityIn.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).attributeValue
                    .toFloat()
            )
            val k = EnchantmentHelper.getEnchantmentModifierDamage(entityIn.armorInventoryList, ds)
            val f = MathHelper.clamp(k.toFloat(), 0.0f, 20.0f)
            damage *= 1.0f - f / 25.0f
            if (entityIn.isPotionActive(MobEffects.RESISTANCE)) damage -= damage / 5
            damage = damage.coerceAtLeast(0.0f)
            return damage
        }
        damage = CombatRules.getDamageAfterAbsorb(
            damage,
            entityIn.totalArmorValue.toFloat(),
            entityIn.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).attributeValue
                .toFloat()
        )
        return damage
    }

    private fun getDamageMultiplied(damage: Float): Float {
        val diff = Module.mc.world.difficulty.id
        return damage * if (diff == 0) 0.0f else if (diff == 2) 1.0f else if (diff == 1) 0.5f else 1.5f
    }

    private fun getRange(a: Vec3d, x: Double, y: Double, z: Double): Double {
        val xl = a.x - x
        val yl = a.y - y
        val zl = a.z - z
        return sqrt(xl * xl + yl * yl + zl * zl)
    }

    fun enumFacing(blockPos: BlockPos): EnumFacing {
        val values: Array<EnumFacing>
        val length = EnumFacing.values().also { values = it }.size
        var i = 0
        while (i < length) {
            val enumFacing = values[i]
            val vec3d = Vec3d(Module.mc.player.posX, Module.mc.player.posY + Module.mc.player.getEyeHeight(), Module.mc.player.posZ)
            val vec3d2 = Vec3d(
                (blockPos.getX() + enumFacing.directionVec.getX()).toDouble(),
                (blockPos.getY() + enumFacing.directionVec.getY()).toDouble(),
                (blockPos.getZ() + enumFacing.directionVec.getZ()).toDouble()
            )
            val rayTraceBlocks: RayTraceResult
            if (Module.mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false).also {
                    rayTraceBlocks = it!!
                } != null && rayTraceBlocks.typeOfHit == RayTraceResult.Type.BLOCK && rayTraceBlocks.blockPos == blockPos) {
                return enumFacing
            }
            i++
        }
        return if (blockPos.getY() > Module.mc.player.posY + Module.mc.player.getEyeHeight()) {
            EnumFacing.DOWN
        } else EnumFacing.UP
    }

    val isEating: Boolean
        get() = Module.mc.player != null && (Module.mc.player.heldItemMainhand.getItem() is ItemFood || Module.mc.player.heldItemOffhand.getItem() is ItemFood) && Module.mc.player.isHandActive

    fun canSeeBlock(p_Pos: BlockPos): Boolean {
        return if (Module.mc.player == null) true else Module.mc.world.rayTraceBlocks(
            Vec3d(Module.mc.player.posX, Module.mc.player.posY + Module.mc.player.getEyeHeight().toDouble(), Module.mc.player.posZ),
            Vec3d(p_Pos.getX().toDouble(), p_Pos.getY().toDouble(), p_Pos.getZ().toDouble()),
            false,
            true,
            false
        ) != null
    }

    fun getVecDistance(a: BlockPos, posX: Double, posY: Double, posZ: Double): Double {
        val x1 = a.x - posX
        val y1 = a.y - posY
        val z1 = a.z - posZ
        return sqrt(x1 * x1 + y1 * y1 + z1 * z1)
    }

    fun glBillboardDistanceScaled(x: Float, y: Float, z: Float, player: EntityPlayer) {
        glBillboard(x, y, z)
        val distance = player.getDistance(x.toDouble(), y.toDouble(), z.toDouble()).toInt()
        var scaleDistance = distance / 2.0f / (2.0f + (2.0f - 1.toFloat()))
        if (scaleDistance < 1f) scaleDistance = 1f
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance)
    }

    fun glBillboard(x: Float, y: Float, z: Float) {
        val scale = 0.016666668f * 1.6f
        GlStateManager.translate(
            x - Minecraft.getMinecraft().getRenderManager().renderPosX,
            y - Minecraft.getMinecraft().getRenderManager().renderPosY,
            z - Minecraft.getMinecraft().getRenderManager().renderPosZ
        )
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(
            Minecraft.getMinecraft().player.rotationPitch,
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) -1.0f else 1.0f,
            0.0f,
            0.0f
        )
        GlStateManager.scale(-scale, -scale, scale)
    }

    val playerPos: BlockPos
        get() = BlockPos(floor(Module.mc.player.posX), floor(Module.mc.player.posY), floor(Module.mc.player.posZ))

    fun canFacePlace(target: EntityLivingBase, blast: Double): Boolean {
        val healthTarget = target.health + target.absorptionAmount
        return healthTarget <= blast
    }

}