package club.deneb.client.gui.component;

import club.deneb.client.value.ModeValue;
import club.deneb.client.value.Value;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class ValueButton<T> extends Component {
    private Value<T> value;
    public Value<T> getValue(){
        return value;
    }

    public ModeValue getAsModeValue(){
        return (ModeValue) value;
    }
    public void setValue(Value<T> value){
        this.value = value;
    }
}
