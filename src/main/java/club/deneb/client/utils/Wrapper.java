package club.deneb.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fontRenderer = mc.fontRenderer;
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    public static Minecraft getMinecraft(){
        return mc;
    }
    public static World getWorld() {
        return getMinecraft().world;
    }
    public static int getKey(String keyname){
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
}
