package com.deneb.client.command;

import com.deneb.client.utils.Wrapper;
import net.minecraft.client.Minecraft;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by B_312 on 01/15/21
 */
public abstract class Command {
    private final String command;
    private final String description;

    public static final Minecraft mc = Wrapper.mc;

    public Command() {
        this.command = getAnnotation().command();
        this.description = getAnnotation().description();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String command();

        String description() default "";
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public abstract void onCall(String s, String... args);

    public abstract String getSyntax();

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }
}
