@file:Suppress("UNCHECKED_CAST")

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @author Tyluur <itstyluur@gmail.com>
 * @Since December 17, 2020
 */
inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = getKoin().get(qualifier, parameters)

fun <T> getPropertyOrNull(key: String): T? = getKoin().getProperty(key) as T

fun <T> getProperty(key: String): T = getPropertyOrNull(key)!!

fun <T> getProperty(key: String, defaultValue: T): T = (getKoin().getProperty(key) ?: defaultValue) as T
