package club.deneb.client.utils

import org.lwjgl.input.Keyboard

object Wrapper {
    @JvmStatic
    fun getKey(keyName: String): Int {
        return Keyboard.getKeyIndex(keyName.toUpperCase())
    }
}