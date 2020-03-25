package org.redrune.core.network.connection.client

import com.github.michaelbull.logging.InlineLogger
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.redrune.core.network.connection.ConnectionSettings
import kotlin.system.exitProcess

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
class NetworkClient(private val settings: ConnectionSettings) : Bootstrap() {

    private val logger = InlineLogger()

    /**
     * Whether we are connected to a server
     */
    var connected = false

    /**
     * The [ChannelFuture] produced from the reconnection
     */
    private var future: ChannelFuture? = null

    /**
     * The options used for the connection are configured here, as well as the [ChannelInitializer]
     */
    fun configure(initializer: ChannelInitializer<SocketChannel>) {
        group(NioEventLoopGroup())
        channel(NioSocketChannel::class.java)
        handler(initializer)
        option(ChannelOption.SO_REUSEADDR, true)
        option(ChannelOption.TCP_NODELAY, true)
        option(ChannelOption.SO_KEEPALIVE, true)
    }

    /**
     * Connecting to the server
     */
    fun start(): ChannelFuture {
        val (host, port) = settings
        future = connect(host, port).syncUninterruptibly()
        logger.info { "Successfully connected to $host on port $port" }
        connected = true
        return future!!
    }

    /**
     * Closing the connection to the server
     */
    fun shutdown() {
        future?.channel()?.disconnect()
        future?.channel()?.close()
        future = null
        logger.info { "Shutdown was a success" }
        exitProcess(-1)
    }

}