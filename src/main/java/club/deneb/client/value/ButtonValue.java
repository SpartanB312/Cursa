package club.deneb.client.value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by B_312 on 01/31/21
 */
public class ButtonValue extends Value<String> {

    public List<Predicate<Object>> binds = new ArrayList<>();

    public ButtonValue(String name, String description) {
        super(name, description);
        binds.add(v -> true);
    }

    public void runBinds(){
        for(Predicate<Object> bind : binds){
            if(bind.test(this)) {
                whenReturnTrue();
            }
        }
    }

    public void whenReturnTrue(){
        //We have nothing to do
    }

    public ButtonValue v(Predicate<Object> predicate) {
        return (ButtonValue) super.v(predicate);
    }

    public ButtonValue bind(Predicate<Object> predicate) {
        binds.add(predicate);
        return this;
    }

    public ButtonValue page(PageValue.Page page) {
        return (ButtonValue) super.v(page.p());
    }

    public ButtonValue c(double min, Value value, double max){
        if(value instanceof IntValue) {
            return (ButtonValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (ButtonValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (ButtonValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
        }
        return (ButtonValue) super.v(v -> true);
    }

    public ButtonValue c(double min, Value value){
        if(value instanceof IntValue) {
            return (ButtonValue) super.v(v -> ((IntValue)value).getValue() >= min);
        }
        if(value instanceof FloatValue) {
            return (ButtonValue) super.v(v -> ((FloatValue)value).getValue() >= min);
        }
        if(value instanceof DoubleValue) {
            return (ButtonValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
        }
        return (ButtonValue) super.v(v -> true);
    }

    public ButtonValue c(Value value, double max){
        if(value instanceof IntValue) {
            return (ButtonValue) super.v(v -> ((IntValue)value).getValue() <= max);
        }
        if(value instanceof FloatValue) {
            return (ButtonValue) super.v(v -> ((FloatValue)value).getValue() <= max);
        }
        if(value instanceof DoubleValue) {
            return (ButtonValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
        }
        return (ButtonValue) super.v(v -> true);
    }

    public ButtonValue m(ModeValue value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

}
