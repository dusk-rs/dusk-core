package org.redrune.core.network.packet

import io.netty.buffer.ByteBuf

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class Packet(open val buffer: ByteBuf)