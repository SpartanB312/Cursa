package club.deneb.client.irc

import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.client.IRC

@Suppress("DEPRECATION")
class IrcManager {

    @JvmField
    var client: IrcClient
    private val proxy = "irc.deneb.club"
    private val port = 20730

    fun reconnect() {
        client.stop()
        client = IrcClient(proxy, port)
        client.start()
    }

    companion object {
        val isEnabled: Boolean
            get() = (ModuleManager.getModule(IRC::class.java) as IRC).enable.value
        val isChatSend: Boolean
            get() = ModuleManager.getModule(IRC::class.java).isEnabled
    }

    init {
        client = IrcClient(proxy, port)
        client.start()
    }
}