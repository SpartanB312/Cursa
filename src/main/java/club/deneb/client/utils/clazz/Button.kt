package club.deneb.client.utils.clazz

import java.util.*
import java.util.function.Consumer

class Button {

    private var binds: MutableList<VoidContainer> = ArrayList()
    fun runBinds() {
        binds.forEach(Consumer { obj: VoidContainer -> obj.invoke() })
    }

    fun setBind(voidContainer: VoidContainer): Button {
        binds.add(voidContainer)
        return this
    }
}