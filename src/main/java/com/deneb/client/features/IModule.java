package com.deneb.client.features;

import com.deneb.client.Deneb;
import com.deneb.client.event.events.render.RenderEvent;
import com.deneb.client.gui.font.CFontRenderer;
import com.deneb.client.value.*;
import com.deneb.client.utils.Wrapper;
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
        onEnable();
    }

    public void disable() {
        toggled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        onDisable();
    }

    public BValue setting(String name, boolean defaultValue){
        BValue value = new BValue(name,defaultValue);
        this.getValues().add(value);
        return value;
    }

    public IValue setting(String name, int defaultValue, int minValue, int maxValue){
        IValue value = new IValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public FValue setting(String name, float defaultValue, float minValue, float maxValue){
        FValue value = new FValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public DValue setting(String name, double defaultValue, double minValue, double maxValue){
        DValue value = new DValue(name,defaultValue,minValue,maxValue);
        this.getValues().add(value);
        return value;
    }

    public MValue setting(String name, MValue.Mode... modes){
        MValue value = new MValue(name,modes);
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
        List<MValue.Mode> modes = new ArrayList<>();
        for(int i = 1;i <= values.length;i++){
            modes.add(new MValue.Mode(values[i-1],i==defaultPage));
        }
        MValue value = new MValue(name,modes);
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
