package com.deneb.client.client;

import com.deneb.client.Deneb;
import com.deneb.client.command.CommandManager;
import com.deneb.client.gui.GUIRender;
import com.deneb.client.gui.HUDRender;
import com.deneb.client.gui.Panel;
import com.deneb.client.features.HUDModule;
import com.deneb.client.features.IModule;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.features.modules.client.NullHUD;
import com.deneb.client.features.modules.client.NullModule;
import com.deneb.client.utils.clazz.Friend;
import com.deneb.client.value.*;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static ConfigManager INSTANCE;

    public ConfigManager(){
        INSTANCE = this;
        init();
    }

    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private static final String CONFIG_PATH = "Deneb/config/";

    private static final String CLIENT_CONFIG = CONFIG_PATH + "Deneb_Client.json";
    private static final String FRIEND_CONFIG = CONFIG_PATH + "Deneb_Friend.json";
    private static final String GUI_CONFIG = CONFIG_PATH + "Deneb_GUI.json";
    private static final String HUD_CONFIG = CONFIG_PATH + "Deneb_HUD.json";
    private static final String MODULE_CONFIG = CONFIG_PATH + "Deneb_Module.json";

    private File CLIENT_FILE;
    private File FRIEND_FILE;
    private File GUI_FILE;
    private File HUD_FILE;
    private File MODULE_FILE;
    private final List<File> initializedConfig = new ArrayList<>();

    private void init(){
        if(!tryLoad()) deleteFiles();
    }

    private boolean tryLoad(){
        try {
            initializedConfig.add(CLIENT_FILE = new File(MODULE_CONFIG));
            initializedConfig.add(FRIEND_FILE = new File(FRIEND_CONFIG));
            initializedConfig.add(GUI_FILE = new File(GUI_CONFIG));
            initializedConfig.add(HUD_FILE = new File(HUD_CONFIG));
            initializedConfig.add(MODULE_FILE = new File(MODULE_CONFIG));
        } catch (Exception e){
            Deneb.log.error("Config files aren't exist or are broken!");
            return false;
        }
        return true;
    }

    public void deleteFiles(){
        try{
            initializedConfig.forEach(File::delete);
            Deneb.log.info("All config files deleted successfully!\n");
        } catch (Exception e){
            Deneb.log.error("Error while deleting config files!");
            e.printStackTrace();
        }
    }

    public static void saveAll(){
        INSTANCE.saveClient();
        INSTANCE.saveFriend();
        INSTANCE.saveGUI();
        INSTANCE.saveHUD();
        INSTANCE.saveModule();
    }

    public static void loadAll(){
        INSTANCE.loadClient();
        INSTANCE.loadFriend();
        INSTANCE.loadGUI();
        INSTANCE.loadHUD();
        INSTANCE.loadModule();
    }

    private void saveModule(){
        try{
            if(!MODULE_FILE.exists()){
                MODULE_FILE.getParentFile().mkdirs();
                try{
                    MODULE_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();
            for (IModule module : ModuleManager.getModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("Bind", module.getBind());
                if (!module.getValues().isEmpty()) {
                    for (Value value : module.getValues()) {
                        if (value instanceof BValue) {
                            jsonModule.addProperty(value.getName(), (boolean)value.getValue());
                        }
                        if (value instanceof IValue) {
                            jsonModule.addProperty(value.getName(), (int)value.getValue());
                        }
                        if (value instanceof FValue) {
                            jsonModule.addProperty(value.getName(), (float)value.getValue());
                        }
                        if (value instanceof DValue) {
                            jsonModule.addProperty(value.getName(), (double)value.getValue());
                        }
                        if (value instanceof MValue) {
                            MValue modeValue = (MValue) value;
                            for (MValue.Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(modeValue.getName()+"-"+mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                father.add(module.getName(), jsonModule);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(MODULE_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            Deneb.log.error("Error while saving module config!");
            e.printStackTrace();
        }
    }

    private void saveHUD(){
        try{
            if(!HUD_FILE.exists()){
                HUD_FILE.getParentFile().mkdirs();
                try{
                    HUD_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();
            for (IModule module : ModuleManager.getHUDModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("HUDPosX",module.x);
                jsonModule.addProperty("HUDPosY",module.y);
                jsonModule.addProperty("Bind", module.getBind());
                if (!module.getValues().isEmpty()) {
                    for (Value value : module.getValues()) {
                        if (value instanceof BValue) {
                            jsonModule.addProperty(value.getName(), (boolean)value.getValue());
                        }
                        if (value instanceof IValue) {
                            jsonModule.addProperty(value.getName(), (int)value.getValue());
                        }
                        if (value instanceof FValue) {
                            jsonModule.addProperty(value.getName(), (float)value.getValue());
                        }
                        if (value instanceof DValue) {
                            jsonModule.addProperty(value.getName(), (double)value.getValue());
                        }
                        if (value instanceof MValue) {
                            MValue modeValue = (MValue) value;
                            for (MValue.Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(modeValue.getName()+"-"+mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                father.add(module.getName(), jsonModule);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(HUD_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            Deneb.log.error("Error while saving HUD config!");
            e.printStackTrace();
        }
    }

    private void saveGUI(){
        try{
            if(!GUI_FILE.exists()){
                GUI_FILE.getParentFile().mkdirs();
                try{
                    GUI_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();

            //Click GUI
            for (Panel panel : GUIRender.getINSTANCE().panels){
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.getName(), jsonGui);
            }

            //HUD Editor
            for (Panel panel : HUDRender.getINSTANCE().panels){
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.getName(), jsonGui);
            }

            PrintWriter saveJSon = new PrintWriter(new FileWriter(GUI_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            Deneb.log.error("Error while saving GUI config!");
            e.printStackTrace();
        }
    }

    private void saveClient(){
        try{
            if(!CLIENT_FILE.exists()){
                CLIENT_FILE.getParentFile().mkdirs();
                try{
                    CLIENT_FILE.createNewFile();
                } catch (Exception ignored){}
            }

            JsonObject father = new JsonObject();
            JsonObject stuff = new JsonObject();

            stuff.addProperty("AutoEz",1);
            stuff.addProperty("CommandPrefix",CommandManager.INSTANCE.cmdPrefix);
            stuff.addProperty("ChatSuffix",1);
            stuff.addProperty("FakePlayerName",1);
            stuff.addProperty("WaterMark",1);

            father.add("Client",stuff);

            PrintWriter saveJSon = new PrintWriter(new FileWriter(CLIENT_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            Deneb.log.error("Error while saving client stuff!");
            e.printStackTrace();
        }
    }

    private void saveFriend(){
        try{
            if(!FRIEND_FILE.exists()){
                FRIEND_FILE.getParentFile().mkdirs();
                try{
                    FRIEND_FILE.createNewFile();
                } catch (Exception ignored){}
            }

            JsonObject father = new JsonObject();

            for(Friend friend : FriendManager.getFriendList()){
                JsonObject stuff = new JsonObject();
                stuff.addProperty("isFriend",friend.isFriend);
                father.add(friend.name, stuff);
            }

            PrintWriter saveJSon = new PrintWriter(new FileWriter(FRIEND_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            Deneb.log.error("Error while saving friends!");
            e.printStackTrace();
        }
    }

    private void trySetClient(JsonObject json){
        try {
            CommandManager.INSTANCE.cmdPrefix = json.get("CommandPrefix").getAsString();
            //HUD.waterMark = json.get("WaterMark").getAsString();
            //FakePlayer.customName = json.get("FakePlayerName").getAsString();
            //CustomChat.CHAT_SUFFIX = json.get("ChatSuffix").getAsString();
            //AutoEz.ezMsg = json.get("AutoEz").getAsString();
        }catch (Exception e){
            Deneb.log.error("Error while setting client!");
        }
    }

    private void loadClient(){
        if (CLIENT_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(CLIENT_FILE));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    if(entry.getKey().equals("Client")) {
                        JsonObject json = (JsonObject) entry.getValue();
                        trySetClient(json);
                    }
                }
            } catch (IOException e) {
                Deneb.log.error("Error while loading client stuff!");
                e.printStackTrace();
            }
        }
    }

    private void loadFriend(){
        if (FRIEND_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(FRIEND_FILE));
                JsonObject friendJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                FriendManager.INSTANCE.friends.clear();
                for (Map.Entry<String, JsonElement> entry : friendJson.entrySet()) {
                    if(entry.getKey() == null) continue;
                    JsonObject json = (JsonObject) entry.getValue();
                    String name = entry.getKey();
                    boolean isFriend = false;
                    try{
                        isFriend = json.get("isFriend").getAsBoolean();
                    } catch (Exception e){
                        Deneb.log.error("Can't set friend value for "+ name + ", unfriended!");
                    }
                    FriendManager.INSTANCE.friends.add(new Friend(name,isFriend));
                }
            } catch (IOException e) {
                Deneb.log.error("Error while loading friends!");
                e.printStackTrace();
            }
        }
    }

    private void loadGUI(){
        if (GUI_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(GUI_FILE));
                JsonObject guiJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJson.entrySet()) {
                    Panel panel = GUIRender.getPanelByName(entry.getKey());
                    if(panel == null) panel = HUDRender.getPanelByName(entry.getKey());
                    if (panel != null) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }
                }
            } catch (IOException e) {
                Deneb.log.error("Error while loading GUI config!");
                e.printStackTrace();
            }
        }
    }

    private void loadModule(){
        if (MODULE_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(MODULE_FILE));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    IModule module = ModuleManager.getModuleByName(entry.getKey());
                    if (!(module instanceof NullModule)) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enable").getAsBoolean();
                        if(module.isEnabled() && !enabled) module.disable();
                        if(module.isDisabled() && enabled) module.enable();
                        if (!module.getValues().isEmpty()) {
                            trySet(module,jsonMod);
                        }
                        module.setBind(jsonMod.get("Bind").getAsInt());
                    }
                }
            } catch (IOException e) {
                Deneb.log.info("Error while loading module config");
                e.printStackTrace();
            }
        }
    }

    private void loadHUD(){
        if (HUD_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(HUD_FILE));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    HUDModule module = ModuleManager.getHUDByName(entry.getKey());
                    if (!(module instanceof NullHUD)) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enable").getAsBoolean();
                        if(module.isEnabled() && !enabled) module.disable();
                        if(module.isDisabled() && enabled) module.enable();
                        module.x = jsonMod.get("HUDPosX").getAsInt();
                        module.y = jsonMod.get("HUDPosY").getAsInt();
                        if (!module.getValues().isEmpty()) {
                            trySet(module,jsonMod);
                        }
                        module.setBind(jsonMod.get("Bind").getAsInt());
                    }
                }
            } catch (IOException e) {
                Deneb.log.info("Error while loading module config");
                e.printStackTrace();
            }
        }
    }

    private void trySet(IModule mods , JsonObject jsonMod){
        try {
            for (Value value : mods.getValues()) {
                tryValue(mods.name, value,jsonMod);
            }
        } catch (Exception e){
            Deneb.log.error("Cant set value for " + (mods.isHUD ? "HUD " : " module ") + mods.getName() + "!");
        }
    }

    private void tryValue(String name, Value value ,JsonObject jsonMod){
        try {
            if (value instanceof BValue) {
                boolean bValue = jsonMod.get(value.getName()).getAsBoolean();
                value.setValue(bValue);
            }
            if (value instanceof DValue) {
                double dValue = jsonMod.get(value.getName()).getAsDouble();
                value.setValue(dValue);
            }
            if (value instanceof IValue) {
                int iValue = jsonMod.get(value.getName()).getAsInt();
                value.setValue(iValue);
            }
            if (value instanceof FValue) {
                float fValue = jsonMod.get(value.getName()).getAsFloat();
                value.setValue(fValue);
            }
            if (value instanceof MValue) {
                MValue modeValue = (MValue) value;
                for (MValue.Mode mode : modeValue.getModes()) {
                    boolean mValue = jsonMod.get(modeValue.getName()+"-"+mode.getName()).getAsBoolean();
                    mode.setToggled(mValue);
                }
            }
        }catch (Exception e) {
            Deneb.log.error("Cant set value for "+ name + ",loaded default!Value name:"+value.getName());
        }
    }

}
