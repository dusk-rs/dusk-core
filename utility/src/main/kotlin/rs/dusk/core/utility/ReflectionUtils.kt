package rs.dusk.core.utility

import io.github.classgraph.ClassGraph
import java.util.*

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
class ReflectionUtils {

    companion object {

        val RESULT = ClassGraph().enableClassInfo().scan()

        inline fun <reified T> findSubclasses(): ArrayList<Any> {
            val name = T::class.qualifiedName
            val list = RESULT.getSubclasses(name).loadClasses() as MutableList<Class<*>>?
            val classes = ArrayList<Any>()
            list?.forEach { classes.add(it.newInstance()) }
            return classes
        }

    }
}