package com.deneb.client.features.modules.client;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;

@Module.Info(name = "Notification",category = Category.CLIENT,visible = false)
public class Notification extends Module {

    public static Notification INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
    }

    public BValue chat = setting("Chat",true);


}
