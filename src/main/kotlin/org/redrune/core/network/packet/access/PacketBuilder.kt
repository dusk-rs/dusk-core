package org.redrune.core.network.packet.access

import io.netty.buffer.ByteBuf
import org.redrune.core.tools.crypto.IsaacCipher

/**
 * The building of a packet is done by this class
 *
 * @author Greg Hibb
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class PacketBuilder(override val buffer: ByteBuf, override val cipher: IsaacCipher? = null) : PacketWriter() {

    init {
        println("packet builder init!")
        if (opcode != null) {
            writeOpcode(opcode!!, type)
            println("Wrote an opcode whne constructing the packet builder ($opcode)")
        }
    }

}