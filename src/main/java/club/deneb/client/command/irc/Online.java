package club.deneb.client.command.irc;

import club.deneb.client.Deneb;
import club.deneb.client.command.Command;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.Timer;


@Command.Info(command = "ol", description = "Check online IRC User")
public class Online extends Command {

    Timer coolDownTimer = new Timer().reset();

    @Override
    public void onCall(String s, String[] args) {
        if (coolDownTimer.passed(5000)) coolDownTimer.reset();
        else {
            ChatUtil.printChatMessage("IRC cooling down,you can send msg every 5s.");
            return;
        }
        try {
            Deneb.getINSTANCE().getIrcManager().client.send("[GETONLINE]");
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "ol";
    }
}
