package rs.dusk.core.network.codec

import com.github.michaelbull.logging.InlineLogger
import com.google.common.base.Stopwatch
import rs.dusk.core.utility.ReflectionUtils
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.math.log
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
class CodecRepository {

	private val logger = InlineLogger()

	/**
	 * The collection of all [codecs][Codec], identifiable by class name
	 */
	private val map = HashMap<KClass<*>, Codec>()
	
	/**
	 * A flag for registration, to avoid duplicate [registration][registerAll]
	 */
	private var registered = false

	init {
		registerAll()
	}
	
	/**
	 * The registration of all [codecs][Codec] is done here using reflection
	 */
	private fun registerAll() {
		if (registered) {
			logger.warn { "Attempt to registered all codec components failed, already complete! "}
			return
		}
		val stopwatch = Stopwatch.createStarted()
		val codecs = ReflectionUtils.findSubclasses<Codec>()
		val information = StringBuilder()
		val iterator = codecs.iterator()
		while (iterator.hasNext()) {
			val codec = iterator.next()
			codec.register()
			map[codec.javaClass.kotlin] = codec
			information.append(codec.generateStatistics() + (if (iterator.hasNext()) ", " else ""))
		}
		logger.info { "Successfully registered ${codecs.size} codecs successfully in ${stopwatch.elapsed(MILLISECONDS)} ms" }
		logger.info { "Statistics[decoders, handlers, encoders]:\t$information" }
		registered = true
	}
	
	/**
	 * Gets a [codec][Codec] from the [codec map][map]
	 */
	fun get(clazz : KClass<*>) : Codec {
		if (map.containsKey(clazz)) {
			return map[clazz]!!
		} else {
			throw IllegalStateException("Unable to find codec from class [$clazz]")
		}
	}
	
}