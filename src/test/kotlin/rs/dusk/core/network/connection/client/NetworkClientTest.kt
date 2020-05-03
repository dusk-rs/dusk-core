package rs.dusk.core.network.connection.client

import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.event.ConnectionEventChain
import rs.dusk.core.network.connection.event.ConnectionEventListener
import rs.dusk.core.network.connection.event.ConnectionEventType.DEREGISTER
import rs.dusk.core.network.connection.event.type.ReestablishmentEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
fun main() {
    val settings = ConnectionSettings("127.0.0.1", 43593)
    val client = NetworkClient(settings)

    val chain = ConnectionEventChain().apply {
        append(DEREGISTER, ReestablishmentEvent(client, retryCapacity = 10, retryDelay = 1000))
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