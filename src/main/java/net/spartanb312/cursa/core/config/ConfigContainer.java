package net.spartanb312.cursa.core.config;

import com.google.gson.*;
import net.spartanb312.cursa.core.common.KeyBind;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.core.setting.settings.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigContainer {

    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private final List<Setting<?>> settings = new ArrayList<>();
    protected File configFile;

    public ConfigContainer() {
    }

    public ConfigContainer(String savePath, String saveName) {
        this.configFile = new File(savePath + saveName + ".json");
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void saveConfig() {
        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonObject jsonObject = new JsonObject();
            for (Setting<?> setting : settings) {
                if (setting instanceof BindSetting) {
                    jsonObject.addProperty(setting.getName(), ((BindSetting) setting).getValue().getKeyCode());
                }
                if (setting instanceof BooleanSetting) {
                    jsonObject.addProperty(setting.getName(), ((BooleanSetting) setting).getValue());
                }
                if (setting instanceof DoubleSetting) {
                    jsonObject.addProperty(setting.getName(), ((DoubleSetting) setting).getValue());
                }
                if (setting instanceof EnumSetting) {
                    jsonObject.addProperty(setting.getName(), ((EnumSetting<? extends Enum<?>>) setting).getValue().name());
                }
                if (setting instanceof FloatSetting) {
                    jsonObject.addProperty(setting.getName(), ((FloatSetting) setting).getValue());
                }
                if (setting instanceof IntSetting) {
                    jsonObject.addProperty(setting.getName(), ((IntSetting) setting).getValue());
                }
                if (setting instanceof StringSetting) {
                    jsonObject.addProperty(setting.getName(), ((StringSetting) setting).getValue());
                }
            }
            try {
                PrintWriter saveJSon = new PrintWriter(new FileWriter(configFile));
                saveJSon.println(gsonPretty.toJson(jsonObject));
                saveJSon.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readConfig() {
        if (configFile.exists()) {
            try {
                BufferedReader bufferedJson = new BufferedReader(new FileReader(configFile));
                JsonElement jsonElement = jsonParser.parse(bufferedJson);
                if (jsonElement instanceof JsonNull) return;
                JsonObject jsonObject = (JsonObject) jsonElement;
                bufferedJson.close();
                Map<String, JsonElement> map = new HashMap<>();
                jsonObject.entrySet().forEach(it -> map.put(it.getKey(), it.getValue()));
                for (Setting<?> setting : settings) {
                    JsonElement element = map.get(setting.getName());
                    if (element != null) {
                        if (setting instanceof BindSetting) {
                            ((BindSetting) setting).getValue().setKeyCode(element.getAsInt());
                        } else if (setting instanceof BooleanSetting) {
                            ((BooleanSetting) setting).setValue(element.getAsBoolean());
                        } else if (setting instanceof DoubleSetting) {
                            ((DoubleSetting) setting).setValue(element.getAsDouble());
                        } else if (setting instanceof EnumSetting) {
                            ((EnumSetting<?>) setting).setByName(element.getAsString());
                        } else if (setting instanceof FloatSetting) {
                            ((FloatSetting) setting).setValue(element.getAsFloat());
                        } else if (setting instanceof IntSetting) {
                            ((IntSetting) setting).setValue(element.getAsInt());
                        } else if (setting instanceof StringSetting) {
                            ((StringSetting) setting).setValue(element.getAsString());
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else saveConfig();
    }

    public Setting<KeyBind> setting(String name, KeyBind defaultValue) {
        BindSetting setting = new BindSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Boolean> setting(String name, boolean defaultValue) {
        BooleanSetting setting = new BooleanSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Double> setting(String name, double defaultValue, double minValue, double maxValue) {
        DoubleSetting setting = new DoubleSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public <E extends Enum<E>> Setting<E> setting(String name, E defaultValue) {
        EnumSetting<E> setting = new EnumSetting<>(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Float> setting(String name, float defaultValue, float minValue, float maxValue) {
        FloatSetting setting = new FloatSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Integer> setting(String name, int defaultValue, int minValue, int maxValue) {
        IntSetting setting = new IntSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public Setting<String> setting(String name, String defaultValue) {
        StringSetting setting = new StringSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

}
