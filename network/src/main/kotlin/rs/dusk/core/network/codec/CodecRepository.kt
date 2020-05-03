package rs.dusk.core.network.codec

import rs.dusk.core.network.codec.message.MessageHandler
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
object CodecRepository {

    /**
     * The collection of all [codecs][Codec], identifiable by class name
     */
    private val codecs = HashMap<KClass<*>, MessageHandler<*>>()

    fun registerAll() {

    }
}