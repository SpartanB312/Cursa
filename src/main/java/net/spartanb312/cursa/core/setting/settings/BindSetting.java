package net.spartanb312.cursa.core.setting.settings;

import net.spartanb312.cursa.core.common.KeyBind;
import net.spartanb312.cursa.core.setting.Setting;

public class BindSetting extends Setting<KeyBind> {
    public BindSetting(String name, KeyBind defaultValue) {
        super(name, defaultValue);
    }
}
