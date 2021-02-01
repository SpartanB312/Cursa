package club.deneb.client.features;

import club.deneb.client.client.NotificationManager;
import club.deneb.client.event.events.render.RenderEvent;
import club.deneb.client.features.modules.client.Notification;
import club.deneb.client.gui.font.CFontRenderer;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.value.*;
import club.deneb.client.Deneb;
import club.deneb.client.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by B_312 on 01/10/21
 */
public class IModule {

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

    private final ArrayList<Value> values = new ArrayList<>();
    public ArrayList<Value> getValues() {
        return values;
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

    public ButtonValue setting(String name, String description){
        ButtonValue value = new ButtonValue(name,description);
        this.getValues().add(value);
        return value;
    }

    public BooleanValue setting(String name, boolean defaultValue){
        BooleanValue value = new BooleanValue(name,defaultValue);
        this.getValues().add(value);
        return value;
    }

    public IntValue setting(String name, int defaultValue, int minValue, int maxValue){
        IntValue value = new IntValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public FloatValue setting(String name, float defaultValue, float minValue, float maxValue){
        FloatValue value = new FloatValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public DoubleValue setting(String name, double defaultValue, double minValue, double maxValue){
        DoubleValue value = new DoubleValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public ModeValue setting(String name, ModeValue.Mode... modes){
        ModeValue value = new ModeValue(name,modes);
        this.getValues().add(value);
        return value;
    }

    public PageValue.Page newPage(PageValue value,int index){
        return new PageValue.Page(value.getValue(),index);
    }

    public PageValue.Page newPage(PageValue value,String name){
        return new PageValue.Page(value.getValue(),name);
    }

    public PageValue page(String name,int defaultPage,String... values){
        List<ModeValue.Mode> modes = new ArrayList<>();
        for(int i = 1;i <= values.length;i++){
            modes.add(new ModeValue.Mode(values[i-1],i==defaultPage));
        }
        ModeValue value = new ModeValue(name,modes);
        this.getValues().add(value);
        return new PageValue(value);
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
