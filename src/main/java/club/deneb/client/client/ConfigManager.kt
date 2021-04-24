package club.deneb.client.client

import club.deneb.client.Deneb
import com.google.gson.JsonObject
import club.deneb.client.features.AbstractModule
import club.deneb.client.features.ModuleManager
import club.deneb.client.gui.GUIRender
import club.deneb.client.gui.HUDRender
import club.deneb.client.features.modules.combat.AutoEz
import club.deneb.client.command.CommandManager
import club.deneb.client.features.modules.client.NullModule
import club.deneb.client.features.modules.client.NullHUD
import club.deneb.client.features.modules.misc.FakePlayer
import club.deneb.client.value.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.io.*
import java.lang.Exception
import java.util.ArrayList

/**
 * Author B_312 on 01/21/2021
 * Updated by B_312 on 04/24/2021
 */
object ConfigManager {

    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    private val jsonParser = JsonParser()
    private const val configPath = "Deneb/config/"

    private var clientFile = File(configPath + "Deneb_Client.json")
    private var friendFile = File(configPath + "Deneb_Friend.json")
    private var guiFile = File(configPath + "Deneb_GUI.json")
    private var hudFile = File(configPath + "Deneb_HUD.json")
    private var moduleFile = File(configPath + "Deneb_Module.json")
    private val initializedConfig: MutableList<File> = ArrayList()

    init {
        try {
            initializedConfig.add(clientFile)
            initializedConfig.add(friendFile)
            initializedConfig.add(guiFile)
            initializedConfig.add(hudFile)
            initializedConfig.add(moduleFile)
            initializedConfig.forEach {
                if (!it.exists()) {
                    it.parentFile.mkdirs()
                    try {
                        it.createNewFile()
                    } catch (ignored: Exception) {
                        Deneb.log.error("Can't create config file!")
                    }
                }
            }
        } catch (e: Exception) {
            Deneb.log.error("Config files aren't exist or are broken!")
        }
    }

