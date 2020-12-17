package rs.dusk.core.network

import com.github.michaelbull.logging.InlineLogger
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelOption.SO_KEEPALIVE
import io.netty.channel.ChannelOption.TCP_NODELAY
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.AttributeKey
import rs.dusk.core.network.NetworkClient.Companion.CLIENT_KEY
import rs.dusk.core.network.connection.Connectable

/**
 * A network client is a node in the network which is connected to another node.
 * It cannot be connected to by another node (in this design).
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since March 25, 2020
 */
abstract class NetworkClient(val host : String) : Connectable {
	
	private val logger = InlineLogger()
	
	/**
	 * Whether we are connected to a server
	 */
	var connected = false
	
	/**
	 * The group used for work
	 */
	private val group = NioEventLoopGroup()
	
	/**
	 * The bootstrap used for the connection
	 */
	private val bootstrap = Bootstrap()
	
	/**
	 * The [future][ChannelFuture] of the connection to the server
	 */
	private var future : ChannelFuture? = null
	
	/**
	 * The options used for the connection are configured here, as well as the [ChannelInitializer]
	 */
	override fun configure(initializer : ChannelInitializer<SocketChannel>) = with(bootstrap) {
		group(group)
		channel(NioSocketChannel::class.java)
		handler(initializer)
		option(ChannelOption.SO_REUSEADDR, true)
		option(TCP_NODELAY, true)
		option(SO_KEEPALIVE, true)
	}
	
	/**
	 * Connecting to the server
	 */
	override fun start(port : Int) : ChannelFuture = with(bootstrap) {
		future = connect(host, port).syncUninterruptibly()
		return future!!
	}
	
	/**
	 * Closing the connection to the server
	 */
	fun shutdown() {
		future?.channel()?.disconnect()
		future?.channel()?.close()
		group.shutdownGracefully()
	}
	
	companion object {
		/**
		 * The attribute in the [Channel] that identifies the session
		 */
		val CLIENT_KEY : AttributeKey<NetworkClient> = AttributeKey.valueOf("network.client.key")
	}
}

/**
 * Gets the [network client][NetworkClient] attribute of a channel
 * @receiver Channel
 */
fun Channel.getClient() : NetworkClient {
	return attr(CLIENT_KEY).get()
}

/**
 * Sets the [network client][NetworkClient] attribute of a channel
 */
fun Channel.setClient(client : NetworkClient) {
	attr(CLIENT_KEY).set(client)
}