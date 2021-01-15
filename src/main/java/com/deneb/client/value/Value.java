package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class Value<T> {

    private T value;
    private final T defaultValue;
    Predicate<T> visibility;
    private final String name;

    public Value(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility = v -> true;
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean visible() {
        return this.visibility.test(getValue());
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Value<T> visibility(Predicate<T> predicate) {
        this.visibility = predicate;
        return this;
    }

}
