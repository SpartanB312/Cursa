package club.deneb.client.utils.clazz;


import java.util.ArrayList;
import java.util.List;

public class Button {
    public List<VoidContainer> binds = new ArrayList<>();

    public void runBinds(){
        binds.forEach(VoidContainer::invoke);
    }

    public Button setBind(VoidContainer voidContainer){
        binds.add(voidContainer);
        return this;
    }
}
