package club.deneb.client.features.modules.client;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;

@Module.Info(name = "Notification",category = Category.CLIENT,visible = false)
public class Notification extends Module {

    public static Notification INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
    }

    public BooleanValue chat = setting("Chat",true);


}
