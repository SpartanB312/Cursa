package com.deneb.client.features.modules.render;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import com.deneb.client.value.FValue;
import com.deneb.client.value.MValue;

import java.util.Stack;
import java.util.function.Function;

/**
 * Created by 086 on 12/12/2017.
 */
@Module.Info(name = "Brightness", description = "Makes everything brighter!", category = Category.RENDER)
public class Brightness extends Module {

    BValue transition = setting("Transition", true);
    FValue seconds = setting("Seconds",1f,0f,10f).b(transition);
    MValue mode = setting("Mode",new MValue.Mode("Sine",true),new MValue.Mode("Liner")).b(transition);

    private final Stack<Float> transitionStack = new Stack<>();

    private static float currentBrightness = 0;
    private static boolean inTransition = false;

    private void addTransition(boolean isUpwards) {
        if (transition.getValue()) {
            int length = (int) (seconds.getValue() * 20);
            float[] values;
            switch (mode.getToggledMode().getName()) {
                case "Liner":
                    values = linear(length, isUpwards);
                    break;
                case "Sine":
                    values = sine(length, isUpwards);
                    break;
                default:
                    values = new float[]{0};
                    break;
            }
            for (float v : values) {
                transitionStack.add(v);
            }

            inTransition = true;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        addTransition(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        addTransition(false);
    }

    @Override
    public void onTick() {
        if (inTransition) {
            if (transitionStack.isEmpty()) {
                inTransition = false;
                currentBrightness = isEnabled() ? 1 : 0;
            } else {
                currentBrightness = transitionStack.pop();
            }
        }
    }

    private float[] createTransition(int length, boolean upwards, Function<Float, Float> function) {
        float[] transition = new float[length];
        for (int i = 0; i < length; i++) {
            float v = function.apply(((float) i / (float) length));
            if (upwards) v = 1 - v;
            transition[i] = v;
        }
        return transition;
    }

    private float[] linear(int length, boolean polarity) { // length of 20 = 1 second
        return createTransition(length, polarity, d -> d);
    }

    private float sine(float x) { // (sin(pi*x-(pi/2)) + 1) / 2
        return ((float) Math.sin(Math.PI * x - Math.PI / 2) + 1) / 2;
    }

    private float[] sine(int length, boolean polarity) {
        return createTransition(length, polarity, this::sine);
    }

    public static float getCurrentBrightness() {
        return currentBrightness;
    }

    public static boolean isInTransition() {
        return inTransition;
    }

    public static boolean shouldBeActive() {
        return isInTransition() || currentBrightness == 1; // if in transition or enabled
    }
}
