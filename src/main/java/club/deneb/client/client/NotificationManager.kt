package club.deneb.client.client

import java.util.concurrent.CopyOnWriteArrayList
import club.deneb.client.features.AbstractModule
import club.deneb.client.utils.Timer

/**
 * Author B_312 on 01/15/2021
 */
object NotificationManager {

    val notificationUnitsList: MutableList<NotificationUnit> = CopyOnWriteArrayList()

    class NotificationUnit(var timer: Timer, var module: AbstractModule, var enabled: Boolean)

    @JvmStatic
    fun addNewNotification(module: AbstractModule, enabled: Boolean) {
        val tempTimer = Timer()
        tempTimer.reset()
        notificationUnitsList.add(NotificationUnit(tempTimer, module, enabled))
    }

}