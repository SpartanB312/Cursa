package club.deneb.client.features.modules.render

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.init.MobEffects
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb
import net.minecraft.network.play.server.SPacketSpawnMob
import net.minecraft.network.play.server.SPacketSpawnPainting
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(
    name = "AntiOverlay",
    description = "Clear the shit effect that you don't like",
    category = Category.RENDER
)
class AntiOverlay : Module() {
    private val fire = setting("Fire", true)
    private val blocks = setting("Blocks", true)
    private val water = setting("Water", true)
    private val blindness = setting("Blindness", true)
    private val nausea = setting("Nausea", false)
    private val xp = setting("XP", false)
    private val mob = setting("Mob", false)
    private val explosion = setting("Explosions", true)
    private val paint = setting("Paintings", false)

    override fun onTick() {
        if (blindness.value) {
            mc.player.removeActivePotionEffect(MobEffects.BLINDNESS)
        }
        if (nausea.value) {
            mc.player.removeActivePotionEffect(MobEffects.NAUSEA)
        }
    }

    @SubscribeEvent
    fun onBlockOverlayEvent(event: RenderBlockOverlayEvent) {
        if (this.isDisabled) {
            return
        }
        if (fire.value && event.overlayType == RenderBlockOverlayEvent.OverlayType.FIRE) event.isCanceled = true
        if (blocks.value && event.overlayType == RenderBlockOverlayEvent.OverlayType.BLOCK) event.isCanceled = true
        if (water.value && event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPackReceive(event: PacketEvent.Receive) {
        if (this.isDisabled) {
            return
        }
        val packet = event.packet
        if (packet is SPacketSpawnExperienceOrb && xp.value
            || packet is SPacketExplosion && explosion.value
            || packet is SPacketSpawnPainting && paint.value
            || packet is SPacketSpawnMob && mob.value
        ) {
            event.isCanceled = true
        }
    }
}