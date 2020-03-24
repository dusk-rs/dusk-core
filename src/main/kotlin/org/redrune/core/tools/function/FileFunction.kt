package org.redrune.core.tools.function

import io.github.classgraph.ClassGraph

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

    }

}