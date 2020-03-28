package org.redrune.core.tools.function

import io.github.classgraph.ClassGraph
import org.redrune.core.network.codec.Codec

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 03, 2020
 */
class FileFunction {

    companion object {

        val RESULT = ClassGraph().enableClassInfo().scan()

        inline fun <reified T> getChildClassesOf(): ArrayList<Any> {
            val name = T::class.qualifiedName
            val list = RESULT.getSubclasses(name).loadClasses() as MutableList<Class<*>>?
            val classes = ArrayList<Any>()
            list?.forEach { classes.add(it.newInstance()) }
            return classes
        }

        inline fun <reified T : Codec> registerCodecs() {
            val encoders = getChildClassesOf<T>()
            for (clazz in encoders) {
                (clazz as T).register()
            }
        }

    }

}