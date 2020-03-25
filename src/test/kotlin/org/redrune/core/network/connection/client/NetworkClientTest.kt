package org.redrune.core.network.connection.client

import org.redrune.core.network.connection.ConnectionSettings
import org.redrune.core.network.model.codec.NetworkPipeline

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
fun main() {
    val settings = ConnectionSettings("127.0.0.1", 43593)
    val client = NetworkClient(settings)
    val pipeline = NetworkPipeline()

    pipeline.addConnectionListener(client, 10_000, 5)
    client.configure(pipeline)
    client.start()

}