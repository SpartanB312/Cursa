package club.deneb.client.features.modules.client

import club.deneb.client.Deneb
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.clazz.Button
import club.deneb.client.value.Value

@Module.Info(name = "IRC", category = Category.CLIENT, visible = false)
class IRC : Module() {

    val reconnect: Value<Button> = setting("Reconnect", Button().setBind { tryReconnect() }).des("Click to reconnect IRC")
    val enable: Value<Boolean> = setting("EnableIRC", true)

    private fun tryReconnect() {
        Deneb.getINSTANCE().ircManager.reconnect()
    }
}