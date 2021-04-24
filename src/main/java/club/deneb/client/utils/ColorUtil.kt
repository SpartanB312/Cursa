package club.deneb.client.utils

import java.awt.Color

object ColorUtil {
    fun getColor(hex: Int): Color {
        return Color(hex)
    }

    fun getRed(hex: Int): Int {
        return hex shr 16 and 255
    }

    fun getGreen(hex: Int): Int {
        return hex shr 8 and 255
    }

    fun getBlue(hex: Int): Int {
        return hex and 255
    }

    @JvmStatic
    fun getHoovered(color: Int, isHoovered: Boolean): Int {
        return if (isHoovered) color and 0x7F7F7F shl 1 else color
    }

}