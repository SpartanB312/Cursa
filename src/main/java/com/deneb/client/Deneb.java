package com.deneb.client;

import com.deneb.client.client.*;
import com.deneb.client.command.CommandManager;
import com.deneb.client.gui.GUIRender;
import com.deneb.client.gui.font.CFont;
import com.deneb.client.gui.font.CFontRenderer;
import com.deneb.client.gui.HUDRender;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.utils.LagCompensator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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

    public final static String VERSION = "0.1";
    public final static String MODID = "deneb";
    public final static String MODNAME = "Deneb";
    public final static String MODNAME2 = "\u30c7\u30cd\u30d6";
    public final static String CHAT_SUFFIX = " \u1d05\u1d07\u0274\u1d07\u0299";
    public final static String AUTHOR = "B_312";
    public final static String GITHUB = "https://github.com/SpartanB312/Deneb";

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
    private ConfigManager configManager;
    private FriendManager friendManager;
    private CommandManager commandManager;
    private NotificationManager notificationManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        INSTANCE = this;
        Display.setTitle(MODNAME + " " + MODNAME2 + " " + VERSION);
        LagCompensator.INSTANCE = new LagCompensator();
        setIcon();
        fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Comfortaa-Bold.ttf", 18f, Font.PLAIN), true, false);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){

        notificationManager = new NotificationManager();
        moduleManager = new ModuleManager();
        friendManager = new FriendManager();
        commandManager = new CommandManager();
        guiManager = new GuiManager();
        MinecraftForge.EVENT_BUS.register(eventProcessor = new ForgeEventProcessor());

        guiRender = new GUIRender();
        hudEditor = new HUDRender();

        configManager = new ConfigManager();
        ConfigManager.loadAll();

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadDiscordRPC();
    }

    public void loadDiscordRPC(){
        //TODO:
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

    public NotificationManager getNotificationManager(){ return notificationManager; }

    public ForgeEventProcessor getEventProcessor(){ return eventProcessor; }

    public ModuleManager getModuleManager(){ return moduleManager; }

    public GuiManager getGuiManager(){ return guiManager; }

    public GUIRender getGuiRender(){ return guiRender; }

    public HUDRender getHudEditor(){ return hudEditor; }

    public CFontRenderer getFont(){ return fontRenderer; }

    public ConfigManager getConfigManager(){ return configManager; }

    public FriendManager getFriendManager(){ return friendManager; }

    public CommandManager getCommandManager(){ return commandManager; }

}
