package rs.dusk.core.network.connection.server

import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.MessageReader
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class NetworkServerTest {}

fun main() {
    val settings = ConnectionSettings("localhost", 43593)
    val server = NetworkServer(settings)
    //  val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
//    val sslInitializer = SslServerInitializer(config) // TODO
    val pipeline = ConnectionPipeline {
        it.addLast("message.handler", MessageReader(CodecTest()))
    }
    server.configure(pipeline)
    server.start()
}

private class CodecTest : Codec() {
    override fun register() {

    }

}