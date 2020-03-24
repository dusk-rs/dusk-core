package org.redrune.core.network.server

import com.github.michaelbull.logging.InlineLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.redrune.core.network.secure.SslServerInitializer

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
internal class NetworkServer(
    private val bossGroup: EventLoopGroup = createGroup(true),
    private val workerGroup: EventLoopGroup = createGroup(false)
) : ServerBootstrap() {

    private val logger = InlineLogger()

    /**
     * The bootstrap is configured here
     */
    fun configure(sslServerInitializer: SslServerInitializer) {
        group(bossGroup, workerGroup)
        channel(NioServerSocketChannel::class.java)
        option(ChannelOption.SO_BACKLOG, 25)
        option(ChannelOption.SO_REUSEADDR, true)
        option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        childOption(ChannelOption.TCP_NODELAY, true)
        childOption(ChannelOption.SO_KEEPALIVE, true)
    }

    /**
     * Binds the server to the defined port
     * @param portId Int The port number to bind this server to
     */
    fun start(portId: Int): ChannelFuture {
        val future = bind(portId).syncUninterruptibly()
        logger.info { "Network bound to port $portId" }
        return future
    }

    /**
     * Shuts down the [EventLoopGroup]s used for this server gracefully
     */
    fun shutdown() {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }

    companion object {
        fun createGroup(boss: Boolean): NioEventLoopGroup {
            val serverWorkersCount = if (boss) {
                1
            } else {
                val processors = Runtime.getRuntime().availableProcessors()
                if (processors >= 6) processors - if (processors >= 12) 7 else 5 else 1
            }
            return NioEventLoopGroup(serverWorkersCount)
        }
    }
}