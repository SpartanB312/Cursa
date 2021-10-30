package net.spartanb312.cursa.client;

import com.google.gson.*;
import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.core.config.ConfigContainer;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.gui.renderers.ClickGUIRenderer;
import net.spartanb312.cursa.gui.renderers.HUDEditorRenderer;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.module.modules.player.FakePlayer;
import net.spartanb312.cursa.utils.ListUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigManager {

    public static final String CONFIG_PATH = "Cursa/config/";
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private final File CLIENT_FILE = new File(CONFIG_PATH + "Cursa_Client.json");
    private final File GUI_FILE = new File(CONFIG_PATH + "Cursa_GUI.json");

    private final List<File> configList = ListUtil.listOf(CLIENT_FILE, GUI_FILE);

    boolean shouldSave = false;

    public void shouldSave() {
        shouldSave = true;
    }

    public void onInit() {
        configList.forEach(it -> {
            if (!it.exists()) {
                shouldSave();
            }
        });
        if (shouldSave) saveAll();
    }

    public void saveGUI() {
        try {
            if (!GUI_FILE.exists()) {
                GUI_FILE.getParentFile().mkdirs();
                try {
                    GUI_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }
            JsonObject father = new JsonObject();
            List<Panel> panels = new ArrayList<>(ClickGUIRenderer.instance.panels);
            panels.addAll(HUDEditorRenderer.instance.panels);
            for (Panel panel : panels) {
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.categoryName, jsonGui);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(GUI_FILE));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            Cursa.log.error("Error while saving GUI config!");
            e.printStackTrace();
        }
    }

    public void loadGUI() {
        if (GUI_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(GUI_FILE));
                JsonObject guiJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJson.entrySet()) {
                    Panel panel = ClickGUIRenderer.instance.getPanelByName(entry.getKey());
                    if (panel == null) panel = HUDEditorRenderer.instance.getPanelByName(entry.getKey());
                    if (panel != null) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }
                }
            } catch (IOException e) {
                Cursa.log.error("Error while loading GUI config!");
                e.printStackTrace();
            }
        }
    }

    public void saveClient() {
        try {
            if (!CLIENT_FILE.exists()) {
                CLIENT_FILE.getParentFile().mkdirs();
                try {
                    CLIENT_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }

            JsonObject father = new JsonObject();

            saveClientStuff(father);
            saveFriend(father);

            PrintWriter saveJSon = new PrintWriter(new FileWriter(CLIENT_FILE));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            Cursa.log.error("Error while saving client stuff!");
            e.printStackTrace();
        }
    }

    private void loadClient() {
        if (CLIENT_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(CLIENT_FILE));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    if (entry.getKey().equals("Client")) {
                        JsonObject json = (JsonObject) entry.getValue();
                        trySetClient(json);
                    } else if (entry.getKey().equals("Friends")) {
                        JsonArray array = (JsonArray) entry.getValue();
                        array.forEach(it -> FriendManager.getInstance().friends.add(it.getAsString()));
                    }
                }
            } catch (IOException e) {
                Cursa.log.error("Error while loading client stuff!");
                e.printStackTrace();
            }
        }
    }

    private void saveFriend(JsonObject father) {
        JsonArray array = new JsonArray();
        FriendManager.getInstance().friends.forEach(array::add);
        father.add("Friends", array);
    }

    private void saveClientStuff(JsonObject father) {
        JsonObject stuff = new JsonObject();
        stuff.addProperty("CommandPrefix", CommandManager.cmdPrefix);
        stuff.addProperty("ChatSuffix", Cursa.CHAT_SUFFIX);
        stuff.addProperty("FakePlayerName", FakePlayer.customName);
        father.add("Client", stuff);
    }

    private void trySetClient(JsonObject json) {
        try {
            CommandManager.cmdPrefix = json.get("CommandPrefix").getAsString();
            Cursa.CHAT_SUFFIX = json.get("ChatSuffix").getAsString();
            FakePlayer.customName = json.get("FakePlayerName").getAsString();
        } catch (Exception e) {
            Cursa.log.error("Error while setting client!");
        }
    }

    private void loadModule() {
        ModuleManager.getModules().forEach(Module::onLoad);
    }

    private void saveModule() {
        ModuleManager.getModules().forEach(Module::onSave);
    }

    public static void loadAll() {
        getInstance().loadClient();
        getInstance().loadGUI();
        getInstance().loadModule();
    }

    public static void saveAll() {
        getInstance().saveClient();
        getInstance().saveGUI();
        getInstance().saveModule();
    }

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public static void init() {
        instance = new ConfigManager();
        instance.onInit();
        loadAll();
    }
}
