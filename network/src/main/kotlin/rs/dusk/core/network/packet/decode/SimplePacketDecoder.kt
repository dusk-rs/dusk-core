package rs.dusk.core.network.packet.decode

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.codec.getCodec

/**
 * Handling the decoding of a simple packet, which is a packet whose contents are [opcode, buffer]
 *
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
open class SimplePacketDecoder : PacketDecoder() {
	
	private val logger = InlineLogger()
	
	override fun readOpcode(buf : ByteBuf) : Int {
		return buf.readUnsignedByte().toInt()
	}
	
	override fun getExpectedLength(ctx : ChannelHandlerContext, opcode : Int) : Int? {
		val codec = ctx.channel().getCodec()
			?: throw IllegalStateException("Unable to extract codec from channel - undefined [solution - please call ctx.channel().setCodec()]")
		
		val decoder = codec.decoder(opcode)
		if (decoder == null) {
			logger.error { "Unable to identify length of packet [opcode=$opcode, codec=${codec.javaClass.simpleName}]" }
			return null
		}
		return decoder.length
	}
}