package com.deneb.client.value;

import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class BValue extends Value<Boolean> {

    public BValue(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BValue v(Predicate<Object> predicate) {
        return (BValue) super.v(predicate);
    }

    public BValue page(PageValue.Page page) {
        return (BValue) super.v(page.p());
    }

    public BValue b(BValue value) {
        return (BValue) super.v(v -> value.getValue());
    }

    public BValue r(BValue value) {
        return (BValue) super.v(v -> !value.getValue());
    }

    public BValue c(double min,Value value,double max){
        if(value instanceof IValue) {
            return (BValue) super.v(v -> ((IValue)value).getValue() <= max && ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (BValue) super.v(v -> ((FValue)value).getValue() <= max && ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (BValue) super.v(v -> ((DValue)value).getValue() <= max && ((DValue)value).getValue() >= min);
        }
        return (BValue) super.v(v -> true);
    }

    public BValue c(double min,Value value){
        if(value instanceof IValue) {
            return (BValue) super.v(v -> ((IValue)value).getValue() >= min);
        }
        if(value instanceof FValue) {
            return (BValue) super.v(v -> ((FValue)value).getValue() >= min);
        }
        if(value instanceof DValue) {
            return (BValue) super.v(v -> ((DValue)value).getValue() >= min);
        }
        return (BValue) super.v(v -> true);
    }

    public BValue c(Value value,double max){
        if(value instanceof IValue) {
            return (BValue) super.v(v -> ((IValue)value).getValue() <= max);
        }
        if(value instanceof FValue) {
            return (BValue) super.v(v -> ((FValue)value).getValue() <= max);
        }
        if(value instanceof DValue) {
            return (BValue) super.v(v -> ((DValue)value).getValue() <= max);
        }
        return (BValue) super.v(v -> true);
    }

    public BValue m(MValue value,String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

}
