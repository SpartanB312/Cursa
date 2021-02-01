package club.deneb.client.value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class Value<T> {

    private T value;
    private final T defaultValue;
    List<Predicate<Object>> visibility = new ArrayList<>();
    private final String name;

    public Value(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility.add(V -> true);
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean visible() {
        for(Predicate<Object> predicate : visibility){
            if(!predicate.test(this)) return false;
        }
        return true;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Value<T> v(Predicate<Object> predicate) {
        this.visibility.add(predicate);
        return this;
    }

}
