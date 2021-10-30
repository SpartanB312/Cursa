package net.spartanb312.cursa.core.concurrent.utils;

import static java.lang.Thread.sleep;

/**
 * Created by B_312 on 05/05/2021
 * To avoid busy waiting warning
 */
public class ThreadUtil {

    public static void delay(){
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
