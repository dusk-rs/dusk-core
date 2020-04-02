package org.redrune.core.network.codec

import org.redrune.core.network.codec.message.MessageDecoder
import org.redrune.core.network.codec.message.MessageEncoder
import org.redrune.core.network.codec.message.MessageHandler
import org.redrune.core.network.model.packet.PacketMetaData
import org.redrune.core.tools.function.NetworkUtils
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class CodecRepository {

    /**
     * The map of decoders, which are of type D, and are specified by the opcode of the message they are handling
     */
    protected val decoders = HashMap<Int, MessageDecoder<*>>()

    /**
     * The map of handlers, which are specified by the class they are handling (a subclass of [Message])
     */
    protected val handlers = HashMap<KClass<*>, MessageHandler<*>>()

    /**
     * The map of message encoders, which are specified by the class they are handling (a subclass of [Message])
     */
    protected val encoders = HashMap<KClass<*>, MessageEncoder<*>>()

    /**
     *
     */
    protected inline fun <reified T : MessageDecoder<*>> bindDecoders() {
        val decoders = NetworkUtils.getCodecEntry<T>()
        for (clazz in decoders) {
            if (!clazz.javaClass.isAnnotationPresent(PacketMetaData::class.java)) {
                System.err.println("Unable to register decoder ${clazz.javaClass.name}, no meta data defined!")
                continue
            }
            val metaData = clazz.javaClass.getDeclaredAnnotation(PacketMetaData::class.java)
            val decoder = clazz as T
            decoder.opcodes = metaData.opcodes
            decoder.length = metaData.length
            bindDecoder(decoder)
        }
    }

    protected inline fun <reified T : MessageHandler<*>> bindHandlers() {
        val handlers = NetworkUtils.getCodecEntry<T>()
        for (clazz in handlers) {
            val handler = clazz as T
            val type: KClass<*> = handler.getGenericTypeClass()
            bindHandler(type, handler)
        }
    }

    protected inline fun <reified T : MessageEncoder<*>> bindEncoders() {
        val encoders = NetworkUtils.getCodecEntry<T>()
        for (clazz in encoders) {
            val encoder = clazz as T
            val type: KClass<*> = encoder.getGenericTypeClass()
            bindEncoder(type, encoder)
        }
    }

    protected fun bindDecoder(decoder: MessageDecoder<*>) {
        decoder.opcodes?.forEach { opcode ->
            if (decoders[opcode] != null) {
                throw IllegalArgumentException("Cannot have duplicate decoders $decoder $opcode")
            }
            decoders[opcode] = decoder
        }
    }

    protected fun bindEncoder(type: KClass<*>, encoder: MessageEncoder<*>) {
        if (encoders.contains(type)) {
            throw IllegalArgumentException("Cannot have duplicate encoders $type $encoder")
        }
        encoders[type] = encoder
    }

    protected fun bindHandler(type: KClass<*>, encoder: MessageHandler<*>) {
        if (handlers.contains(type)) {
            throw IllegalArgumentException("Cannot have duplicate handlers $type $encoder")
        }
        handlers[type] = encoder
    }

}