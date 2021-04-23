package club.deneb.client.value

import club.deneb.client.utils.clazz.Button
import java.util.function.BooleanSupplier

open class ModeValue<T>(name: String, defaultValue: T, var modes: MutableList<T> = ArrayList()) : Value<T>(name, defaultValue) {

    init {
        if (!modes.contains(defaultValue)) modes.add(defaultValue)
    }

    override fun v(booleanSupplier: BooleanSupplier): ModeValue<T> {
        visibility.add(booleanSupplier)
        return this
    }

    override fun des(description: String): ModeValue<T> {
        this.description = description
        return this
    }

    override fun b(booleanValue: Value<Boolean>): ModeValue<T> {
        visibility.add(BooleanSupplier { booleanValue.value!! })
        return this
    }

    override fun r(booleanValue: Value<Boolean>): ModeValue<T> {
        visibility.add(BooleanSupplier { !booleanValue.value!! })
        return this
    }

    override fun m(modeValue: ModeValue<String>, mode: String): ModeValue<T> {
        visibility.add(BooleanSupplier { modeValue.value == mode })
        return this
    }

    fun getAsString(): String {
        return if (value is Enum<*>) {
            (value as Enum<*>).name
        } else {
            value.toString()
        }
    }

    fun setWithName(name:String){
        modes.forEach {
            if(it.toString().equals(name, ignoreCase = true)) value = it
        }
    }

    open fun forwardLoop() {
        val index = modes.indexOf(value)
        value = if (index != modes.size - 1) {
            modes[index + 1]
        } else {
            modes[0]
        }
    }
}


@Suppress("UNCHECKED_CAST")
class EnumValue<T : Enum<T>>(name: String, defaultValue: Enum<*>) :
    ModeValue<T>(name, defaultValue as T,ArrayList()) {

    override fun v(booleanSupplier: BooleanSupplier): EnumValue<T> {
        visibility.add(booleanSupplier)
        return this
    }

    override fun des(description: String): EnumValue<T> {
        this.description = description
        return this
    }

    override fun b(booleanValue: Value<Boolean>): EnumValue<T> {
        visibility.add(BooleanSupplier { booleanValue.value })
        return this
    }

    override fun r(booleanValue: Value<Boolean>): EnumValue<T> {
        visibility.add(BooleanSupplier { !booleanValue.value })
        return this
    }

    override fun m(modeValue: ModeValue<String>, mode: String): EnumValue<T> {
        visibility.add(BooleanSupplier { modeValue.value == mode })
        return this
    }

    fun setEnumValue(value: String) {
        for (e in this.value.javaClass.enumConstants) {
            if (e.name.equals(value, ignoreCase = true)) {
                this.value = e
            }
        }
    }

    init {
        value.javaClass.enumConstants.forEach {
            if (it != null && !modes.contains(it)) modes.add(it)
        }
    }
}

class StringMode(name: String, defaultValue: String, modes: MutableList<String>):ModeValue<String>(name,defaultValue,modes)

class IntMode(name: String, defaultValue: Int, modes: MutableList<Int>):ModeValue<Int>(name,defaultValue,modes)

class IntValue(name: String,defaultValue: Int,min:Int,max:Int):Value<Int>(name,defaultValue,min,max)

class FloatValue(name: String,defaultValue: Float,min:Float,max:Float):Value<Float>(name,defaultValue,min,max)

class DoubleValue(name: String,defaultValue: Double,min:Double,max:Double):Value<Double>(name,defaultValue,min,max)

class ButtonValue(name: String,defaultValue: Button):Value<Button>(name,defaultValue)

class BooleanValue(name: String,defaultValue: Boolean):Value<Boolean>(name,defaultValue)

