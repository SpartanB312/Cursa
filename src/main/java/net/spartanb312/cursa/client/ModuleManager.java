package net.spartanb312.cursa.client;

import net.minecraft.client.Minecraft;
import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.event.Listener;
import net.spartanb312.cursa.core.event.Priority;
import net.spartanb312.cursa.event.events.client.KeyEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.gui.CursaHUDEditor;
import net.spartanb312.cursa.gui.renderers.HUDEditorRenderer;
import net.spartanb312.cursa.hud.HUDModule;
import net.spartanb312.cursa.hud.huds.CombatInfo;
import net.spartanb312.cursa.hud.huds.Welcomer;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.module.modules.client.*;
import net.spartanb312.cursa.module.modules.combat.Anti32kTotem;
import net.spartanb312.cursa.module.modules.combat.AutoTotem;
import net.spartanb312.cursa.module.modules.combat.CursaAura;
import net.spartanb312.cursa.module.modules.combat.OffHandCrystal;
import net.spartanb312.cursa.module.modules.misc.CustomChat;
import net.spartanb312.cursa.module.modules.misc.SkinFlicker;
import net.spartanb312.cursa.module.modules.misc.Spammer;
import net.spartanb312.cursa.module.modules.movement.Sprint;
import net.spartanb312.cursa.module.modules.movement.Velocity;
import net.spartanb312.cursa.module.modules.player.AntiContainer;
import net.spartanb312.cursa.module.modules.player.AutoJump;
import net.spartanb312.cursa.module.modules.player.FakePlayer;
import net.spartanb312.cursa.module.modules.render.AntiOverlay;
import net.spartanb312.cursa.module.modules.render.Brightness;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runBlocking;

public class ModuleManager {

    public final Map<Class<? extends Module>, Module> moduleMap = new ConcurrentHashMap<>();
    public final List<Module> moduleList = new ArrayList<>();
    private final Set<Class<? extends Module>> classes = new HashSet<>();

    private static ModuleManager instance;

    public static List<Module> getModules() {
        return getInstance().moduleList;
    }

    public static void init() {
        if (instance == null) instance = new ModuleManager();
        instance.moduleMap.clear();

        //Client
        registerNewModule(ActiveModuleList.class);
        registerNewModule(ClickGUI.class);
        registerNewModule(GUISetting.class);
        registerNewModule(HUDEditor.class);
        registerNewModule(WaterMark.class);

        //Combat
        registerNewModule(Anti32kTotem.class);
        registerNewModule(AutoTotem.class);
        registerNewModule(CursaAura.class);
        registerNewModule(OffHandCrystal.class);

        //Misc
        registerNewModule(CustomChat.class);
        registerNewModule(SkinFlicker.class);
        registerNewModule(Spammer.class);

        //Movement
        registerNewModule(Sprint.class);
        registerNewModule(Velocity.class);

        //Player
        registerNewModule(AntiContainer.class);
        registerNewModule(AutoJump.class);
        registerNewModule(FakePlayer.class);

        //Render
        registerNewModule(AntiOverlay.class);
        registerNewModule(Brightness.class);

        //HUD
        registerNewModule(CombatInfo.class);
        registerNewModule(Welcomer.class);

        instance.loadModules();
        Cursa.EVENT_BUS.register(instance);
    }

    public static void registerNewModule(Class<? extends Module> clazz) {
        instance.classes.add(clazz);
    }

    @Listener(priority = Priority.HIGHEST)
    public void onKey(KeyEvent event) {
        moduleList.forEach(it -> it.keyBinds.forEach(binds -> binds.test(event.getKey())));
    }

    @Listener(priority = Priority.HIGHEST)
    public void onRenderHUD(RenderOverlayEvent event) {
        for (int i = HUDEditorRenderer.instance.hudModules.size() - 1; i >= 0; i--) {
            HUDModule hudModule = HUDEditorRenderer.instance.hudModules.get(i);

            if (!(Minecraft.getMinecraft().currentScreen instanceof CursaHUDEditor) && hudModule.isEnabled())
                hudModule.onHUDRender(event.getScaledResolution());
        }
    }

    public static ModuleManager getInstance() {
        if (instance == null) instance = new ModuleManager();
        return instance;
    }

    public static Module getModule(Class<? extends Module> clazz) {
        return getInstance().moduleMap.get(clazz);
    }

    public static Module getModuleByName(String targetName) {
        for (Module module : getModules()) {
            if (module.name.equalsIgnoreCase(targetName)) {
                return module;
            }
        }
        Cursa.log.info("Module " + targetName + " is not exist.Please check twice!");
        return null;
    }

    private void loadModules() {
        Cursa.log.info("[ModuleManager]Loading modules.");
        runBlocking(unit -> classes.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            if (clazz != HUDModule.class) {
                try {
                    if (clazz.isAnnotationPresent(Parallel.class) && clazz.getAnnotation(Parallel.class).loadable()) {
                        unit.launch(() -> {
                            try {
                                add(clazz.newInstance(), clazz);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println("Couldn't initiate Module " + clazz.getSimpleName() + "! Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
                            }
                        });
                    } else {
                        add(clazz.newInstance(), clazz);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Couldn't initiate Module " + clazz.getSimpleName() + "! Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
                }
            }
        }));
        sort();
        Cursa.log.info("[ModuleManager]Loaded " + moduleList.size() + " modules");
    }

    private synchronized void add(Module module, Class<? extends Module> clazz) {
        moduleList.add(module);
        moduleMap.put(clazz, module);
    }

    private void sort() {
        moduleList.sort(Comparator.comparing(it -> it.name));
    }


}
