package org.redrune.core.network.codec.message.encode

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.redrune.core.network.codec.Codec
import org.redrune.core.network.codec.message.MessageEncoder
import org.redrune.core.network.codec.packet.access.PacketBuilder
import org.redrune.core.network.model.message.Message

/**
 * Encoder for writing messages to byte data
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
@ChannelHandler.Sharable
class GenericMessageEncoder(private val codec: Codec, private val builder: PacketBuilder = PacketBuilder()) : MessageToByteEncoder<Message>() {

    private val logger = InlineLogger()

    @Suppress("UNCHECKED_CAST")
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        val encoder = codec.encoder(msg::class) as? MessageEncoder<Message>
        if (encoder == null) {
            logger.warn { "Unable to find encoder! [msg=$msg, codec=${codec.javaClass.simpleName}]" }
            return
        }
        builder.build(out) {
            encoder.encode(it, msg)
        }
        logger.info { "Encoding successful [encoder=${encoder.javaClass.simpleName}, msg=$msg, codec=${codec.javaClass.simpleName}" }
    }

}
