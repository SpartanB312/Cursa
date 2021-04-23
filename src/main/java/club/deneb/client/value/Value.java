package club.deneb.client.value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class Value<T> {

    private T value;
    private final T defaultValue;
    private Number minimum, maximum;
    List<BooleanSupplier> visibility = new ArrayList<>();
    private final String name;
    public List<T> modes = new ArrayList<>();
    public String description = "";

    public Value(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility.add(() -> true);
        setup();
    }

    public Value(String name, T defaultValue, Number minimum, Number maximum) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.minimum = minimum;
        this.maximum = maximum;
        setup();
    }

    public Value(String name, T defaultValue,List<T> modes) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.modes = modes;
        setup();
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Number getMin(){
        return minimum;
    }

    public Number getMax(){
        return maximum;
    }

    public boolean visible() {
        for(BooleanSupplier booleanSupplier : visibility){
            if(!booleanSupplier.getAsBoolean()) return false;
        }
        return true;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean toggled(T value){
        return this.value.equals(value);
    }

    public Value<T> v(BooleanSupplier booleanSupplier) {
        this.visibility.add(booleanSupplier);
        return this;
    }

    public Value<T> des(String description) {
        this.description = description;
        return this;
    }

    public Value<T> b(Value<Boolean> booleanValue) {
        this.visibility.add(booleanValue::getValue);
        return this;
    }

    public Value<T> r(Value<Boolean> booleanValue) {
        this.visibility.add(() -> !booleanValue.getValue());
        return this;
    }

    public Value<T> m(Value<String> modeValue,String mode) {
        this.visibility.add(() -> modeValue.getValue().equals(mode));
        return this;
    }

    public void reset(){
        this.value = defaultValue;
    }

    private void setup(){
        if(modes.contains(defaultValue)) modes.add(defaultValue);
    }

    public void forwardLoop(){
        int index = modes.indexOf(value);
        if(index != modes.size() -1) {
            value = modes.get(index + 1);
        } else {
            value = modes.get(0);
        }
    }

}
