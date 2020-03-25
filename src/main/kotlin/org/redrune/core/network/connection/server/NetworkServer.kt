package org.redrune.core.network.connection.server

import com.github.michaelbull.logging.InlineLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.redrune.core.network.connection.ConnectionSettings
import org.redrune.core.network.connection.secure.SslServerInitializer

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class NetworkServer(
    /**
     * The connection settings to use
     */
    private val settings: ConnectionSettings,

    /**
     * The event group used for the parent group
     */
    private val bossGroup: EventLoopGroup = createGroup(true),

    /**
     * The event group used for the child group
     */
    private val workerGroup: EventLoopGroup = createGroup(false)
) : ServerBootstrap() {

    private val logger = InlineLogger()

    /**
     * The bootstrap is configured here by preparing the worker groups then binding the relevant options
     */
    fun configure(initializer: ChannelInitializer<SocketChannel>) {
        group(bossGroup, workerGroup)
        channel(NioServerSocketChannel::class.java)
        option(ChannelOption.SO_BACKLOG, 25)
        option(ChannelOption.SO_REUSEADDR, true)
        option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        childOption(ChannelOption.TCP_NODELAY, true)
        childOption(ChannelOption.SO_KEEPALIVE, true)
        childHandler(initializer)
    }

    /**
     * The server is started by binding the server to the defined port
     */
    fun start(sslInitializer: SslServerInitializer? = null): ChannelFuture {
        val future = bind(settings.port).syncUninterruptibly()
        sslInitializer?.addSslHandler(future.channel())
        logger.info { "Network bound with $settings" }
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
                Runtime.getRuntime().availableProcessors() / 2
            }
            return NioEventLoopGroup(serverWorkersCount)
        }
    }
}