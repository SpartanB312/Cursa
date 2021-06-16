package club.deneb.client.config.configs

import club.deneb.client.Deneb
import club.deneb.client.client.FriendManager
import club.deneb.client.command.CommandManager
import club.deneb.client.config.Config
import club.deneb.client.config.ConfigManager
import club.deneb.client.features.modules.combat.AutoEz
import club.deneb.client.features.modules.misc.FakePlayer
import com.google.gson.JsonObject
import java.io.*

class FriendConfig(name: String) : Config(File(name)) {

    override fun load() {
        if (this.file.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(this.file))
                val friendJson = ConfigManager.jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                FriendManager.friends.clear()
                for ((name, value) in friendJson.entrySet()) {
                    if (name == null) continue
                    val json = value as JsonObject
                    var isFriend = false
                    try {
                        isFriend = json["isFriend"].asBoolean
                    } catch (e: Exception) {
                        Deneb.log.error("Can't set friend value for $name, unfriended!")
                    }
                    FriendManager.friends[name] = isFriend
                }
            } catch (e: IOException) {
                Deneb.log.error("Error while loading friends!")
                e.printStackTrace()
            }
        }
    }

    override fun save(){
        try {
            val father = JsonObject()
            FriendManager.friends.forEach { (name,isFriend) ->
                val stuff = JsonObject()
                stuff.addProperty("isFriend", isFriend)
                father.add(name, stuff)
            }
            val saveJSon = PrintWriter(FileWriter(this.file))
            saveJSon.println(ConfigManager.gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving friends!")
            e.printStackTrace()
        }
    }
}