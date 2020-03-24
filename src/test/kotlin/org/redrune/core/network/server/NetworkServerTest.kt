package org.redrune.core.network.server

import org.redrune.core.network.secure.SslConfig
import org.redrune.core.network.secure.SslServerInitializer

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class NetworkServerTest {


}

fun main() {
    val server = NetworkServer()
    val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
    server.configure(SslServerInitializer(config))
    server.start(43593)
}