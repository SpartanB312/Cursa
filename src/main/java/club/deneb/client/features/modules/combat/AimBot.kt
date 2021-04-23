package club.deneb.client.features.modules.combat

import club.deneb.client.client.FriendManager
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.EntityUtil
import club.deneb.client.utils.MathsUtils
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBow
import net.minecraft.util.math.MathHelper

/**
 * This is a kotlin module example from KAMI BLUE
 */
@Module.Info(
    name = "AimBot",
    description = "Automatically aims at entities for you.",
    category = Category.COMBAT
)
class AimBot : Module() {

    private val range = setting("Range", 16, 4, 24)
    private val useBow = setting("Use Bow", true)
    private val ignoreWalls = setting("Ignore Walls",true)
    private val targetPlayers = setting("Target Players",true)
    private val targetFriends = setting("Friends",false).v { targetPlayers.value == true }
    private val targetSleeping = setting("Sleeping Players",false).v { targetPlayers.value == true }
    private val mobs = setting("Mobs", false)
    private val passive = setting("Passive Mobs",false).v { mobs.value }
    private val neutral = setting("Neutral Mobs",false).v { mobs.value }
    private val hostile = setting("Hostile Mobs",false).v { mobs.value }

    override fun onTick() {

        if (ModuleManager.getModule(Aura::class.java).isEnabled) {
            return
        }
        if (useBow.value) {
            var bowSlot = 0
            for (i in 0..9) {
                val potentialBow = mc.player.inventory.getStackInSlot(i)
                if (potentialBow.getItem() is ItemBow) {
                    bowSlot = mc.player.inventory.getSlotFor(potentialBow)
                }
            }
            mc.player.inventory.currentItem = bowSlot
            mc.playerController.syncCurrentPlayItem()
        }

        for (entity in mc.world.loadedEntityList) {
            if (entity is EntityLivingBase
                && (entity !is EntityPlayerSP && mc.player.getDistance(entity) <= range.value && entity.health > 0)
            ) {
                if (!ignoreWalls.value) {
                    if (!mc.player.canEntityBeSeen(entity)) {
                        return
                    }
                }
                if (EntityUtil.mobTypeSettings(
                        entity,
                        mobs.value,
                        passive.value,
                        neutral.value,
                        hostile.value
                    )
                ) faceEntity(entity)
                if (targetPlayers.value) {
                    if (entity.isPlayerSleeping && entity is EntityPlayer && targetSleeping.value) {
                        faceEntity(entity)
                    }
                    if (!targetFriends.value) {
                        for (friend in FriendManager.INSTANCE.friends) {
                            if (friend.name != entity.name) {
                                faceEntity(entity)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun faceEntity(entity: Entity) {
        val diffX = entity.posX - mc.player.posX
        val diffZ = entity.posZ - mc.player.posZ
        val diffY = mc.player.posY + mc.player.getEyeHeight().toDouble() - (entity.posY + entity.eyeHeight.toDouble())

        val xz = MathHelper.sqrt(diffX * diffX + diffZ * diffZ).toDouble()
        val yaw = MathsUtils.normalizeAngle(kotlin.math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0f).toFloat()
        val pitch = MathsUtils.normalizeAngle(-kotlin.math.atan2(diffY, xz) * 180.0 / Math.PI).toFloat()

        mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, -pitch)
    }
}