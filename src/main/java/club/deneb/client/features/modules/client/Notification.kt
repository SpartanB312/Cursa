package club.deneb.client.features.modules.client

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.value.Value

@Module.Info(name = "Notification", category = Category.CLIENT, visible = false)
class Notification : Module() {

    init {
        chat = setting("Chat", true)
    }

    companion object{
        lateinit var chat:Value<Boolean>
    }

}