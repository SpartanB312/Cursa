package com.deneb.client.features.modules.client;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import com.deneb.client.value.FValue;
import com.deneb.client.value.IValue;
import com.deneb.client.value.MValue;

@Module.Info(name = "Colors",category = Category.CLIENT,visible = false)
public class Colors extends Module {

    public IValue red = setting("Red",255,0,255);
    public IValue green = setting("Green",0,0,255);
    public IValue blue = setting("Blue",0,0,255);
    public BValue particle = setting("Particle",true);
    public BValue rainbow = setting("Rainbow",false);
    public FValue rainbowSpeed = setting("Rainbow Speed", 5.0f,0.0f,30.0f).v(v -> rainbow.getValue());
    public FValue rainbowSaturation = setting("Saturation",0.65f,0.0f,1.0f).v(v -> rainbow.getValue());
    public FValue rainbowBrightness = setting("Brightness",1.0f,0.0f,1.0f).v(v -> rainbow.getValue());
    public MValue background = setting("Background",new MValue.Mode("Shadow",true),new MValue.Mode("Blur"),new MValue.Mode("Both"),new MValue.Mode("None"));
    public MValue setting = setting("Setting",new MValue.Mode("Rect"),new MValue.Mode("Side",true),new MValue.Mode("None"));

    static Colors INSTANCE;

    public static Colors getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
