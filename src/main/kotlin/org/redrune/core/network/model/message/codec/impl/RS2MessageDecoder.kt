package org.redrune.core.network.model.message.codec.impl

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import org.redrune.core.network.codec.Codec
import org.redrune.core.network.model.packet.access.PacketReader

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
@ChannelHandler.Sharable
class RS2MessageDecoder(private val codec: Codec) : MessageToMessageDecoder<PacketReader>() {

    private val logger = InlineLogger()

    override fun decode(ctx: ChannelHandlerContext, msg: PacketReader, out: MutableList<Any>) {
        val decoder = codec.decoder(msg.opcode)
        if (decoder == null) {
            logger.warn { "Unable to find message decoder [msg=$msg, codec=${codec.javaClass.simpleName}]" }
            return
        }
        val message = decoder.decode(msg)
        out.add(message)
        logger.info { "Message decoding successful [decoder=${decoder.javaClass.simpleName}]"}
    }
}