package com.deneb.client.gui.component;

import com.deneb.client.value.MValue;
import com.deneb.client.value.Value;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class ValueButton extends Component {
    private Value value;
    public Value getValue(){
        return value;
    }

    public MValue getAsModeValue(){
        return (MValue) value;
    }
    public void setValue(Value value){
        this.value = value;
    }
}
