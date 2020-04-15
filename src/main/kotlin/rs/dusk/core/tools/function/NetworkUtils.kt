package rs.dusk.core.tools.function

import io.github.classgraph.ClassGraph
import rs.dusk.core.network.codec.Codec

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 03, 2020
 */
class NetworkUtils {

    companion object {

        val RESULT = ClassGraph().enableClassInfo().scan()

        inline fun <reified T> getCodecEntry(): ArrayList<Any> {
            val name = T::class.qualifiedName
            val list = RESULT.getSubclasses(name).loadClasses() as MutableList<Class<*>>?
            val classes = ArrayList<Any>()
            list?.forEach { classes.add(it.newInstance()) }
            return classes
        }

        fun loadCodecs(vararg codecs: Codec) {
            for (codec in codecs) {
                codec.register()
                codec.report()
            }
        }
    }

}