package club.deneb.client.features.modules.client;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.FloatValue;
import club.deneb.client.value.IntValue;
import club.deneb.client.value.ModeValue;

@Module.Info(name = "Colors",category = Category.CLIENT,visible = false)
public class Colors extends Module {

    public IntValue red = setting("Red",255,0,255);
    public IntValue green = setting("Green",0,0,255);
    public IntValue blue = setting("Blue",0,0,255);
    public BooleanValue particle = setting("Particle",true);
    public BooleanValue rainbow = setting("Rainbow",false);
    public FloatValue rainbowSpeed = setting("Rainbow Speed", 5.0f,0.0f,30.0f).b(rainbow);
    public FloatValue rainbowSaturation = setting("Saturation",0.65f,0.0f,1.0f).b(rainbow);
    public FloatValue rainbowBrightness = setting("Brightness",1.0f,0.0f,1.0f).b(rainbow);
    public ModeValue background = setting("Background",new ModeValue.Mode("Shadow",true),new ModeValue.Mode("Blur"),new ModeValue.Mode("Both"),new ModeValue.Mode("None"));
    public ModeValue setting = setting("Setting",new ModeValue.Mode("Rect"),new ModeValue.Mode("Side",true),new ModeValue.Mode("None"));

    static Colors INSTANCE;

    public static Colors getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
