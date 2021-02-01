package club.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class IntValue extends Value<Integer>{
    protected Integer min, max;

    public IntValue(String name, Integer defaultValue, Integer min, Integer max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public IntValue v(Predicate<Object> predicate) {
        return (IntValue) super.v(predicate);
    }

    public IntValue page(PageValue.Page page) {
        return (IntValue) super.v(page.p());
    }

    public IntValue b(BooleanValue value) {
        return (IntValue) super.v(v -> value.getValue());
    }

    public IntValue r(BooleanValue value) {
        return (IntValue) super.v(v -> !value.getValue());
    }

    public IntValue c(double min, Value value, double max){
        if(value instanceof IntValue) {
            return (IntValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (IntValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (IntValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
        }
        return (IntValue) super.v(v -> true);
    }

    public IntValue c(double min, Value value){
        if(value instanceof IntValue) {
            return (IntValue) super.v(v -> ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (IntValue) super.v(v -> ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (IntValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
        }
        return (IntValue) super.v(v -> true);
    }

    public IntValue c(Value value, double max){
        if(value instanceof IntValue) {
            return (IntValue) super.v(v -> ((IntValue)value).getValue() <= max);
        }
        if(value instanceof FloatValue) {
            return (IntValue) super.v(v -> ((FloatValue)value).getValue() <= max);
        }
        if(value instanceof DoubleValue) {
            return (IntValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
        }
        return (IntValue) super.v(v -> true);
    }

    public IntValue m(ModeValue value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Integer getMin(){
        return min;
    }

    public Integer getMax() {
        return max;
    }
}