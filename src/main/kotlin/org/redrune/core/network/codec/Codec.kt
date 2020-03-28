package org.redrune.core.network.codec

import com.github.michaelbull.logging.InlineLogger
import org.redrune.core.network.codec.message.MessageDecoder
import org.redrune.core.network.codec.message.MessageEncoder
import org.redrune.core.network.codec.message.MessageHandler
import org.redrune.core.network.model.message.Message
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class Codec : CodecRepository(), CodecFunction {

    /**
     * The registration of the codec components must be explicitly done here, due to the requirement of type specification
     */
    abstract fun register()

    private val logger = InlineLogger()

    fun report() {
        logger.info { "${this.javaClass.simpleName} information - [decoders=${decoders.size}, handlers=${handlers.size}, encoders=${encoders.size}]" }
    }

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

}