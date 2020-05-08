package rs.dusk.core.network.connection.factory.client

import rs.dusk.core.network.NetworkClient
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.event.ConnectionEventChain
import rs.dusk.core.network.connection.event.ConnectionEventType.DEREGISTER
import rs.dusk.core.network.connection.event.ConnectionEventType.REGISTER
import rs.dusk.core.network.connection.event.type.ConnectionDeregistrationEvent
import rs.dusk.core.network.connection.event.type.ConnectionRegistrationEvent
import rs.dusk.core.network.connection.factory.ConnectionFactory

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
open class ClientConnectionFactory : ConnectionFactory() {
	
	/**
	 * Connecting to the server
	 */
	fun connect(client : NetworkClient, chain : ConnectionEventChain, pipeline : ConnectionPipeline) = with(chain) {
		append(REGISTER, ConnectionRegistrationEvent(channels))
		append(DEREGISTER, ConnectionDeregistrationEvent(channels))
		
		client.configure(pipeline)
		client.connect()
	}
	
	
}