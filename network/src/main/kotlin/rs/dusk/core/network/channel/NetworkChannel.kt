package rs.dusk.core.network

import io.netty.channel.Channel
import rs.dusk.core.network.codec.Codec

/**
 * Gets the [network client][NetworkClient] attribute of a channel
 * @receiver Channel
 */
fun Channel.getClient() : NetworkClient {
	return attr(NetworkClient.CLIENT_KEY).get()
}

/**
 * Sets the [network client][NetworkClient] attribute of a channel
 */
fun Channel.setClient(client : NetworkClient) {
	attr(NetworkClient.CLIENT_KEY).set(client)
}

/**
 * Getting the codec of the channel
 * @receiver Channel
 */
fun Channel.getCodec() : Codec? {
	return attr(Codec.CODEC_KEY).get()
}

/**
 * Setting the codec of the channel
 * @receiver Channel
 */
fun Channel.setCodec(codec : Codec) {
	attr(Codec.CODEC_KEY).set(codec)
}