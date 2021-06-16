package club.deneb.client.config.configs

import club.deneb.client.Deneb
import club.deneb.client.command.CommandManager
import club.deneb.client.config.Config
import club.deneb.client.config.ConfigManager
import club.deneb.client.features.modules.combat.AutoEz
import club.deneb.client.features.modules.misc.FakePlayer
import com.google.gson.JsonObject
import java.io.*

class ClientConfig(name: String) : Config(File(name)) {

    private fun trySetClient(json: JsonObject) {
        try {
            CommandManager.INSTANCE.cmdPrefix = json["CommandPrefix"].asString
            FakePlayer.customName = json["FakePlayerName"].asString
            Deneb.CHAT_SUFFIX = json["Suffix"].asString
            AutoEz.ezMsg = json["AutoEz"].asString
        } catch (e: Exception) {
            Deneb.log.error("Error while setting client!")
        }
    }

    override fun load() {
        if (this.file.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(this.file))
                val guiJason = ConfigManager.jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                for ((key, value) in guiJason.entrySet()) {
                    if (key == "Client") {
                        val json = value as JsonObject
                        trySetClient(json)
                    }
                }
            } catch (e: IOException) {
                Deneb.log.error("Error while loading client stuff!")
                e.printStackTrace()
            }
        }
    }


    override fun save(){
        try {
            val father = JsonObject()
            val stuff = JsonObject()
            stuff.addProperty("AutoEz", AutoEz.ezMsg)
            stuff.addProperty("CommandPrefix", CommandManager.INSTANCE.cmdPrefix)
            stuff.addProperty("Suffix", Deneb.CHAT_SUFFIX)
            stuff.addProperty("FakePlayerName", FakePlayer.customName)
            father.add("Client", stuff)
            val saveJSon = PrintWriter(FileWriter(this.file))
            saveJSon.println(ConfigManager.gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving client stuff!")
            e.printStackTrace()
        }
    }
}