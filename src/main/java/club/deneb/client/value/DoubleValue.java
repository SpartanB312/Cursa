package club.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class DoubleValue extends Value<Double> {

    protected Double min, max;

    public DoubleValue(String name, Double defaultValue, Double min, Double max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public DoubleValue v(Predicate<Object> predicate) {
        return (DoubleValue) super.v(predicate);
    }

    public DoubleValue page(PageValue.Page page) {
        return (DoubleValue) super.v(page.p());
    }

    public DoubleValue b(BooleanValue value) {
        return (DoubleValue) super.v(v -> value.getValue());
    }

    public DoubleValue r(BooleanValue value) {
        return (DoubleValue) super.v(v -> !value.getValue());
    }

    public DoubleValue c(double min, Value value, double max){
        if(value instanceof IntValue) {
            return (DoubleValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (DoubleValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (DoubleValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
        }
        return (DoubleValue) super.v(v -> true);
    }

    public DoubleValue c(double min, Value value){
        if(value instanceof IntValue) {
            return (DoubleValue) super.v(v -> ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (DoubleValue) super.v(v -> ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (DoubleValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
        }
        return (DoubleValue) super.v(v -> true);
    }

    public DoubleValue c(Value value, double max){
        if(value instanceof IntValue) {
            return (DoubleValue) super.v(v -> ((IntValue)value).getValue() <= max);
        }
        if(value instanceof FloatValue) {
            return (DoubleValue) super.v(v -> ((FloatValue)value).getValue() <= max);
        }
        if(value instanceof DoubleValue) {
            return (DoubleValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
        }
        return (DoubleValue) super.v(v -> true);
    }

    public DoubleValue m(ModeValue value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }
}
