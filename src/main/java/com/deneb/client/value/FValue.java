package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class FValue extends Value<Float>{
    protected Float min, max;

    public FValue(String name, Float defaultValue, Float min, Float max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public FValue v(Predicate<Object> predicate) {
        return (FValue) super.v(predicate);
    }

    public FValue page(PageValue.Page page) {
        return (FValue) super.v(page.p());
    }

    public FValue b(BValue value) {
        return (FValue) super.v(v -> value.getValue());
    }

    public FValue r(BValue value) {
        return (FValue) super.v(v -> !value.getValue());
    }

    public FValue c(double min,Value value,double max){
        if(value instanceof IValue) {
            return (FValue) super.v(v -> ((IValue)value).getValue() <= max && ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (FValue) super.v(v -> ((FValue)value).getValue() <= max && ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (FValue) super.v(v -> ((DValue)value).getValue() <= max && ((DValue)value).getValue() >= min);
        }
        return (FValue) super.v(v -> true);
    }

    public FValue c(double min,Value value){
        if(value instanceof IValue) {
            return (FValue) super.v(v -> ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (FValue) super.v(v -> ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (FValue) super.v(v -> ((DValue)value).getValue() >= min);
        }
        return (FValue) super.v(v -> true);
    }

    public FValue c(Value value,double max){
        if(value instanceof IValue) {
            return (FValue) super.v(v -> ((IValue)value).getValue() <= max);
        }
        if(value instanceof FValue) {
            return (FValue) super.v(v -> ((FValue)value).getValue() <= max);
        }
        if(value instanceof DValue) {
            return (FValue) super.v(v -> ((DValue)value).getValue() <= max);
        }
        return (FValue) super.v(v -> true);
    }

    public FValue m(MValue value,String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() { return max; }
}
