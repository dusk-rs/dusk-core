package rs.dusk.core.network.model.event.type

import rs.dusk.core.network.model.event.NetworkEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since April 08, 2020
 */
data class ActivityNetworkEvent(val active: Boolean) : NetworkEvent