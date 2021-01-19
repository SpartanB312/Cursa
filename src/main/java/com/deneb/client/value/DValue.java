package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class DValue extends Value<Double> {

    protected Double min, max;

    public DValue(String name, Double defaultValue, Double min, Double max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public DValue v(Predicate<Object> predicate) {
        return (DValue) super.v(predicate);
    }

    public DValue page(PageValue.Page page) {
        return (DValue) super.v(page.p());
    }

    public DValue b(BValue value) {
        return (DValue) super.v(v -> value.getValue());
    }

    public DValue r(BValue value) {
        return (DValue) super.v(v -> !value.getValue());
    }

    public DValue c(double min,Value value,double max){
        if(value instanceof IValue) {
            return (DValue) super.v(v -> ((IValue)value).getValue() <= max && ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (DValue) super.v(v -> ((FValue)value).getValue() <= max && ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (DValue) super.v(v -> ((DValue)value).getValue() <= max && ((DValue)value).getValue() >= min);
        }
        return (DValue) super.v(v -> true);
    }

    public DValue c(double min,Value value){
        if(value instanceof IValue) {
            return (DValue) super.v(v -> ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (DValue) super.v(v -> ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (DValue) super.v(v -> ((DValue)value).getValue() >= min);
        }
        return (DValue) super.v(v -> true);
    }

    public DValue c(Value value,double max){
        if(value instanceof IValue) {
            return (DValue) super.v(v -> ((IValue)value).getValue() <= max);
        }
        if(value instanceof FValue) {
            return (DValue) super.v(v -> ((FValue)value).getValue() <= max);
        }
        if(value instanceof DValue) {
            return (DValue) super.v(v -> ((DValue)value).getValue() <= max);
        }
        return (DValue) super.v(v -> true);
    }

    public DValue m(MValue value,String mode){
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
