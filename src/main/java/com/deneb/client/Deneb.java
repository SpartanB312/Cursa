package com.deneb.client;

import com.deneb.client.client.ForgeEventProcessor;
import com.deneb.client.client.GuiManager;
import com.deneb.client.gui.GUIRender;
import com.deneb.client.gui.font.CFont;
import com.deneb.client.gui.font.CFontRenderer;
import com.deneb.client.gui.HUDRender;
import com.deneb.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mod(modid = Deneb.MODID, name = Deneb.MODNAME, version = Deneb.VERSION)
public class Deneb {

    public final static String VERSION = "beta 0.1";
    public final static String MODID = "deneb";
    public final static String MODNAME = "Deneb";

    public static final Logger log = LogManager.getLogger(MODNAME);

    private static Deneb INSTANCE;
    public static Deneb getINSTANCE(){
        return INSTANCE;
    }

    private ModuleManager moduleManager;
    private GuiManager guiManager;
    private CFontRenderer fontRenderer;
    private GUIRender guiRender;
    private HUDRender hudEditor;
    private ForgeEventProcessor eventProcessor;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        INSTANCE = this;
        Display.setTitle(MODNAME + " " + VERSION);
        setIcon();
        fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Comfortaa-Bold.ttf", 18f, Font.PLAIN), true, false);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        guiRender = new GUIRender();
        hudEditor = new HUDRender();
        MinecraftForge.EVENT_BUS.register(eventProcessor = new ForgeEventProcessor());
    }

    private void setIcon() {
        Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            try {
                InputStream inputstream = Deneb.class.getResourceAsStream("/assets/minecraft/textures/logo32.png");
                InputStream inputstream1 = Deneb.class.getResourceAsStream("/assets/minecraft/textures/logo16.png");
                if (inputstream != null && inputstream1 != null) {
                    Display.setIcon(new ByteBuffer[]{Minecraft.getMinecraft().readImageToBuffer(inputstream), Minecraft.getMinecraft().readImageToBuffer(inputstream1)});
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    public ForgeEventProcessor getEventProcessor(){ return eventProcessor; }

    public ModuleManager getModuleManager(){ return moduleManager; }

    public GuiManager getGuiManager(){ return guiManager; }

    public GUIRender getGuiRender(){ return guiRender; }

    public HUDRender getHudEditor(){ return hudEditor; }

    public CFontRenderer getFont(){ return fontRenderer; }

}
