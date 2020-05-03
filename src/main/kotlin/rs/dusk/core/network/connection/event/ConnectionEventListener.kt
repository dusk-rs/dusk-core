package rs.dusk.core.network.connection.event

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
class ConnectionEventListener(private val chain: ConnectionEventChain) : ChannelInboundHandlerAdapter() {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        chain.handle(ConnectionEventType.REGISTER, ctx)
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        chain.handle(ConnectionEventType.DEREGISTER, ctx)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        chain.handle(ConnectionEventType.ACTIVE, ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        chain.handle(ConnectionEventType.INACTIVE, ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        chain.handle(ConnectionEventType.INACTIVE, ctx, cause)
    }


}