package rs.dusk.core.network

import io.netty.channel.Channel

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