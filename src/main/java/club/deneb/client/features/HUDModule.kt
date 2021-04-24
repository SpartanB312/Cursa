package club.deneb.client.features

/**
 * Created by B_312 on 01/10/21
 */
open class HUDModule : AbstractModule() {

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class Info(
        val name: String,
        val x: Int = 0,
        val y: Int = 0,
        val width: Int = 0,
        val height: Int = 0,
        val category: Category = Category.HUD,
        val description: String = "",
        val visible: Boolean = true
    )

    private val annotation: Info
        get() {
            if (javaClass.isAnnotationPresent(Info::class.java)) {
                return javaClass.getAnnotation(Info::class.java)
            }
            throw IllegalStateException("No Annotation on class " + this.javaClass.canonicalName + "!")
        }

    open fun onDragging(mouseX: Int, mouseY: Int) {}
    open fun onMouseRelease() {}

    init {
        x = annotation.x
        y = annotation.y
        width = annotation.width
        height = annotation.height
        name = annotation.name
        this.category = annotation.category
        description = annotation.description
        isHUD = true
    }
}