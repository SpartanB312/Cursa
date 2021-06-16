package club.deneb.client.config

import club.deneb.client.Deneb
import club.deneb.client.config.configs.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object ConfigManager {

    val gsonPretty: Gson = GsonBuilder().setPrettyPrinting().create()
    val jsonParser = JsonParser()
    private const val configPath = "Deneb/config/"

    private val initializedConfig = mutableListOf<Config>()

    init {
        try {
            initializedConfig.add(ClientConfig(configPath + "Deneb_Client.json"))
            initializedConfig.add(FriendConfig(configPath + "Deneb_Friend.json"))
            initializedConfig.add(GUIConfig(configPath + "Deneb_GUI.json"))
            initializedConfig.add(HUDConfig(configPath + "Deneb_HUD.json"))
            initializedConfig.add(ModuleConfig(configPath + "Deneb_Module.json"))
            initializedConfig.forEach {
                if (!it.file.exists()) {
                    it.file.parentFile.mkdirs()
                    try {
                        it.file.createNewFile()
                    } catch (ignored: Exception) {
                        Deneb.log.error("Can't create config file!")
                    }
                    it.save()
                }
            }
        } catch (e: Exception) {
            Deneb.log.error("Config files aren't exist or are broken!")
        }
    }


    @JvmStatic
    fun saveAll() {
        initializedConfig.forEach {
            it.save()
        }
    }

    @JvmStatic
    fun loadAll() {
        initializedConfig.forEach {
            it.load()
        }
    }


}