    private fun saveModule() {
        try {
            val father = JsonObject()
            for (module in ModuleManager.getModules()) {
                val jsonModule = JsonObject()
                jsonModule.addProperty("Enable", module.toggled)
                jsonModule.addProperty("Bind", module.bind)
                if (module.values.isNotEmpty()) {
                    for (value in module.values) {
                        if (value is BooleanValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is IntValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is FloatValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is DoubleValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is ModeValue<*>) {
                            val save = value.value.toString()
                            jsonModule.addProperty(value.getName(), save)
                        }
                    }
                }
                module.onConfigSave()
                father.add(module.getName(), jsonModule)
            }
            val saveJSon = PrintWriter(FileWriter(moduleFile))
            saveJSon.println(gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving module config!")
            e.printStackTrace()
        }
    }

    private fun saveHUD() {
        try {
            val father = JsonObject()
            for (module in ModuleManager.getHUDModules()) {
                val jsonModule = JsonObject()
                jsonModule.addProperty("Enable", module.toggled)
                jsonModule.addProperty("HUDPosX", module.x)
                jsonModule.addProperty("HUDPosY", module.y)
                jsonModule.addProperty("Bind", module.bind)
                if (module.values.isNotEmpty()) {
                    for (value in module.values) {
                        if (value is BooleanValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is IntValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is FloatValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is DoubleValue) {
                            val valueT = value.value
                            jsonModule.addProperty(value.getName(), valueT)
                        }
                        if (value is ModeValue<*>) {
                            val save = value.value.toString()
                            jsonModule.addProperty(value.getName(), save)
                        }
                    }
                }
                module.onConfigSave()
                father.add(module.getName(), jsonModule)
            }
            val saveJSon = PrintWriter(FileWriter(hudFile))
            saveJSon.println(gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving HUD config!")
            e.printStackTrace()
        }
    }

    private fun saveGUI() {
        try {
            val father = JsonObject()

            //Click GUI
            for (panel in GUIRender.getINSTANCE().panels) {
                val jsonGui = JsonObject()
                jsonGui.addProperty("X", panel.x)
                jsonGui.addProperty("Y", panel.y)
                jsonGui.addProperty("Extended", panel.extended)
                father.add(panel.category.getName(), jsonGui)
            }

            //HUD Editor
            for (panel in HUDRender.getINSTANCE().panels) {
                val jsonGui = JsonObject()
                jsonGui.addProperty("X", panel.x)
                jsonGui.addProperty("Y", panel.y)
                jsonGui.addProperty("Extended", panel.extended)
                father.add(panel.category.getName(), jsonGui)
            }
            val saveJSon = PrintWriter(FileWriter(guiFile))
            saveJSon.println(gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving GUI config!")
            e.printStackTrace()
        }
    }

    private fun saveClient() {
        try {
            val father = JsonObject()
            val stuff = JsonObject()
            stuff.addProperty("AutoEz", AutoEz.ezMsg)
            stuff.addProperty("CommandPrefix", CommandManager.INSTANCE.cmdPrefix)
            stuff.addProperty("Suffix", Deneb.CHAT_SUFFIX)
            stuff.addProperty("FakePlayerName", FakePlayer.customName)
            father.add("Client", stuff)
            val saveJSon = PrintWriter(FileWriter(clientFile))
            saveJSon.println(gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving client stuff!")
            e.printStackTrace()
        }
    }

    private fun saveFriend() {
        try {
            val father = JsonObject()
            FriendManager.friends.forEach { (name,isFriend) ->
                val stuff = JsonObject()
                stuff.addProperty("isFriend", isFriend)
                father.add(name, stuff)
            }
            val saveJSon = PrintWriter(FileWriter(friendFile))
            saveJSon.println(gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving friends!")
            e.printStackTrace()
        }
    }

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

    private fun loadClient() {
        if (clientFile.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(clientFile))
                val guiJason = jsonParser.parse(loadJson) as JsonObject
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

    private fun loadFriend() {
        if (friendFile.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(friendFile))
                val friendJson = jsonParser.parse(loadJson) as JsonObject
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

    private fun loadGUI() {
        if (guiFile.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(guiFile))
                val guiJson = jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                for ((key, value) in guiJson.entrySet()) {
                    var panel = GUIRender.getPanelByName(key)
                    if (panel == null) panel = HUDRender.getPanelByName(key)
                    if (panel != null) {
                        val jsonGui = value as JsonObject
                        panel.x = jsonGui["X"].asInt
                        panel.y = jsonGui["Y"].asInt
                        panel.extended = jsonGui["Extended"].asBoolean
                    }
                }
            } catch (e: IOException) {
                Deneb.log.error("Error while loading GUI config!")
                e.printStackTrace()
            }
        }
    }

    private fun loadModule() {
        if (moduleFile.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(moduleFile))
                val moduleJason = jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                for ((key, value) in moduleJason.entrySet()) {
                    val module = ModuleManager.getModuleByName(key)
                    if (module !is NullModule) {
                        val jsonMod = value as JsonObject
                        val enabled = jsonMod["Enable"].asBoolean
                        if (module.isEnabled && !enabled) module.disable()
                        if (module.isDisabled && enabled) module.enable()
                        if (module.values.isNotEmpty()) {
                            trySet(module, jsonMod)
                        }
                        module.onConfigLoad()
                        module.bind = jsonMod["Bind"].asInt
                    }
                }
            } catch (e: IOException) {
                Deneb.log.info("Error while loading module config")
                e.printStackTrace()
            }
        }
    }

    private fun loadHUD() {
        if (hudFile.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(hudFile))
                val moduleJason = jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                for ((key, value) in moduleJason.entrySet()) {
                    val module = ModuleManager.getHUDByName(key)
                    if (module !is NullHUD) {
                        val jsonMod = value as JsonObject
                        val enabled = jsonMod["Enable"].asBoolean
                        if (module.isEnabled && !enabled) module.disable()
                        if (module.isDisabled && enabled) module.enable()
                        module.x = jsonMod["HUDPosX"].asInt
                        module.y = jsonMod["HUDPosY"].asInt
                        if (module.values.isNotEmpty()) {
                            trySet(module, jsonMod)
                        }
                        module.onConfigLoad()
                        module.bind = jsonMod["Bind"].asInt
                    }
                }
            } catch (e: IOException) {
                Deneb.log.info("Error while loading module config")
                e.printStackTrace()
            }
        }
    }

    private fun trySet(mods: AbstractModule, jsonMod: JsonObject) {
        try {
            for (value in mods.values) {
                tryValue(mods.name, value, jsonMod)
            }
        } catch (e: Exception) {
            Deneb.log.error("Cant set value for " + (if (mods.isHUD) "HUD " else " module ") + mods.getName() + "!")
        }
    }

    private fun tryValue(name: String, value: Value<*>, jsonMod: JsonObject) {
        try {
            if (value is BooleanValue) {
                value.value = jsonMod[value.getName()].asBoolean
            }
            if (value is DoubleValue) {
                value.value = jsonMod[value.getName()].asDouble
            }
            if (value is IntValue) {
                value.value = jsonMod[value.getName()].asInt
            }
            if (value is FloatValue) {
                value.value = jsonMod[value.getName()].asFloat
            }
            if (value is ModeValue<*>) {
                val modeName = jsonMod[value.getName()].asString
                value.setWithName(modeName)
            }
        } catch (e: Exception) {
            Deneb.log.error("Cant set value for " + name + ",loaded default!Value name:" + value.name)
        }
    }

    @JvmStatic
    fun saveAll() {
        saveClient()
        saveFriend()
        saveGUI()
        saveHUD()
        saveModule()
    }

    @JvmStatic
    fun loadAll() {
        loadClient()
        loadFriend()
        loadGUI()
        loadHUD()
        loadModule()
    }


}