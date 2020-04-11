package org.redrune.core.network.codec.packet.access

import org.redrune.core.io.read.BufferReader
import java.nio.ByteBuffer

/**
 * @author Greg Hibb
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class PacketReader(val opcode: Int = -1, buffer: ByteBuffer) : BufferReader(buffer) {

    constructor(opcode: Int = -1, byteArray: ByteArray) : this(opcode, ByteBuffer.wrap(byteArray))

    override fun toString(): String {
        return "PacketReader[opcode=$opcode, buffer=$buffer]"
    }

}