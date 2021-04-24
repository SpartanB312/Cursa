package club.deneb.client.irc

import club.deneb.client.Deneb
import club.deneb.client.utils.ChatUtil
import net.minecraft.client.Minecraft
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class IrcClient(host: String, port: Int) : Thread() {

    private var socket: Socket = Socket(host, port)

    private var input = DataInputStream(socket.getInputStream())
    private var output = DataOutputStream(socket.getOutputStream())

    override fun run() {
        try {
            send("[CONNECT]" + Minecraft.getMinecraft().getSession().username)
            //Declare that we are deneb user!
            send("[CLIENT]Deneb")
            while (!socket.isClosed) {
                val received = input.readUTF()

                //We ignore connection checking message.
                if (received == "[CHECK]") continue
                IrcChatUtil.printIRCMessage(received)
            }
        } catch (ignored: Exception) { }
    }

    fun sendChatMessage(msg: String) {
        send("[MSG][" + Minecraft.getMinecraft().getSession().username + "]" + msg)
    }

    fun send(msg: String) {
        try {
            output.writeUTF(msg)
        } catch (e: Exception) {
            ChatUtil.printChatMessage("Irc lost connection,reconnecting!")
            Deneb.getINSTANCE().ircManager.reconnect()
        }
    }

}