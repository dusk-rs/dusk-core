package org.redrune.core.network.connection.server

import org.redrune.core.network.connection.ConnectionSettings
import org.redrune.core.network.model.codec.NetworkPipeline

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
fun main() {
    val settings = ConnectionSettings("localhost", 43593)
    val server = NetworkServer(settings)
//    val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
//    val sslInitializer = SslServerInitializer(config)
    val pipeline = NetworkPipeline().apply {

    }
    server.configure(pipeline)
    server.start()
}