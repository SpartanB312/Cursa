package club.deneb.client.features;

import club.deneb.client.utils.ClassFinder;
import club.deneb.client.Deneb;
import club.deneb.client.event.events.render.RenderEvent;
import club.deneb.client.gui.guis.HUDEditorScreen;
import club.deneb.client.features.modules.client.NullHUD;
import club.deneb.client.features.modules.client.NullModule;
import club.deneb.client.utils.DenebTessellator;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by B_312 on 01/10/21
 */
public class ModuleManager {

    public static ModuleManager INSTANCE;
    private final HashMap<Class<? extends AbstractModule>,AbstractModule> moduleHashMap = new HashMap<>();

    private final List<AbstractModule> moduleList = new ArrayList<>();

    private List<AbstractModule> modules(){
        return moduleList;
    }

    public ModuleManager(){
        INSTANCE = this;
        init();
    }

    private void init(){
        loadModules();
        loadHUDs();
        moduleList.addAll(moduleHashMap.values());
        moduleList.sort(Comparator.comparing(AbstractModule::getName));
    }


    public static void onKey(InputUpdateEvent event){
        INSTANCE.modules().forEach( mod -> {
            if (mod.isEnabled()) mod.onKey(event);
        });
    }

    private void loadModules(){
        Set<Class> classList = ClassFinder.findClasses(AbstractModule.class.getPackage().getName(), Module.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(aClass -> {
            try {
                Module mod = (Module) aClass.newInstance();
                moduleHashMap.put(aClass,mod);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    private void loadHUDs(){
        Set<Class> classList = ClassFinder.findClasses(AbstractModule.class.getPackage().getName(), HUDModule.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(aClass -> {
            try {
                HUDModule mod = (HUDModule) aClass.newInstance();
                moduleHashMap.put(aClass,mod);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate HUD Module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    public static List<AbstractModule> getAllModules(){
        return INSTANCE.modules();
    }

    public static List<AbstractModule> getModules(){
        return INSTANCE.modules().stream().filter(module -> !module.isHUD).collect(Collectors.toList());
    }

    public static List<AbstractModule> getHUDModules(){
        return INSTANCE.modules().stream().filter(module -> module.isHUD).collect(Collectors.toList());
    }

    public static AbstractModule getModuleByName(String targetName) {
        for(AbstractModule iModule : getAllModules()){
            if(iModule.name.equalsIgnoreCase(targetName)){
                return iModule;
            }
        }
        Deneb.log.fatal("Module "+ targetName +" is not exist.Please check twice!");
        return new NullModule();
    }

    public static AbstractModule getModule(Class<? extends AbstractModule> clazz){
        return INSTANCE.moduleHashMap.get(clazz);
    }

    public static HUDModule getHUDByName(String targetName) {
        for(AbstractModule iModule : getHUDModules()){
            if(iModule.name.equalsIgnoreCase(targetName)){
                return (HUDModule)iModule;
            }
        }
        Deneb.log.fatal("HUD "+ targetName +" is not exist.Please check twice!");
        return new NullHUD();
    }

    public static void onBind(int bind) {
        if (bind == 0) return;
        INSTANCE.modules().forEach(module -> {
            if (module.getBind() == bind) {
                module.toggle();
            }
        });
    }

    public static void onTick() {
        INSTANCE.modules().forEach(mod -> {
            if (mod.isEnabled()) mod.onTick();
        });
    }


    public static void onRender(RenderGameOverlayEvent.Post event) {
        INSTANCE.modules().forEach( mod -> {
            if (mod.isEnabled()) mod.onRender2D(event);
        });
        onRenderHUD();
    }

    public static void onRenderHUD() {
        if(!(Minecraft.getMinecraft().currentScreen instanceof HUDEditorScreen)) {
            getHUDModules().forEach( mod -> {
                if (mod.isEnabled()) mod.onRender();
            });
        }
    }

    public static void onWorldRender(RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("deneb");

        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        Vec3d renderPos = getInterpolatedPos(Objects.requireNonNull(Minecraft.getMinecraft().getRenderViewEntity()), event.getPartialTicks());

        RenderEvent e = new RenderEvent(DenebTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        INSTANCE.modules().forEach(mod -> {
            if (mod.isEnabled()) {
                Minecraft.getMinecraft().profiler.startSection(mod.getName());
                mod.onWorldRender(e);
                Minecraft.getMinecraft().profiler.endSection();
            }
        });

        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        DenebTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(EntityUtil.getInterpolatedAmount(entity, ticks));
    }

    public static List<AbstractModule> getModulesByCategory(Category category){
        return getAllModules().stream().filter(module -> module.category.equals(category)).collect(Collectors.toList());
    }
}
