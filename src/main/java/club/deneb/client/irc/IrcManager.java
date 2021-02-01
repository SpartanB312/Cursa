package club.deneb.client.irc;

import club.deneb.client.features.modules.client.IRC;

public class IrcManager {

    public IrcClient client;

    public final String proxy = "irc.deneb.club";
    public final int port = 20730;

    public IrcManager(){
        client = new IrcClient(proxy,port);
        client.start();
    }

    public void reconnect(){
        if(client != null) client.stop();
        client = new IrcClient(proxy,port);
        client.start();
    }

    public static boolean isEnabled(){
        if(IRC.getINSTANCE()==null) return false;
        return IRC.getINSTANCE().enable.getValue();
    }

    public static boolean isChatSend(){
        if(IRC.getINSTANCE()==null) return false;
        return IRC.getINSTANCE().isEnabled();
    }

}
