package club.deneb.client.command.commands

import club.deneb.client.client.ConfigManager.loadAll
import club.deneb.client.client.ConfigManager.saveAll
import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "config", description = "Save or load config.")
class Config : Command() {
    override fun onCall(s: String, vararg args: String) {
        when (args[0].toLowerCase()) {
            "save" -> save()
            "load" -> load()
        }
    }

    override fun getSyntax(): String {
        return "config <save/load>"
    }

    private fun load() {
        loadAll()
        sendNoSpamMessage("Configuration reloaded!")
    }

    private fun save() {
        saveAll()
        sendNoSpamMessage("Configuration saved!")
    }
}