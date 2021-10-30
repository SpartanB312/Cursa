package net.spartanb312.cursa.core.setting;

public class NumberSetting<T extends Number> extends Setting<T> {

    private final T min;
    private final T max;

    public NumberSetting(String name, T defaultValue, T min, T max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public boolean isInRange(Number valueIn) {
        return valueIn.doubleValue() <= max.doubleValue() && valueIn.doubleValue() >= min.doubleValue();
    }

}
