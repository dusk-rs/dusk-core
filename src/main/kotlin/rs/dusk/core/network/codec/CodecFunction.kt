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
interface CodecFunction {

    /**
     * Finds a decoder by its opcode
     */
    fun decoder(opcode: Int): MessageDecoder<*>?

    /**
     * Finds the handler of a message
     * @param clazz KClass<M>
     * @return MessageHandler<M>?
     */
    fun <M : Message> handler(clazz: KClass<M>): MessageHandler<M>?

    /**
     * Finds the encoder of a message
     * @param clazz KClass<M>
     * @return MessageEncoder<M>?
     */
    fun <M : Message> encoder(clazz: KClass<M>): MessageEncoder<M>?

}
