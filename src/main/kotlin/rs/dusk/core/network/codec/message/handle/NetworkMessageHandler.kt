package rs.dusk.core.network.codec.message.handle

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageHandler
import rs.dusk.core.network.connection.ConnectionEvent
import rs.dusk.core.network.model.message.Message
import rs.dusk.core.tools.utility.getPipelineContents
import java.io.IOException

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 26, 2020
 */
class NetworkMessageHandler(private val codec: Codec, private val event: ConnectionEvent) :
    SimpleChannelInboundHandler<Message>() {

    private val logger = InlineLogger()

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        event.onRegistration(ctx)
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        event.onDeregistration(ctx)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        event.onActive(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        event.onInactive(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        event.onException(ctx, cause)
    }

    @Suppress("UNCHECKED_CAST")
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        try {
            val handler: MessageHandler<Message>? = codec.handler(msg::class) as? MessageHandler<Message>

            if (handler == null) {
                logger.warn { "Unable to find message handler - [msg=$msg], codec=${codec.javaClass.simpleName}" }
                return
            }

            handler.handle(ctx, msg)

            logger.info {
                "Handled successfully [msg=$msg, codec=${codec.javaClass.simpleName}, handler=${handler.javaClass.simpleName}, pipeline=${ctx.pipeline()
                    .getPipelineContents()}]"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

