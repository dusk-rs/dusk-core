package rs.dusk.core.network.connection.server

import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.codec.Codec
import rs.dusk.core.network.codec.message.handle.NetworkMessageHandler
import rs.dusk.core.network.connection.ConnectionEvent
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.secure.SslConfig
import rs.dusk.core.network.connection.secure.SslServerInitializer

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
fun main() {
    val settings = ConnectionSettings("localhost", 43593)
    val server = NetworkServer(settings)
    val config = SslConfig("./", "", "") ?: throw IllegalStateException("Unable to create ssl configuration")
    val sslInitializer = SslServerInitializer(config) // TODO
    val pipeline = ConnectionPipeline {
        it.addLast("message.handler", NetworkMessageHandler(object : Codec() {
            override fun register() {
                TODO("Not yet implemented")
            }

        }, object : ConnectionEvent {
            override fun onRegistration(ctx: ChannelHandlerContext) {
                TODO("Not yet implemented")
            }

            override fun onDeregistration(ctx: ChannelHandlerContext) {
                TODO("Not yet implemented")
            }

            override fun onActive(ctx: ChannelHandlerContext) {
                TODO("Not yet implemented")
            }

            override fun onInactive(ctx: ChannelHandlerContext) {
                TODO("Not yet implemented")
            }

            override fun onException(ctx: ChannelHandlerContext, exception: Throwable) {
                TODO("Not yet implemented")
            }

        }))
    }
    server.configure(pipeline)
    server.start()
}