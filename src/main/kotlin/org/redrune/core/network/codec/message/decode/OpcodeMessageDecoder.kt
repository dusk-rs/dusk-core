package org.redrune.core.network.codec.message.decode

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import org.redrune.core.network.codec.Codec
import org.redrune.core.network.codec.packet.access.PacketReader

/**
 * Packets that are identified with an opcode must be decoded into a [Message] with this decoder
 *
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
@ChannelHandler.Sharable
class OpcodeMessageDecoder(private val codec: Codec) : MessageToMessageDecoder<PacketReader>() {

    private val logger = InlineLogger()

    override fun decode(ctx: ChannelHandlerContext, msg: PacketReader, out: MutableList<Any>) {
        val decoder = codec.decoder(msg.opcode)
        if (decoder == null) {
            logger.warn { "Unable to find message decoder [msg=$msg, codec=${codec.javaClass.simpleName}, codec=$codec]" }
            return
        }
        val message = decoder.decode(msg)
        out.add(message)
        logger.info { "Message decoding successful [decoder=${decoder.javaClass.simpleName}, codec=$codec]"}
    }
}