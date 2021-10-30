package net.spartanb312.cursa.common.annotations;

import net.spartanb312.cursa.module.Category;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();

    int keyCode() default Keyboard.KEY_NONE;

    Category category();

    String description() default "";

    boolean visible() default true;
}
