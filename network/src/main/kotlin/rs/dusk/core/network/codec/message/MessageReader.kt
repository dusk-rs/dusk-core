package rs.dusk.core.network.codec.message

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.model.message.Message
import rs.dusk.core.utility.getPipelineContents
import java.io.IOException

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 26, 2020
 */
class MessageReader(private val codec: Codec) : SimpleChannelInboundHandler<Message>() {

    private val logger = InlineLogger()

    @Suppress("UNCHECKED_CAST")
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        try {
            val handler: MessageHandler<Message>? = codec.handler(msg::class) as? MessageHandler<Message>

            if (handler == null) {
                logger.warn { "Unable to find message handler - [msg=$msg], codec=${codec.javaClass.simpleName}" }
                return
            }

            handler.handle(ctx, msg)

            logger.debug {
                "Handled successfully [msg=$msg, codec=${codec.javaClass.simpleName}, handler=${handler.javaClass.simpleName}, pipeline=${ctx.pipeline()
                        .getPipelineContents()}]"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

