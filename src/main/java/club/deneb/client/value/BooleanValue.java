package club.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class BooleanValue extends Value<Boolean> {

    public BooleanValue(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanValue v(Predicate<Object> predicate) {
        return (BooleanValue) super.v(predicate);
    }

    public BooleanValue page(PageValue.Page page) {
        return (BooleanValue) super.v(page.p());
    }

    public BooleanValue b(BooleanValue value) {
        return (BooleanValue) super.v(v -> value.getValue());
    }

    public BooleanValue r(BooleanValue value) {
        return (BooleanValue) super.v(v -> !value.getValue());
    }

    public BooleanValue c(double min, Value value, double max){
        if(value instanceof IntValue) {
            return (BooleanValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (BooleanValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (BooleanValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
        }
        return (BooleanValue) super.v(v -> true);
    }

    public BooleanValue c(double min, Value value){
        if(value instanceof IntValue) {
            return (BooleanValue) super.v(v -> ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (BooleanValue) super.v(v -> ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (BooleanValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
        }
        return (BooleanValue) super.v(v -> true);
    }

    public BooleanValue c(Value value, double max){
        if(value instanceof IntValue) {
            return (BooleanValue) super.v(v -> ((IntValue)value).getValue() <= max);
        }
        if(value instanceof FloatValue) {
            return (BooleanValue) super.v(v -> ((FloatValue)value).getValue() <= max);
        }
        if(value instanceof DoubleValue) {
            return (BooleanValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
        }
        return (BooleanValue) super.v(v -> true);
    }

    public BooleanValue m(ModeValue value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

}
