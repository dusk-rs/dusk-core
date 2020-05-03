package rs.dusk.core.network.codec

import rs.dusk.core.network.codec.message.MessageDecoder
import rs.dusk.core.network.codec.message.MessageEncoder
import rs.dusk.core.network.codec.message.MessageHandler
import rs.dusk.core.network.model.message.Message
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class Codec : CodecComponents() {

    /**
     * The registration of all [components][CodecComponents] of this codec must be done here.
     *
     */
    abstract fun register()

    fun decoder(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Message> handler(clazz: KClass<M>): MessageHandler<M>? {
        return handlers[clazz] as? MessageHandler<M>
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Message> encoder(clazz: KClass<M>): MessageEncoder<M>? {
        return encoders[clazz] as? MessageEncoder<M>
    }

}