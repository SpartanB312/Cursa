package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class IValue extends Value<Integer>{
    protected Integer min, max;

    public IValue(String name, Integer defaultValue, Integer min, Integer max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public IValue v(Predicate<Object> predicate) {
        return (IValue) super.v(predicate);
    }

    public IValue page(PageValue.Page page) {
        return (IValue) super.v(page.p());
    }

    public IValue b(BValue value) {
        return (IValue) super.v(v -> value.getValue());
    }

    public IValue r(BValue value) {
        return (IValue) super.v(v -> !value.getValue());
    }

    public IValue c(double min,Value value,double max){
        if(value instanceof IValue) {
            return (IValue) super.v(v -> ((IValue)value).getValue() <= max && ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (IValue) super.v(v -> ((FValue)value).getValue() <= max && ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (IValue) super.v(v -> ((DValue)value).getValue() <= max && ((DValue)value).getValue() >= min);
        }
        return (IValue) super.v(v -> true);
    }

    public IValue c(double min,Value value){
        if(value instanceof IValue) {
            return (IValue) super.v(v -> ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (IValue) super.v(v -> ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (IValue) super.v(v -> ((DValue)value).getValue() >= min);
        }
        return (IValue) super.v(v -> true);
    }

    public IValue c(Value value,double max){
        if(value instanceof IValue) {
            return (IValue) super.v(v -> ((IValue)value).getValue() <= max);
        }
        if(value instanceof FValue) {
            return (IValue) super.v(v -> ((FValue)value).getValue() <= max);
        }
        if(value instanceof DValue) {
            return (IValue) super.v(v -> ((DValue)value).getValue() <= max);
        }
        return (IValue) super.v(v -> true);
    }

    public Integer getMin(){
        return min;
    }

    public Integer getMax() {
        return max;
    }
}