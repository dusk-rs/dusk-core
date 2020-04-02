package org.redrune.core.network.connection

import io.netty.channel.ChannelHandlerContext

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 26, 2020
 */
interface ConnectionEvent {

    /**
     * When a channel is registered, this function is called
     */
    fun onRegistration(ctx: ChannelHandlerContext)

    /**
     * When a channel is unregistered, this function is called
     */
    fun onDeregistration(ctx: ChannelHandlerContext)

    /**
     * When the channel h
     * @param ctx ChannelHandlerContext
     */
    fun onActive(ctx: ChannelHandlerContext)

    /**
     * When a channel has gone inactive, this function is called
     */
    fun onInactive(ctx: ChannelHandlerContext)

    /**
     * When there is an exception produced by the channel, this function is called
     */
    fun onException(ctx: ChannelHandlerContext, exception: Throwable)
}