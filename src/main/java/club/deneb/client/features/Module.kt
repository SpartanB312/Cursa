package club.deneb.client.features

import club.deneb.client.client.GuiManager
import club.deneb.client.utils.clazz.Button
import club.deneb.client.utils.clazz.VoidContainer
import club.deneb.client.value.ButtonValue
import club.deneb.client.value.StringMode
import club.deneb.client.value.Value
import org.lwjgl.input.Keyboard

open class Module : AbstractModule() {

    private var visibleValue: Value<String>
    private var resetConfig: Value<Button>

    val isShownOnArray: Boolean
        get() = visibleValue.toggled("ON")

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class Info(
        val name: String,
        val description: String = "",
        val keyCode: Int = Keyboard.KEY_NONE,
        val category: Category,
        val visible: Boolean = true
    )

    private val annotation: Info
        get() {
            if (javaClass.isAnnotationPresent(Info::class.java)) {
                return javaClass.getAnnotation(Info::class.java)
            }
            throw IllegalStateException("No Annotation on class " + this.javaClass.canonicalName + "!")
        }

    init {
        name = annotation.name
        this.category = annotation.category
        description = annotation.description
        keyCode = annotation.keyCode
        values.add(
            StringMode("Visible", if (annotation.visible) "ON" else "OFF", listOf("ON", "OFF")).v(GuiManager::visibleButton).also { visibleValue = it })
        values.add(
            ButtonValue("LoadDefault", Button().setBind(object : VoidContainer {
                override fun invoke() {
                    reset()
                }
            })).des("Click here to reset this module").v(GuiManager::resetButton).also { resetConfig = it })
        isHUD = false
    }
}