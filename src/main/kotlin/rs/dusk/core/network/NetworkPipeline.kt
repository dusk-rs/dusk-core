package rs.dusk.core.network

import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
class NetworkPipeline(private val action: (ChannelPipeline) -> Unit) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        action.invoke(ch.pipeline())
    }

}