package rs.dusk.core.utility

import io.github.classgraph.ClassGraph
import java.util.*

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
class ReflectionUtils {
	
	companion object {
		
		val result = ClassGraph().enableClassInfo().scan()
		
		/**
		 * Finds all subclasses of the parameterized type, and stores them into the returned list
		 */
		inline fun <reified T> findSubclasses() : ArrayList<T> {
			val name = T::class.qualifiedName
			val list = result.getSubclasses(name).loadClasses() as MutableList<Class<*>>?
			val classes = ArrayList<T>()
			list?.forEach { classes.add(it.newInstance() as T) }
			return classes
		}
		
	}
}