package com.deneb.client.utils;

import com.deneb.client.features.ModuleManager;
import com.deneb.client.gui.component.Component;

import java.util.function.Predicate;

public class LambdaUtil {

    public static Predicate<String> isEnabled(String name){
        return b -> ModuleManager.getModuleByName(b).isEnabled();
    }

    public static Predicate<String> isDisabled(String name){
        return b -> ModuleManager.getModuleByName(b).isDisabled();
    }

    public static Predicate<Component> isHovered(int mouseX, int mouseY) {
        return c -> mouseX >= Math.min(c.x,c.x + c.width) && mouseX <= Math.max(c.x,c.x+c.width)  && mouseY >= Math.min(c.y,c.y + c.height) && mouseY <= Math.max(c.y,c.y + c.height);
    }

    public static class Bool{
        Predicate<Object> predicate;

        public Bool() {
            this.predicate = v -> true;
        }

        public boolean get(){
            return this.predicate.test(this);
        }

        public Predicate<Object> p(){
            return predicate;
        }

        public Bool set(Predicate<Object> predicate) {
            this.predicate = predicate;
            return this;
        }

    }


}
