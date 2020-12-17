package rs.dusk.core.network.codec.message

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import rs.dusk.core.network.codec.getCodec
import rs.dusk.core.network.model.message.Message
import rs.dusk.core.utility.getContents
import java.io.IOException

/**
 * This class is responsible for reading incoming [messages][Message] and passing them to the right [handler][MessageHandler]
 *
 * @author Tyluur <contact@kiaira.tech>
 * @since March 26, 2020
 */
open class NetworkMessageReader : SimpleChannelInboundHandler<Message>() {
	
	private val logger = InlineLogger()
	
	@Suppress("UNCHECKED_CAST")
	override fun channelRead0(ctx : ChannelHandlerContext, msg : Message) {
		try {
			val codec = ctx.channel().getCodec()
				?: throw IllegalStateException("Unable to extract codec from channel - undefined!")
			
			val handler : MessageHandler<Message>? = codec.handler(msg::class) as? MessageHandler<Message>
			
			if (handler == null) {
				logger.error { "Unable to find message handler - [msg=$msg], codec=${codec.javaClass.simpleName}" }
				return
			}
			
			handler.handle(ctx, msg)
			
			logger.debug {
				"Handled successfully [msg=$msg, codec=${codec.javaClass.simpleName}, handler=${handler.javaClass.simpleName}, pipeline=${ctx.pipeline()
					.getContents()}]"
			}
		} catch (e : IOException) {
			e.printStackTrace()
		}
	}
	
}

