package net.spartanb312.cursa.command;

import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.minecraft.client.Minecraft;

public abstract class Command {

    private final String command;
    private final String description;

    public static final Minecraft mc = Minecraft.getMinecraft();

    public Command() {
        this.command = getAnnotation().command();
        this.description = getAnnotation().description();
    }

    private CommandInfo getAnnotation() {
        if (getClass().isAnnotationPresent(CommandInfo.class)) {
            return getClass().getAnnotation(CommandInfo.class);
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