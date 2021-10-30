package net.spartanb312.cursa.event.events.client;

import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.CursaEvent;

public class SettingUpdateEvent extends CursaEvent {

    private final Setting<?> setting;

    public SettingUpdateEvent(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
