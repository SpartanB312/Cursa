package club.deneb.client.features.modules.client;

import club.deneb.client.Deneb;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.clazz.Button;
import club.deneb.client.value.Value;

@Module.Info(name = "IRC",category = Category.CLIENT,visible = false)
public class IRC extends Module {

    public Value<Button> reconnect = setting("Reconnect",new Button().setBind(this::tryReconnect)).des("Click to reconnect IRC");
    public Value<Boolean> enable = setting("EnableIRC",true);

    static IRC INSTANCE;

    public void tryReconnect(){
        Deneb.getINSTANCE().getIrcManager().reconnect();
    }

    public static IRC getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
