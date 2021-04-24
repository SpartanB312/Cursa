package club.deneb.client.features;

import club.deneb.client.client.GuiManager;
import club.deneb.client.utils.clazz.Button;
import club.deneb.client.value.ButtonValue;
import club.deneb.client.value.StringMode;
import club.deneb.client.value.Value;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by B_312 on 01/10/21
 */
public class Module extends AbstractModule {

    Value<String> visible_value;
    Value<Button> resetConfig;

    public boolean isShownOnArray(){
        return this.visible_value.toggled("ON");
    }

    public Module(){
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.description = getAnnotation().description();
        this.keyCode = getAnnotation().keyCode();
        this.getValues().add(visible_value = new StringMode("Visible",getAnnotation().visible() ? "ON" : "OFF" , listOf("ON","OFF")).v(GuiManager.INSTANCE::getVisibleButton));
        this.getValues().add(resetConfig = new ButtonValue("LoadDefault", new Button().setBind(this::reset)).des("Click here to reset this module").v(GuiManager.INSTANCE::getResetButton));
        this.isHUD = false;
        this.onInit();
    }

    public void onInit(){}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        String description() default "";
        int keyCode() default Keyboard.KEY_NONE;
        Category category();
        boolean visible() default true;
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

}
