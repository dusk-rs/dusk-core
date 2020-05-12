package rs.dusk.core.network.connection.server

import rs.dusk.core.network.NetworkServer
import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.event.ChannelEventChain
import rs.dusk.core.network.connection.event.ChannelEventListener
import rs.dusk.core.network.connection.factory.server.ServerConnectionFactory

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class NetworkServerTest

fun main() {
	val factory = ServerConnectionFactory()
	
	val settings = ConnectionSettings("localhost", 43593)
	val server = NetworkServer(settings)
	
	/*
	val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
	val sslInitializer = SslServerInitializer(config) // TODO
	*/
	
	val chain = ChannelEventChain()
	
	val pipeline = ConnectionPipeline {
		it.addLast("message.handler", MessageReader(object : Codec() {
			override fun register() {
			
			}
			
		}))
		it.addLast("connection.listener", ChannelEventListener(chain))
	}
	factory.bind(server, chain, pipeline)
}