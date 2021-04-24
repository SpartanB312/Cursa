package club.deneb.client.command

import club.deneb.client.utils.Wrapper

/**
 * Created by B_312 on 01/15/21
 */
open class Command {
    val command: String
    val description: String

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class Info(val command: String, val description: String = "")

    private val annotation: Info
        get() {
            if (javaClass.isAnnotationPresent(Info::class.java)) {
                return javaClass.getAnnotation(Info::class.java)
            }
            throw IllegalStateException("No Annotation on class " + this.javaClass.canonicalName + "!")
        }

    open fun onCall(s: String, vararg args: String){}

    companion object {
        val mc = Wrapper.minecraft
    }

    init {
        command = annotation.command
        description = annotation.description
    }

    open fun getSyntax(): String {
        return ""
    }
}