package club.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class FloatValue extends Value<Float>{
    protected Float min, max;

    public FloatValue(String name, Float defaultValue, Float min, Float max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public FloatValue v(Predicate<Object> predicate) {
        return (FloatValue) super.v(predicate);
    }

    public FloatValue page(PageValue.Page page) {
        return (FloatValue) super.v(page.p());
    }

    public FloatValue b(BooleanValue value) {
        return (FloatValue) super.v(v -> value.getValue());
    }

    public FloatValue r(BooleanValue value) {
        return (FloatValue) super.v(v -> !value.getValue());
    }

    public FloatValue c(double min, Value value, double max){
        if(value instanceof IntValue) {
            return (FloatValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (FloatValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (FloatValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
        }
        return (FloatValue) super.v(v -> true);
    }

    public FloatValue c(double min, Value value){
        if(value instanceof IntValue) {
            return (FloatValue) super.v(v -> ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (FloatValue) super.v(v -> ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (FloatValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
        }
        return (FloatValue) super.v(v -> true);
    }

    public FloatValue c(Value value, double max){
        if(value instanceof IntValue) {
            return (FloatValue) super.v(v -> ((IntValue)value).getValue() <= max);
        }
        if(value instanceof FloatValue) {
            return (FloatValue) super.v(v -> ((FloatValue)value).getValue() <= max);
        }
        if(value instanceof DoubleValue) {
            return (FloatValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
        }
        return (FloatValue) super.v(v -> true);
    }

    public FloatValue m(ModeValue value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() { return max; }
}
