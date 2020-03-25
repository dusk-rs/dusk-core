package org.redrune.core.network.model.packet.codec.impl

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import org.redrune.core.network.model.codec.Codec
import org.redrune.core.network.model.packet.codec.PacketDecoder

/**
 * Handling the decoding of a simple packet, which is a packet whose contents are [opcode, buffer]
 *
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class SimplePacketDecoder(private val codec: Codec) : PacketDecoder() {

    private val logger = InlineLogger()

    override fun readOpcode(buf: ByteBuf): Int {
        return buf.readUnsignedByte().toInt()
    }

    override fun getExpectedLength(opcode: Int): Int? {
        val decoder = codec.decoder(opcode)
        if (decoder == null) {
            logger.warn { "Unable to identify length of packet [opcode=$opcode, codec=${codec.javaClass.simpleName}]" }
            return null
        }
        return decoder.length
    }
}