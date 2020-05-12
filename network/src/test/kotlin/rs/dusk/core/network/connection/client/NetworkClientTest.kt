package rs.dusk.core.network.connection.client

import rs.dusk.core.network.NetworkClient
import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.event.ConnectionEventChain
import rs.dusk.core.network.connection.event.ConnectionEventListener
import rs.dusk.core.network.connection.event.ConnectionEventType
import rs.dusk.core.network.connection.event.type.ConnectionReestablishmentEvent
import rs.dusk.core.network.connection.factory.client.ClientConnectionFactory

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
fun main() {
	val factory = ClientConnectionFactory()
	
	val client =
		NetworkClient(ConnectionSettings("127.0.0.1", 43593))

    val chain = ConnectionEventChain().apply {
        append(ConnectionEventType.DEREGISTER, ConnectionReestablishmentEvent(client, limit = 10, delay = 1000))
    }

    val pipeline = ConnectionPipeline {
        it.addLast("message.handler", MessageReader(TestClientCodec()))
        it.addLast("connection.listener", ConnectionEventListener(chain))
    }
	
    factory.connect(client, chain, pipeline)
}

private class TestClientCodec : Codec() {
    override fun register() {

    }

}