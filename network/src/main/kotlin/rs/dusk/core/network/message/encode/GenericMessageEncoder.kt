package rs.dusk.core.network.message.encode

import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import rs.dusk.core.network.channel.getCodec
import rs.dusk.core.network.message.MessageEncoder
import rs.dusk.core.network.packet.access.PacketBuilder
import rs.dusk.core.network.message.Message

/**
 * This class adds the capability to write generic messages (which extend the [Message][Message] class) to the channel.
 * The encoding portion is done by introducing the requirement of an encoding class for the message, to the codec.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since February 18, 2020
 */
@ChannelHandler.Sharable
class GenericMessageEncoder(
	/**
	 * The packet builder instance
	 */
	private val builder : PacketBuilder = PacketBuilder()
) : MessageToByteEncoder<Message>() {
	
	private val logger = InlineLogger()
	
	@Suppress("UNCHECKED_CAST")
	override fun encode(ctx : ChannelHandlerContext, msg : Message, out : ByteBuf) {
		val codec = ctx.channel().getCodec()
			?: throw IllegalStateException("Unable to extract codec from channel - undefined!")
		
		val encoder = codec.encoder(msg::class) as? MessageEncoder<Message>
		if (encoder == null) {
			logger.error { "Unable to find encoder! [msg=$msg, codec=${codec.javaClass.simpleName}]" }
			return
		}
		builder.build(out) {
			encoder.encode(it, msg)
		}
		logger.debug { "Encoding successful [encoder=${encoder.javaClass.simpleName}, msg=$msg, codec=${codec.javaClass.simpleName}" }
	}
	
}
