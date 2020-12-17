package rs.dusk.core.network.connection

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
 * This class interfaces for nodes in a network that are connectable - i.e. the node can connect to a server or it can be connected to by a client.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since May 11, 2020
 */
interface Connectable {
	
	/**
	 * A connectable object will call this when the connection has been dropped
	 */
	fun onConnect()
	
	/**
	 * A connectable object will call this when a connection has been dropped
	 */
	fun onDisconnect()
	
	/**
	 * Configure the properties for the channel
	 */
	fun configure(initializer : ChannelInitializer<SocketChannel>) : Any
	
	/**
	 * Start the function of the connectable node (bind or connect)
	 */
	fun start(port: Int) : Any
}