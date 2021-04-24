package club.deneb.client.features.modules.combat

import club.deneb.client.client.FriendManager
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.modules.misc.AutoTool.Companion.equipBestWeapon
import club.deneb.client.utils.EntityUtil
import club.deneb.client.utils.Timer
import club.deneb.client.utils.WaitCounter
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.util.EnumHand
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * Updated by B_312 on 04/24/2021
 */
@Module.Info(
        name = "Aura",
        category = Category.COMBAT,
        description = "Hits entities around you"
)
class Aura : Module() {

    private val delayMode = setting("Mode", "Tick", listOf("Tick","Ms"))

    private val tickDelay = setting("TickDelay",2,0,100).m(delayMode,"Tick")
    private val msDelay = setting("MsDelay",100,0,5000).m(delayMode,"Ms")
    private val whileEating = setting("While Eating", true)
    private val multiTarget = setting("Multi Targets", false)
    private val attackPlayers = setting("Players", true)
    private val attackMobs = setting("Mobs", false)
    private val passive = setting("Passive Mobs",false).v { attackMobs.value }
    private val neutral = setting("Neutral Mobs",false).v { attackMobs.value }
    private val hostile = setting("Hostile Mobs",false).v { attackMobs.value }
    private val hitRange = setting("Hit Range", 5.5,0.0,10.0)
    private val ignoreWalls = setting("Ignore Walls", true)
    private val autoTool = setting("Auto Weapon", true)
    private val prefer = setting("Prefer", "Sword", listOf("Sword","Axe","None"))
    private val disableOnDeath = setting("Disable On Death", false)

    private var tickCounter = WaitCounter()
    private val msTimer = Timer()

    override fun onEnable() {
        //We don't want to lag
        tickCounter.addTick(20)
    }
    override fun onDisable() {
        msTimer.reset()
        tickCounter.reset()
    }

    override fun onTick() {
        if(shouldPause()) return
        if(delayMode.value == "Tick"){
            if(tickCounter.passed(tickDelay.value)) {
                loop()
                tickCounter.reset()
            } else
                tickCounter.addTick()
        }
    }

    @SubscribeEvent
    fun onRenderTick(event:TickEvent.RenderTickEvent){
        if(shouldPause()) return
        if(delayMode.value == "Ms"){
            if(msTimer.passed(msDelay.value.toDouble())) {
                loop()
                msTimer.reset()
            }
        }
    }

    private fun shouldPause():Boolean{
        if (mc.player == null || mc.player.isDead) {
            if (disableOnDeath.value) disable()
            return true
        }

        if (!whileEating.value) {
            val shield = mc.player.heldItemOffhand.getItem() == Items.SHIELD && mc.player.activeHand == EnumHand.OFF_HAND
            if (mc.player.isHandActive && !shield) {
                return true
            }
        }

        return false
    }

    private fun loop() {

        for (target in mc.world.loadedEntityList) {
            if (!EntityUtil.isLiving(target)) continue
            if (target === mc.player) continue
            if (mc.player.getDistance(target) > hitRange.value) continue
            if ((target as EntityLivingBase).health <= 0) continue
            if (target.hurtTime != 0) continue
            if (!ignoreWalls.value && !mc.player.canEntityBeSeen(target) && !canEntityFeetBeSeen(target)) continue

            if (attackPlayers.value && target is EntityPlayer && !FriendManager.isFriend(target)) {
                if (autoTool.value) equipBestWeapon(prefer.value)
                attack(target)
                if (!multiTarget.value) return
            } else {
                if (EntityUtil.mobTypeSettings(target, attackMobs.value, passive.value, neutral.value, hostile.value)) {
                    if (autoTool.value) equipBestWeapon(prefer.value)
                    attack(target)
                    if (!multiTarget.value) return
                }
            }
        }
    }

    private fun attack(e: Entity) {
        mc.playerController.attackEntity(mc.player, e)
        mc.player.swingArm(EnumHand.MAIN_HAND)
    }

    private fun canEntityFeetBeSeen(entityIn: Entity): Boolean {
        return mc.world.rayTraceBlocks(Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null
    }

}