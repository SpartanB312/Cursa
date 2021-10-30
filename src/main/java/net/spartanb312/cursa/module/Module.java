package net.spartanb312.cursa.module;

import net.minecraft.client.Minecraft;
import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.client.ConfigManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.common.KeyBind;
import net.spartanb312.cursa.core.concurrent.task.Task;
import net.spartanb312.cursa.core.concurrent.task.VoidTask;
import net.spartanb312.cursa.core.config.ListenableContainer;
import net.spartanb312.cursa.core.event.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.core.event.decentralization.EventData;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.client.InputUpdateEvent;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.event.events.render.RenderEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.notification.NotificationManager;
import net.spartanb312.cursa.utils.ChatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Module extends ListenableContainer {

    public final String name = getAnnotation().name();
    public final Category category = getAnnotation().category();
    public final Parallel annotation = getClass().getAnnotation(Parallel.class);
    public final boolean parallelRunnable = annotation != null && annotation.runnable();
    public final String description = getAnnotation().description();

    public Module() {
        configFile = new File(ConfigManager.CONFIG_PATH + "modules/" + category.categoryName + "/" + name + ".json");
    }

    boolean enabled = false;
    private final ConcurrentHashMap<DecentralizedEvent<? extends EventData>, Task<? extends EventData>> listenerMap = new ConcurrentHashMap<>();
    public Minecraft mc = Minecraft.getMinecraft();

    public final List<KeyBind> keyBinds = new ArrayList<>();

    protected final Setting<Boolean> enabledSetting = setting("Enabled", false).when(() -> false);
    public final Setting<KeyBind> bindSetting = setting("Bind", subscribeKey(new KeyBind(getAnnotation().keyCode(), this::toggle))).des("The key bind of this module");
    private final Setting<Visibility> visibleSetting = setting("Visible", Visibility.True).des("Determine the visibility of the module");
    private final Setting<VoidTask> reset = actionListener("Reset", () -> {
        disable();
        getSettings().forEach(Setting::reset);
    }).des("Reset this module");

    enum Visibility {
        True,
        False
    }

    public void onSave() {
        enabledSetting.setValue(enabled);
        saveConfig();
    }

    public void onLoad() {
        readConfig();
        if (enabledSetting.getValue() && !enabled) enable();
        else if (!enabledSetting.getValue() && enabled) disable();
    }

    public KeyBind subscribeKey(KeyBind keyBind) {
        keyBinds.add(keyBind);
        return keyBind;
    }

    public KeyBind unsubscribeKey(KeyBind keyBind) {
        keyBinds.remove(keyBind);
        return keyBind;
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    public void reload() {
        if (enabled) {
            enabled = false;
            Cursa.MODULE_BUS.unregister(this);
            onDisable();
            enabled = true;
            Cursa.MODULE_BUS.register(this);
            onEnable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public void enable() {
        enabled = true;
        Cursa.MODULE_BUS.register(this);
        subscribe();
        NotificationManager.moduleToggle(this, true);
        onEnable();
    }

    public void disable() {
        enabled = false;
        Cursa.MODULE_BUS.unregister(this);
        unsubscribe();
        NotificationManager.moduleToggle(this, false);
        onDisable();
    }

    public void onPacketReceive(PacketEvent.Receive event) {
    }

    public void onPacketSend(PacketEvent.Send event) {
    }

    public void onTick() {
    }

    public void onRenderTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onRender(RenderOverlayEvent event) {
    }

    public void onRenderWorld(RenderEvent event) {
    }

    public void onInputUpdate(InputUpdateEvent event) {
    }

    public void onSettingChange(Setting<?> setting) {
    }

    public Setting<VoidTask> actionListener(String name, VoidTask defaultValue) {
        ListenerSetting setting = new ListenerSetting(name, defaultValue);
        getSettings().add(setting);
        return setting;
    }

    @SafeVarargs
    public final <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    private ModuleInfo getAnnotation() {
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            return getClass().getAnnotation(ModuleInfo.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public String getModuleInfo() {
        return "";
    }

    public String getHudSuffix() {
        return this.name + (!this.getModuleInfo().equals("") ? (ChatUtil.colored("7") + "[" + ChatUtil.colored("f") + this.getModuleInfo() + ChatUtil.colored("7") + "]") : this.getModuleInfo());
    }

}
