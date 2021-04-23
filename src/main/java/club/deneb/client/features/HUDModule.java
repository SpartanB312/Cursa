package club.deneb.client.features;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by B_312 on 01/10/21
 */
public class HUDModule extends AbstractModule {

    public HUDModule(){
        this.x = getAnnotation().x();
        this.y = getAnnotation().y();
        this.width = getAnnotation().width();
        this.height = getAnnotation().height();
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.description = getAnnotation().description();
        this.isHUD = true;
        onInit();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        int x() default 0;
        int y() default 0;
        int width() default 0;
        int height() default 0;
        Category category() default Category.HUD;
        String description() default "";
        boolean visible() default true;
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public void onInit(){}

    public void onDragging(int mouseX,int mouseY){}

    public void onMouseRelease(){}

}
