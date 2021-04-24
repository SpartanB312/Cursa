package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.entity.EntityOtherPlayerMP
import com.mojang.authlib.GameProfile
import java.util.UUID

@Module.Info(name = "FakePlayer", category = Category.MISC)
class FakePlayer : Module() {
    private val health = setting("Health", 10, 0, 36)
    private val customMode = setting("CustomName", false)
    private val mode = setting(
        "Name", "B_312", listOf(
            "popbob",
            "jared2013",
            "bachi",
            "dot5",
            "FitMC",
            "Cyri"
        )
    ).r(customMode)

    override fun onEnable() {
        if (mc.player == null || mc.world == null) return
        val fakePlayer = EntityOtherPlayerMP(
            mc.world,
            GameProfile(
                UUID.fromString("60569353-f22b-42da-b84b-d706a65c5ddf"),
                if (customMode.value) customName else mode.value
            )
        )
        fakePlayer.copyLocationAndAnglesFrom(mc.player)
        for (potionEffect in mc.player.activePotionEffects) {
            fakePlayer.addPotionEffect(potionEffect)
        }
        fakePlayer.health = health.value.toFloat()
        fakePlayer.inventory.copyInventory(mc.player.inventory)
        fakePlayer.rotationYawHead = mc.player.rotationYawHead
        mc.world.addEntityToWorld(-100, fakePlayer)
    }

    override fun onDisable() {
        mc.world.removeEntityFromWorld(-100)
    }

    companion object {
        @JvmField
        var customName = "None"
    }
}