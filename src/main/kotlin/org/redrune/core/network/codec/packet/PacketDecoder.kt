package org.redrune.core.network.codec.packet

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.redrune.core.network.codec.packet.DecoderState.*
import org.redrune.core.network.codec.packet.access.PacketReader
import org.redrune.core.network.model.packet.PacketType
import org.redrune.core.network.model.packet.PacketType.*

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class PacketDecoder : ByteToMessageDecoder() {

    private val logger = InlineLogger()

    /**
     * The current state of the decoder
     */
    private var state: DecoderState = DECODE_OPCODE

    /**
     * The read opcode of the packet
     */
    protected var opcode = -1

    /**
     * The expected length of the packet
     */
    protected var length = -1

    /**
     * The type of packet
     */
    private var type = FIXED

    /**
     * Handles reading the opcode from the buffer
     */
    abstract fun readOpcode(buf: ByteBuf): Int

    /**
     * Getting the expected length of a buffer by the opcode identified [opcode]. If th
     */
    abstract fun getExpectedLength(opcode: Int): Int?

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (state == DECODE_OPCODE) {
            if (!buf.isReadable) {
                logger.warn { "Unable to decode opcode from buffer - buffer is not readable." }
                return
            }
            opcode = readOpcode(buf)
            length = getExpectedLength(opcode) ?: buf.readableBytes()
            type = PacketType.byLength(length)
            state = if (length < 0) DECODE_LENGTH else DECODE_PAYLOAD
            logger.info { "Identified opcode! [opcode=$opcode, expectedLength=$length, readable=${buf.readableBytes()}, nextState=$state]" }
        }
        if (state == DECODE_LENGTH) {
            if (buf.readableBytes() < if (length == -1) 1 else 2) {
                logger.warn { "Unable to decode length from buffer [opcode=$opcode] - buffer is not readable [readable=${buf.readableBytes()}]." }
                return
            }
            // when the packet is of a variable length, the expected length is overwritten by the length encoded next
            length = when (type) {
                BYTE -> buf.readUnsignedByte().toInt()
                SHORT -> buf.readUnsignedShort()
                else -> throw IllegalStateException("Decoding length from packet #$opcode with type $type!")
            }
            logger.info { "Identified length! [opcode=$opcode, length=$length, type=$type]" }
            state = DECODE_PAYLOAD
        }
        if (state == DECODE_PAYLOAD) {
            if (buf.readableBytes() < length) {
                logger.warn { "Unable to decode payload from buffer - length=$length, readable=${buf.readableBytes()}." }
                return
            }

            //Copy from unsafe buffer
            val payload = buf.readBytes(length)

            //Handle data
            out.add(PacketReader(opcode, type, payload))

            //Reset state
            state = DECODE_OPCODE

            logger.info { "Finished and pushed. remaining readable = ${buf.readableBytes()} [opcode=$opcode] " }
        }
    }

}