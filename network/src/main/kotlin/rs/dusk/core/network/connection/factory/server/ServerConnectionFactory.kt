package rs.dusk.core.network.connection.factory.server

import rs.dusk.core.network.NetworkServer
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
open class ServerConnectionFactory : ConnectionFactory() {
	
	open fun bind(server : NetworkServer, chain : ConnectionEventChain, pipeline : ConnectionPipeline) = with(chain) {
		append(REGISTER, ConnectionRegistrationEvent(channels))
		append(DEREGISTER, ConnectionDeregistrationEvent(channels))
		
		server.configure(pipeline)
		server.bind()
	}
	
}