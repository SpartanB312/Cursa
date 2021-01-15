package com.deneb.client.gui.component;

import com.deneb.client.value.MValue;
import com.deneb.client.value.Value;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class ValueButton<T> extends Component {
    private Value<T> value;
    public Value<T> getValue(){
        return value;
    }

    public MValue getAsModeValue(){
        return (MValue) value;
    }
    public void setValue(Value<T> value){
        this.value = value;
    }
}
