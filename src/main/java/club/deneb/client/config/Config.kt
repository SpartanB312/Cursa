package club.deneb.client.config

import club.deneb.client.Deneb
import club.deneb.client.features.AbstractModule
import club.deneb.client.value.*
import com.google.gson.JsonObject
import java.io.File

open class Config(val file:File) {
    open fun load() {}
    open fun save() {}

    fun trySet(mods: AbstractModule, jsonMod: JsonObject) {
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
}