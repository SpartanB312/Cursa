package club.deneb.client.features.modules.render

import club.deneb.client.client.FriendManager.isFriend
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.ChatUtil
import club.deneb.client.value.Value
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam

@Module.Info(
    name = "ExtraTab",
    description = "Expands the player tab menu",
    category = Category.RENDER
)
class ExtraTab : Module() {

    init {
        tabSize = setting("Players", 80, 1, 200)
    }

    companion object {
        lateinit var tabSize: Value<Int>
        @JvmStatic
        fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo): String {
            val dName = if (networkPlayerInfoIn.displayName != null) networkPlayerInfoIn.displayName!!
                .formattedText else ScorePlayerTeam.formatPlayerName(
                networkPlayerInfoIn.playerTeam,
                networkPlayerInfoIn.gameProfile.name
            )
            return if (isFriend(dName)) String.format("%sa%s", ChatUtil.SECTIONSIGN, dName) else dName
        }
    }

}