package club.deneb.client.features.modules.client;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.ModeValue;
import club.deneb.client.value.Value;

@Module.Info(name = "Colors",category = Category.CLIENT,visible = false)
public class Colors extends Module {

    public Value<Integer> red = setting("Red",255,0,255);
    public Value<Integer> green = setting("Green",0,0,255);
    public Value<Integer> blue = setting("Blue",0,0,255);
    public Value<Boolean> particle = setting("Particle",true);
    public Value<Boolean> rainbow = setting("Rainbow",false);
    public Value<Float> rainbowSpeed = setting("Rainbow Speed", 5.0f,0.0f,30.0f).b(rainbow);
    public Value<Float> rainbowSaturation = setting("Saturation",0.65f,0.0f,1.0f).b(rainbow);
    public Value<Float> rainbowBrightness = setting("Brightness",1.0f,0.0f,1.0f).b(rainbow);
    public ModeValue<String> background = setting("Background","Shadow",listOf("Shadow","Blur","Both","None"));
    public ModeValue<String> setting = setting("Setting","Rect",listOf("Rect","Side","None"));

    static Colors INSTANCE;

    public static Colors getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
