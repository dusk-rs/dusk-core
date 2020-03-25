package org.redrune.core.network.connection

import com.github.michaelbull.logging.InlineLogger

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
class ReconnectionWorker(private val adapter: ReconnectionAdapter) : Thread() {

    private val logger = InlineLogger()

    /**
     * The amount of retry attempts that have been done
     */
    private var retryCount = 0

    /**
     * When a connection has dropped we will need to handle the reconnection
     */
    private var reconnecting = false

    fun onDeregistry() {
        if (reconnecting) {
            return
        }
        reconnecting = true
        start()
    }

    override fun run() {
        val client = adapter.client
        val retryDelay = adapter.retryDelay
        val retryCapacity = adapter.retryCapacity
        while (reconnecting) {
            if (client.connected) {
                reset()
                break
            }
            if (retryCount >= retryCapacity) {
                client.shutdown()
                break
            }
            val reconnected = try {
                client.start()
                true
            } catch (e: Exception) {
                false
            }
            if (reconnected) {
                reset()
            }
            retryCount++
            sleep(retryDelay)
        }
    }

    /**
     * Resetting the statistics used for the reconnecting function
     */
    private fun reset() {
        retryCount = 0
        reconnecting = false
    }

}