package club.deneb.client.features;

import club.deneb.client.client.NotificationManager;
import club.deneb.client.event.events.render.RenderEvent;
import club.deneb.client.features.modules.client.Notification;
import club.deneb.client.gui.font.CFontRenderer;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.clazz.Button;
import club.deneb.client.value.*;
import club.deneb.client.Deneb;
import club.deneb.client.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by B_312 on 01/10/21
 * This is abstract module
 */
public class AbstractModule {

    public CFontRenderer font = Deneb.getINSTANCE().getFont();
    public String name;
    public boolean toggled;
    public String description;
    public Category category;
    public int keyCode;

    //HUD
    public boolean isHUD;
    public int x;
    public int y;
    public int width;
    public int height;

    private final ArrayList<Value<?>> values = new ArrayList<>();
    public ArrayList<Value<?>> getValues() {
        return values;
    }

    public boolean reset(){
        for(Value<?> value  : getValues()){
            value.reset();
        }
        this.disable();
        this.setBind(Keyboard.KEY_NONE);
        return true;
    }

    public static List<String> listOf(String ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static List<Integer> listOf(Integer ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static List<Double> listOf(Double ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static List<Float> listOf(Float ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static List<Boolean> listOf(Boolean ...strings){
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static final Minecraft mc = Wrapper.mc;
    public static final FontRenderer fontRenderer = mc.fontRenderer;

    public String getName(){
        return name;
    }

    public void enable() {
        toggled = true;
        MinecraftForge.EVENT_BUS.register(this);
        if(!isHUD) NotificationManager.addNewNotification(this,this.toggled);
        if(Notification.INSTANCE.chat.getValue()) ChatUtil.sendNoSpamMessage(name + " has been " + ChatUtil.SECTIONSIGN + "a" + "Enabled!");
        onEnable();
    }

    public void disable() {
        toggled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        if(!isHUD) NotificationManager.addNewNotification(this,this.toggled);
        if(Notification.INSTANCE.chat.getValue()) ChatUtil.sendNoSpamMessage(name + " has been " + ChatUtil.SECTIONSIGN + "c" + "Disabled!");
        onDisable();
    }

    public Value<Button> setting(String name, Button button){
        ButtonValue value = new ButtonValue(name, button);
        this.getValues().add(value);
        return value;
    }

    public Value<Boolean> setting(String name, boolean defaultValue){
        BooleanValue value = new BooleanValue(name,defaultValue);
        this.getValues().add(value);
        return value;
    }

    public Value<Integer> setting(String name, int defaultValue, int minValue, int maxValue){
        IntValue value = new IntValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public Value<Float> setting(String name, float defaultValue, float minValue, float maxValue){
        FloatValue value = new FloatValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public Value<Double> setting(String name, double defaultValue, double minValue, double maxValue){
        DoubleValue value = new DoubleValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public StringMode setting(String name, String defaultValue, List<String> modes){
        StringMode value = new StringMode(name,defaultValue,modes);
        this.getValues().add(value);
        return value;
    }

    public <T extends Enum<?>> EnumValue<T> setting(String name, Enum<?> enumValue){
        EnumValue<T> value = new EnumValue<>(name, enumValue);
        this.getValues().add(value);
        return value;
    }

    public boolean isEnabled(){
        return toggled;
    }
    public boolean isDisabled(){
        return !toggled;
    }

    public void onConfigLoad(){}
    public void onConfigSave(){}

    public void onEnable(){}
    public void onDisable(){}

    public void onTick(){}
    public void onRender(){}

    public void onRender2D(RenderGameOverlayEvent.Post event){}

    public void onWorldRender(RenderEvent event){}

    public String getHudInfo(){ return ""; }

    public void toggle(){
        toggled = !toggled;
        if (toggled){
            enable();
        } else {
            disable();
        }
    }

    public void onKey(InputUpdateEvent event){}

    public int getBind(){
        return keyCode;
    }

    public void setBind(int keycode){
        this.keyCode = keycode;
    }

    public void setEnable(boolean toggled){
        this.toggled = toggled;
    }

}
