package rs.dusk.core.network.codec.packet.access

import rs.dusk.core.io.read.BufferReader
import java.nio.ByteBuffer

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class PacketReader(val opcode: Int = -1, buffer: ByteBuffer) : BufferReader(buffer) {

    constructor(opcode: Int = -1, byteArray: ByteArray) : this(opcode, ByteBuffer.wrap(byteArray))

    override fun toString(): String {
        return "PacketReader[opcode=$opcode, buffer=$buffer]"
    }

}