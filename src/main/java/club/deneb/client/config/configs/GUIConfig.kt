package club.deneb.client.config.configs

import club.deneb.client.Deneb
import club.deneb.client.config.Config
import club.deneb.client.config.ConfigManager
import club.deneb.client.gui.GUIRender
import club.deneb.client.gui.HUDRender
import com.google.gson.JsonObject
import java.io.*

class GUIConfig(name: String) : Config(File(name)) {

    override fun load() {
        if (this.file.exists()) {
            try {
                val loadJson = BufferedReader(FileReader(this.file))
                val guiJson = ConfigManager.jsonParser.parse(loadJson) as JsonObject
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

    override fun save(){
        try {
            val father = JsonObject()

            //Click GUI
            for (panel in GUIRender.panels) {
                val jsonGui = JsonObject()
                jsonGui.addProperty("X", panel.x)
                jsonGui.addProperty("Y", panel.y)
                jsonGui.addProperty("Extended", panel.extended)
                father.add(panel.category.categoryName, jsonGui)
            }

            //HUD Editor
            for (panel in HUDRender.panels) {
                val jsonGui = JsonObject()
                jsonGui.addProperty("X", panel.x)
                jsonGui.addProperty("Y", panel.y)
                jsonGui.addProperty("Extended", panel.extended)
                father.add(panel.category.categoryName, jsonGui)
            }
            val saveJSon = PrintWriter(FileWriter(this.file))
            saveJSon.println(ConfigManager.gsonPretty.toJson(father))
            saveJSon.close()
        } catch (e: Exception) {
            Deneb.log.error("Error while saving GUI config!")
            e.printStackTrace()
        }
    }
}