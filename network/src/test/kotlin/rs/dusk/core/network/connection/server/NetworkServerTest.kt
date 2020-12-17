package rs.dusk.core.network.connection.server

import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionFactory
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.event.ChannelEventChain
import rs.dusk.core.network.connection.event.ChannelEventListener
import rs.dusk.core.network.security.SslConfig
import rs.dusk.core.network.security.SslServerInitializer

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class NetworkServerTest {
	val factory = ServerConnectionFactory()
	
	val settings = ConnectionSettings("localhost", 43593)

	val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
	val sslInitializer = SslServerInitializer(config) // TODO - finish implementing this
	
	val chain = ChannelEventChain()
	
	fun bind() {
		val pipeline = ConnectionPipeline {
			it.addLast("message.handler", MessageReader())
			it.addLast("connection.listener", ChannelEventListener(chain))
		}
		
	}
}

class ServerConnectionFactory : ConnectionFactory() {

}

fun main() {
	
	val server = NetworkServerTest()
}
