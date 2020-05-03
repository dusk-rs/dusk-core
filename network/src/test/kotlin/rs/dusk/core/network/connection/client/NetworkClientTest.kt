package rs.dusk.core.network.connection.client

import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.event.ConnectionEventChain
import rs.dusk.core.network.connection.event.ConnectionEventListener
import rs.dusk.core.network.connection.event.ConnectionEventType
import rs.dusk.core.network.connection.event.type.ReestablishmentEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
fun main() {
    val settings = ConnectionSettings("127.0.0.1", 43593)
    val client = NetworkClient(settings)

    val chain = ConnectionEventChain().apply {
        append(ConnectionEventType.DEREGISTER, ReestablishmentEvent(client, limit = 1, delay = 10))
    }

    val pipeline = ConnectionPipeline {
        it.addLast("message.handler", MessageReader(TestClientCodec()))
        it.addLast("connection.listener", ConnectionEventListener(chain))
    }
    client.configure(pipeline)
    client.start()
}

private class TestClientCodec : Codec() {
    override fun register() {

    }

}