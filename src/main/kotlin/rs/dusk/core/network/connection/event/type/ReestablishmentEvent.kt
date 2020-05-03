package rs.dusk.core.network.connection.event.type

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rs.dusk.core.network.connection.client.NetworkClient
import rs.dusk.core.network.connection.event.ConnectionEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
class ReestablishmentEvent(

    /**
     * The client that we wish to reconnect to a server
     */
    private val client: NetworkClient,

    /**
     * The maximum amount of re-connection attempts
     */
    private val retryCapacity: Int,

    /**
     * After a connection has closed, this value is the amount of seconds to wait until attempting to reconnect
     */
    private val retryDelay: Long
) : ConnectionEvent {

    private val logger = InlineLogger()

    /**
     * If we are currently attempting to re-establish a connection
     */
    private var running = false

    override fun run(ctx: ChannelHandlerContext, cause: Throwable?) {
        if (running) {
            logger.info { "An attempt to run multiple re-establishment events has been stopped." }
            return
        }
        running = true
        runBlocking {
            GlobalScope.launch {
                println("1")
                reestablish()
                println("2")
            }
        }
    }

    private suspend fun reestablish() {
        logger.info { "Attempt is starting [cap=$retryCapacity del=$retryDelay]" }
        var attempt = 1
        repeat(retryCapacity) {
            delay(retryDelay)

            logger.info { "Retry attempt ${attempt}/$retryCapacity is running." }
            attempt++
        }
        logger.info { "End of retry arrived." }
    }

}