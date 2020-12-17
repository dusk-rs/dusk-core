package rs.dusk.core.network

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.event.ChannelEventChain
import rs.dusk.core.network.connection.event.ChannelEventListener
import rs.dusk.core.network.connection.event.ChannelEventType.*
import rs.dusk.core.network.connection.event.type.ChannelExceptionEvent
import rs.dusk.core.network.message.NetworkMessageReader
import rs.dusk.core.network.message.decode.OpcodeMessageDecoder
import rs.dusk.core.network.message.encode.GenericMessageEncoder
import rs.dusk.core.network.packet.decode.SimplePacketDecoder

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
	fun onConnect(ctx : ChannelHandlerContext) {}
	
	/**
	 * A connectable object will call this when a connection has been dropped
	 */
	fun onDisconnect(ctx : ChannelHandlerContext) {}
	
	/**
	 * Configure the properties for the channel
	 */
	fun configure(initializer : ChannelInitializer<SocketChannel>) : Any
	
	/**
	 * Start the function of the connectable node (bind or connect)
	 */
	fun start(port : Int) : Any
	
	/**
	 * Configure a default pipeline
	 */
	fun configureDefault() {
		val chain = ChannelEventChain().apply {
			append(EXCEPTION, ChannelExceptionEvent())
		}
		val pipeline = ConnectionPipeline {
			it.addLast("packet.decoder", SimplePacketDecoder())
			it.addLast("message.decoder", OpcodeMessageDecoder())
			it.addLast("message.reader", NetworkMessageReader())
			it.addLast("message.encoder", GenericMessageEncoder())
			it.addLast("channel.listener", ChannelEventListener(chain))
		}
		configure(pipeline)
	}
}