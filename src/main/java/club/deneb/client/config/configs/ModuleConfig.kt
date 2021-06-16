package club.deneb.client.config.configs

import club.deneb.client.Deneb
import club.deneb.client.config.Config
import club.deneb.client.config.ConfigManager
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.client.NullModule
import club.deneb.client.value.*
import com.google.gson.JsonObject
import java.io.*

class ModuleConfig(name:String) : Config(File(name)) {

    override fun load() {
        if (this.file.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(this.file))
                val moduleJason = ConfigManager.jsonParser.parse(loadJson) as JsonObject
                loadJson.close()
                for ((key, value) in moduleJason.entrySet()) {
                    val module = ModuleManager.getModuleByName(key)
                    if (module !is NullModule && !module.isHUD) {
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
    override fun save() {
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
            val saveJSon = PrintWriter(FileWriter(this.file))
            saveJSon.println(ConfigManager.gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving module config!")
            e.printStackTrace()
        }
    }

}