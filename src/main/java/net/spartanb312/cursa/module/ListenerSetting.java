package net.spartanb312.cursa.module;

import net.spartanb312.cursa.core.concurrent.task.VoidTask;
import net.spartanb312.cursa.core.setting.Setting;

public class ListenerSetting extends Setting<VoidTask> {
    public ListenerSetting(String name, VoidTask defaultValue) {
        super(name, defaultValue);
    }
}
