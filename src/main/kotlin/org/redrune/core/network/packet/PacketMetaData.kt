package org.redrune.core.network.packet

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class PacketMetaData(
    val opcodes: IntArray,
    val length: Int
)