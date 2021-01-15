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

    public BValue visibility(Predicate<Boolean> predicate) {
        return (BValue) super.visibility(predicate);
    }

}
