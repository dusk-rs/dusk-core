package org.redrune.core.network.model.message.codec.impl

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.redrune.core.network.codec.Codec
import org.redrune.core.network.model.message.Message
import org.redrune.core.network.model.message.codec.MessageEncoder
import org.redrune.core.network.model.packet.access.PacketBuilder
import org.redrune.core.tools.crypto.IsaacCipher

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
@ChannelHandler.Sharable
class RS2MessageEncoder(private val codec: Codec, private val cipher: IsaacCipher) : MessageToByteEncoder<Message>() {

    private val logger = InlineLogger()

    @Suppress("UNCHECKED_CAST")
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        val encoder = codec.encoder(msg::class) as? MessageEncoder<Message>
        if (encoder == null) {
            logger.warn { "Unable to find encoder! [msg=$msg]" }
            return
        }
        val builder =
            PacketBuilder(buffer = out, cipher = cipher)
        encoder.encode(builder, msg)
        builder.writeSize()
        logger.info { "Encoding successful [encoder=${encoder.javaClass.simpleName}, msg=$msg, codec=${codec.javaClass.simpleName}" }
    }

}
