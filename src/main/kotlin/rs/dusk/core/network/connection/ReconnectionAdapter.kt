package rs.dusk.core.network.connection

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import rs.dusk.core.network.connection.client.NetworkClient

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
@ChannelHandler.Sharable
class ReconnectionAdapter(

        val client: NetworkClient,

        /**
     * After a connection has closed, this value is the amount of seconds to wait until attempting to reconnect
     */
    val retryDelay: Long,

        /**
     * The maximum amount of re-connection attempts
     */
    val retryCapacity: Int

) : ChannelInboundHandlerAdapter() {

    private val logger = InlineLogger()

    /**
     * The worker used to perform the reconnection
     */
    private val worker = ReconnectionWorker(this)

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        client.connected = true
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        client.connected = false
        worker.onDeregistry()
    }
}