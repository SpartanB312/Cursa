package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.client.NullModule
import club.deneb.client.features.modules.client.NullHUD
import club.deneb.client.value.*
import java.lang.Exception

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "setting", description = "Set settings for module or HUD.")
class Settings : Command() {
    override fun onCall(s: String, vararg args: String) {
        try {
            val m = ModuleManager.getModuleByName(args[0])
            if (m is NullModule || m is NullHUD) {
                sendNoSpamErrorMessage("Couldn't find a module &b" + args[0] + "!")
                return
            }
            val settings = m.values
            for (value in settings) {
                if (value.name.equals(args[1], ignoreCase = true)) {
                    try {
                        if (value is BooleanValue) {
                            when {
                                args[2].equals("true", ignoreCase = true) -> {
                                    value.value = true
                                }
                                args[2].equals("false", ignoreCase = true) -> {
                                    value.value = false
                                }
                                else -> {
                                    sendNoSpamErrorMessage("Can not set value to " + args[2])
                                }
                            }
                        }
                        if (value is IntValue) {
                            val input = args[2].toInt()
                            if (input > value.max.toInt() || input < value.min.toInt()) {
                                sendNoSpamErrorMessage("Can not set value to $input")
                                return
                            }
                            value.value = input
                        }
                        if (value is DoubleValue) {
                            val input = args[2].toDouble()
                            if (input > value.max.toDouble() || input < value.min.toDouble()) {
                                sendNoSpamErrorMessage("Can not set value to $input")
                                return
                            }
                            value.value = input
                        }
                        if (value is FloatValue) {
                            val input = args[2].toFloat()
                            if (input > value.max.toFloat() || input < value.min.toFloat()) {
                                sendNoSpamErrorMessage("Can not set value to $input")
                                return
                            }
                            value.value = input
                        }
                        if (value is ModeValue<*>) {
                            value.setWithName(args[2])
                        }
                        sendNoSpamMessage("Set " + value.name + " to " + args[2])
                    } catch (e: Exception) {
                        sendNoSpamErrorMessage("Can not set value to " + args[2])
                    }
                } else {
                    sendNoSpamMessage("Can not find setting call " + args[1])
                }
            }
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "setting <module> <value name> <value>"
    }
}