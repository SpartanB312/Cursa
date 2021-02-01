package club.deneb.client.features;

import club.deneb.client.value.ModeValue;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by B_312 on 01/10/21
 */
public class Module extends IModule{

    ModeValue visible_value;

    public boolean isShownOnArray(){
        return this.visible_value.getMode("ON").isToggled();
    }

    public Module(){
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.description = getAnnotation().description();
        this.keyCode = getAnnotation().keyCode();
        this.getValues().add(visible_value = new ModeValue("Visible",new ModeValue.Mode("ON",getAnnotation().visible()),new ModeValue.Mode("OFF",!getAnnotation().visible())));
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
