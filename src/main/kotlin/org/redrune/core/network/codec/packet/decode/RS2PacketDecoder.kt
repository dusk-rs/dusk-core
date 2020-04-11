package org.redrune.core.network.codec.packet.decode

import io.netty.buffer.ByteBuf
import org.redrune.core.network.codec.Codec
import org.redrune.core.tools.crypto.IsaacCipher

/**
 * This packet decoder decodes runescape packets which are built in this manner [opcode, length, buffer], with the opcode decryption requiring an [IsaacCipher]
 *
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
class RS2PacketDecoder(private val cipher: IsaacCipher, codec: Codec) : SimplePacketDecoder(codec) {

    override fun readOpcode(buf: ByteBuf): Int {
        println("attempting to read opcode of a rs2 packet with cipher=${cipher.seed.contentToString()}")
        return (buf.readUnsignedByte().toInt() - cipher.nextInt()) and 0xff
    }

}