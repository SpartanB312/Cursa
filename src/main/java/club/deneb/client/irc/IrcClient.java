package club.deneb.client.irc;

import club.deneb.client.Deneb;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.Wrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class IrcClient extends Thread{

    public Socket socket = null;

    private DataInputStream in;
    private DataOutputStream out;


    public IrcClient(String host, int port) {
        try {
            try {
                socket = new Socket(host, port);
                in = null;
                out = null;
            } catch (ConnectException ignored){
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public void run() {

        super.run();
        try {
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());

            send("[CONNECT]" + Wrapper.mc.getSession().getUsername());
            //Declare that we are deneb user!
            send("[CLIENT]Deneb");

            while (!socket.isClosed()) {
                String received = in.readUTF();

                //We ignore connection checking message.
                if(received.equals("[CHECK]")) continue;
                IrcChatUtil.printIRCMessage(received);
            }

        } catch (Exception ignored){ }
    }

    public void sendChatMessage(String msg) {
        send("[MSG]["+Wrapper.mc.getSession().getUsername() + "]"+ msg);
    }

    public void send(String msg){
        try {
            out.writeUTF(msg);
        } catch (Exception e) {
            ChatUtil.printChatMessage("Irc lost connection,reconnecting!");
            Deneb.getINSTANCE().getIrcManager().reconnect();
        }
    }
}
