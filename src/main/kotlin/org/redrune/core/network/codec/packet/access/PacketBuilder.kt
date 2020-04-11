package org.redrune.core.network.codec.packet.access

import io.netty.buffer.ByteBuf
import org.redrune.core.tools.crypto.IsaacCipher

/**
 * The building of a packet is done by this class
 *
 * @author Greg Hibb
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class PacketBuilder(private val cipher: IsaacCipher? = null, private val sized: Boolean = cipher != null) {

    fun build(buffer: ByteBuf, build: (PacketWriter) -> Unit) {
        val writer = PacketWriter(buffer = buffer, cipher = cipher)
        build(writer)
        if(sized) {
            writer.writeSize()
        }
    }

}