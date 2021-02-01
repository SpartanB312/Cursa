package club.deneb.client.client;

import club.deneb.client.features.IModule;
import club.deneb.client.utils.Timer;
;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author B_312 on 01/15/2021
 */
public class NotificationManager {

    public static NotificationManager INSTANCE;

    private final List<NotificationUnit> notificationUnitsList = new CopyOnWriteArrayList<>();

    public static class NotificationUnit{
        public Timer timer;
        public IModule module;
        public boolean enabled;

        public NotificationUnit(Timer timer,IModule module,boolean enabled){
            this.module = module;
            this.timer = timer;
            this.enabled = enabled;
        }
    }

    public NotificationManager(){
        INSTANCE = this;
    }

    public static void addNewNotification(IModule module,boolean enabled){
        Timer tempTimer = new Timer();
        tempTimer.reset();
        INSTANCE.notificationUnitsList.add(new NotificationUnit(tempTimer,module,enabled));
    }

    public static List<NotificationUnit> getUnits(){
        return INSTANCE.notificationUnitsList;
    }



}
