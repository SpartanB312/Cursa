package club.deneb.client.command.irc;

import club.deneb.client.Deneb;
import club.deneb.client.command.Command;
import club.deneb.client.irc.IrcChatUtil;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.Timer;

@Command.Info(command = "tell", description = "Whisper to your friend.")
public class Tell extends Command {

    Timer coolDownTimer = new Timer().reset();

    @Override
    public void onCall(String s, String[] args) {

        if (coolDownTimer.passed(5000)) coolDownTimer.reset();
        else {
            ChatUtil.printChatMessage("IRC cooling down,you can send msg every 5s.");
            return;
        }

        try {
            StringBuilder content = new StringBuilder();
            String name = args[0];
            for (int i = 1; i < args.length; i++) {
                content.append(" ").append(args[i]);
            }
            Deneb.getINSTANCE().getIrcManager().client.send("[TELL]" + name + "[MSG]" + content);
            IrcChatUtil.printRawIRCMessage(IrcChatUtil.SECTIONSIGN + "6[ Whisper to " + name + " ] " + IrcChatUtil.SECTIONSIGN + "r" + content);
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }


    @Override
    public String getSyntax() {
        return "tell <player> <message>";
    }
}
