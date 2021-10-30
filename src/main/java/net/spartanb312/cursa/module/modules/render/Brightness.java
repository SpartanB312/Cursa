package net.spartanb312.cursa.module.modules.render;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;

import java.util.Stack;
import java.util.function.Function;

@Parallel(runnable = true)
@ModuleInfo(name = "Brightness", category = Category.RENDER, description = "Always bright")
public class Brightness extends Module {

    Setting<Boolean> transition = setting("Transition", true);
    Setting<Float> seconds = setting("Seconds", 1f, 0f, 10f).whenTrue(transition);
    Setting<Mode> mode = setting("Mode", Mode.Sine).whenTrue(transition);

    enum Mode {
        Sine, Liner
    }

    private final Stack<Float> transitionStack = new Stack<>();

    private static boolean inTransition = false;

    private void addTransition(boolean isUpwards) {
        if (transition.getValue()) {
            int length = (int) (seconds.getValue() * 20);
            float[] values;
            switch (mode.getValue()) {
                case Liner:
                    values = linear(length, isUpwards);
                    break;
                case Sine:
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
        addTransition(true);
    }

    @Override
    public void onDisable() {
        addTransition(false);
    }

    @Override
    public void onTick() {
        if (inTransition) {
            if (transitionStack.isEmpty()) {
                inTransition = false;
            } else {
                transitionStack.pop();
            }
        }
    }

    @Override
    public String getModuleInfo() {
        return mode.getValue().name();
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

}
