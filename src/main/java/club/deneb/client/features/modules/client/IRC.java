package club.deneb.client.features.modules.client;

import club.deneb.client.Deneb;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.ButtonValue;

@Module.Info(name = "IRC",category = Category.CLIENT,visible = false)
public class IRC extends Module {

    public ButtonValue reconnect = setting("Reconnect","Click to reconnect IRC").bind(v -> tryReconnect());
    public BooleanValue enable = setting("EnableIRC",true);

    static IRC INSTANCE;

    public boolean tryReconnect(){
        Deneb.getINSTANCE().getIrcManager().reconnect();
        return true;
    }

    public static IRC getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
