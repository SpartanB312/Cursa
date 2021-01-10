package com.deneb.client.client;

import com.google.gson.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    /*

    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private static final String CONFIG_PATH = "HyperLethal_Two/config/";
    public static final String SETTING_CONFIG = CONFIG_PATH + "HyperLethal_TwoConfig.json";
    public static final String FRIEND_CONFIG = CONFIG_PATH + "HyperLethal_TwoFriend.json";
    public static final String GUI_CONFIG = CONFIG_PATH + "HyperLethal_TwoGUI.json";
    public static final String HUD_CONFIG = CONFIG_PATH + "HyperLethal_TwoHUD.json";

    private static File toFiles(String filePath){
        return new File(filePath);
    }
    public static boolean firstOpen = false;

    public static void saveGUI() {
        try {
            if (!toFiles(GUI_CONFIG).exists()) {
                toFiles(GUI_CONFIG).getParentFile().mkdirs();
                try {
                    toFiles(GUI_CONFIG).createNewFile();
                } catch (IOException e4) {
                    //  e4.printStackTrace();
                }
            }
            JsonObject json = new JsonObject();

            for (Panel panel : GUIRender.panels){
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                json.add(panel.title, jsonGui);
            }

            PrintWriter saveJson = new PrintWriter(new FileWriter(GUI_CONFIG));
            saveJson.println(gsonPretty.toJson(json));
            saveJson.close();
        } catch (IOException ignored){

        }
    }

    public static void saveHUD() {
        try {
            if (!toFiles(HUD_CONFIG).exists()) {
                toFiles(HUD_CONFIG).getParentFile().mkdirs();
                try {
                    toFiles(HUD_CONFIG).createNewFile();
                } catch (IOException e4) {
                    //   e4.printStackTrace();
                }
            }
            JsonObject json = new JsonObject();

            Panel hudPanel = HUDEditorRender.hudPanel;
            JsonObject jsonGui = new JsonObject();
            jsonGui.addProperty("X", hudPanel.x);
            jsonGui.addProperty("Y", hudPanel.y);
            jsonGui.addProperty("Extended", hudPanel.extended);
            json.add(hudPanel.title, jsonGui);

            PrintWriter saveJson = new PrintWriter(new FileWriter(HUD_CONFIG));
            saveJson.println(gsonPretty.toJson(json));
            saveJson.close();
        } catch (IOException ignored){

        }
    }

    public static void loadGUI() {
        if (toFiles(GUI_CONFIG).exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(GUI_CONFIG));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    Panel panel = GUIRender.getPanelByName(entry.getKey());
                    if (panel != null) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }

                }
            } catch (IOException ignored) {

            }
        }
    }

    public static void loadHUD() {
        if (toFiles(HUD_CONFIG).exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(HUD_CONFIG));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    Panel panel = HUDEditorRender.getPanel();
                    if (panel.title.equals(entry.getKey())) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }

                }
            } catch (IOException ignored) {

            }
        }
    }

    public static void trySet(IModule mods ,JsonObject jsonMod){
        try {
            for (Value value : mods.getValues()) {
                tryValue(mods.name, value,jsonMod);
            }
        } catch (Exception exception){
            HyperLethal.log.error("Cant set value for module "+ mods.getName() + "!");
            // exception.printStackTrace();
        }
    }

    public static void trySet(HUDModule hudModule ,JsonObject jsonMod){
        try {
            for (Value value : hudModule.getValues()) {
                tryValue(hudModule.getName(),value,jsonMod);
            }
        } catch (Exception exception){
            HyperLethal.log.error("Cant set value for hud "+ hudModule.getName() + "!");
            // exception.printStackTrace();
        }
    }

    public static void tryValue(String name, Value value ,JsonObject jsonMod){
        try {
            if (value instanceof BooleanValue) {
                boolean bvalue = jsonMod.get(value.getName()).getAsBoolean();
                value.setValue(bvalue);
            }
            if (value instanceof DoubleValue) {
                double dvalue = jsonMod.get(value.getName()).getAsDouble();
                value.setValue(dvalue);
            }
            if (value instanceof IntValue) {
                int ivalue = jsonMod.get(value.getName()).getAsInt();
                value.setValue(ivalue);
            }
            if (value instanceof FloatValue) {
                float fvalue = jsonMod.get(value.getName()).getAsFloat();
                value.setValue(fvalue);
            }
            if (value instanceof ModeValue) {
                ModeValue modeValue = (ModeValue) value;
                for (Mode mode : modeValue.getModes()) {
                    boolean mvalue = jsonMod.get(mode.getName()).getAsBoolean();
                    mode.setToggled(mvalue);
                }
            }
        }catch (Exception e) {
            HyperLethal.log.error("Cant set value for "+ name + "!Value name:"+value.getName());
            // e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (toFiles(SETTING_CONFIG).exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(SETTING_CONFIG));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    IModule mods = ModuleManager.getModuleByName(entry.getKey());
                    HUDModule hudModule = ModuleManager.getHUDByName(entry.getKey());
                    if (mods != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enable").getAsBoolean();
                        if (enabled) {
                            if (!mods.isEnabled()) {
                                mods.enable();
                            }
                        }
                        if (!mods.getValues().isEmpty()) {
                            trySet(mods,jsonMod);
                        }
                        mods.setBind(jsonMod.get("Bind").getAsInt());
                    }
                    if (hudModule != null) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enable").getAsBoolean();
                        if (enabled) {
                            if (!hudModule.isEnable()) {
                                hudModule.enable();
                            }
                        }
                        hudModule.x = jsonMod.get("x").getAsInt();
                        hudModule.y = jsonMod.get("y").getAsInt();
                        if (!hudModule.getValues().isEmpty()) {
                            trySet(hudModule,jsonMod);
                        }
                    }
                    if(entry.getKey().equals("Client")) {
                        JsonObject json = (JsonObject) entry.getValue();
                        tryClient(json);
                    }

                }
                firstOpen = false;
            } catch (IOException e) {
                //e.printStackTrace();
            }
        } else {
            HyperLethal.log.info("Config does not exists skipped loading config");
            firstOpen = true;
            SaveConfig();
            SaveFriends();
        }
    }

    public static void tryClient(JsonObject json){
        try {
            HyperLethal.INSTANCE.getCommandManager().cmdPrefix = json.get("Prefix").getAsString();
            InfoHUD.waterMark = json.get("WaterMark").getAsString();
            FakePlayer.customName = json.get("FPName").getAsString();
            CustomChat.CHAT_SUFFIX = json.get("Suffix").getAsString();
            AutoEz.ezMsg = json.get("Ez").getAsString();
            InfoHUD.TVer = json.get("TVer").getAsString();
            InfoHUD.version = json.get("Version").getAsString();
        }catch (Exception e){
            HyperLethal.log.error("Error while setting client!");
        }
    }

    public static void SaveConfig() {
        if (!toFiles(SETTING_CONFIG).exists()) {
            toFiles(SETTING_CONFIG).getParentFile().mkdirs();
            try {
                toFiles(SETTING_CONFIG).createNewFile();
            } catch (IOException e4) {
                // e4.printStackTrace();
            }
        }
        try {
            JsonObject json = new JsonObject();
            for (IModule module : ModuleManager.getModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("Bind", module.getBind());
                if (!module.getValues().isEmpty()) {
                    for (Value value : module.getValues()) {
                        if (value instanceof BooleanValue) {
                            jsonModule.addProperty(value.getName(), (boolean)value.getValue());
                        }
                        if (value instanceof IntValue) {
                            jsonModule.addProperty(value.getName(), (int)value.getValue());
                        }
                        if (value instanceof FloatValue) {
                            jsonModule.addProperty(value.getName(), (float)value.getValue());
                        }
                        if (value instanceof DoubleValue) {
                            jsonModule.addProperty(value.getName(), (double)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            ModeValue modeValue = (ModeValue) value;
                            for (Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                json.add(module.getName(), jsonModule);
            }
            for (HUDModule module : ModuleManager.hudModules) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("x", module.x);
                jsonModule.addProperty("y", module.y);
                if (!module.getValues().isEmpty()) {
                    for (Value value : module.getValues()) {
                        if (value instanceof BooleanValue) {
                            jsonModule.addProperty(value.getName(), (boolean)value.getValue());
                        }
                        if (value instanceof IntValue) {
                            jsonModule.addProperty(value.getName(), (int)value.getValue());
                        }
                        if (value instanceof FloatValue) {
                            jsonModule.addProperty(value.getName(), (float)value.getValue());
                        }
                        if (value instanceof DoubleValue) {
                            jsonModule.addProperty(value.getName(), (double)value.getValue());
                        }
                        if (value instanceof ModeValue) {
                            ModeValue modeValue = (ModeValue) value;
                            for (Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                json.add(module.getName(), jsonModule);
            }

            JsonObject client = new JsonObject();
            client.addProperty("Prefix", HyperLethal.getINSTANCE().getCommandManager().cmdPrefix);
            client.addProperty("WaterMark",InfoHUD.waterMark == null ? "HyperLethal" : InfoHUD.waterMark);
            client.addProperty("FPName",FakePlayer.customName == null ? "None" : FakePlayer.customName);
            client.addProperty("Suffix",CustomChat.CHAT_SUFFIX == null ? " \u029c\u028f\u1d18\u1d07\u0280\u029f\u1d07\u1d1b\u029c\u1d00\u029f \u1d1c\u029f\u1d1b\u026a\u1d0d\u1d00\u1d1b\u1d07"
                    : CustomChat.CHAT_SUFFIX);
            client.addProperty("Ez", AutoEz.ezMsg == null ? "default ez msg" : AutoEz.ezMsg);
            client.addProperty("TVer",InfoHUD.TVer == null ? HyperLethal.TVer : InfoHUD.TVer);
            client.addProperty("Version",InfoHUD.version == null ? HyperLethal.VERSION : InfoHUD.version);
            json.add("Client", client);

            PrintWriter saveJson = new PrintWriter(new FileWriter(SETTING_CONFIG));
            saveJson.println(gsonPretty.toJson(json));
            saveJson.close();
        } catch (IOException e){
            //  e.printStackTrace();
        }
    }

    public static void SaveFriends() {
        if (!toFiles(FRIEND_CONFIG).exists()) {
            toFiles(FRIEND_CONFIG).getParentFile().mkdirs();
            try {
                toFiles(FRIEND_CONFIG).createNewFile();
            } catch (IOException e4) {
                //  e4.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            List<String> friendList = HyperLethal.getINSTANCE().getFriendManger().getFriendList();
            writer = new BufferedWriter(new FileWriter(FRIEND_CONFIG, false));
            for (String friend : friendList) {
                writer.write(friend);
                writer.flush();
                writer.newLine();
            }
        } catch (Exception e){
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ignored){
            }
        }
    }

    public static void loadFriends() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FRIEND_CONFIG));
            String line;
            while ((line = reader.readLine()) != null) {
                HyperLethal.getINSTANCE().getFriendManger().addFriend(line);
            }
        }
        catch (Exception ex) {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ignored) {}
        }
    }


    public static void onLoad(){
        loadConfig();
        loadFriends();
        loadGUI();
        loadHUD();
    }

    public static void onSave(){
        saveGUI();
        saveHUD();
        SaveConfig();
        SaveFriends();
    }


    public static void deletedFiles(){
        File file = new File(SETTING_CONFIG);
        file.delete();
        HyperLethal.log.info("File deleted successfully!\n");
    }

     */
}
