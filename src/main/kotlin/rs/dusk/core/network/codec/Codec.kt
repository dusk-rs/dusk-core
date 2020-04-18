package rs.dusk.core.network.codec

import com.github.michaelbull.logging.InlineLogger
import rs.dusk.core.network.codec.message.MessageDecoder
import rs.dusk.core.network.codec.message.MessageEncoder
import rs.dusk.core.network.codec.message.MessageHandler
import rs.dusk.core.network.model.message.Message
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class Codec : CodecRepository() {

    private val logger = InlineLogger()

    /**
     * All components of the codec must be registered with utilization of the functions in [CodecFunction]
     */
    abstract fun register()

    override fun decoder(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }

    @Suppress("UNCHECKED_CAST")
    override fun <M : Message> handler(clazz: KClass<M>): MessageHandler<M>? {
        return handlers[clazz] as? MessageHandler<M>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <M : Message> encoder(clazz: KClass<M>): MessageEncoder<M>? {
        return encoders[clazz] as? MessageEncoder<M>
    }

    fun report() {
        logger.info { "${this.javaClass.simpleName} information - [decoders=${decoders.size}, handlers=${handlers.size}, encoders=${encoders.size}]" }
    }

}