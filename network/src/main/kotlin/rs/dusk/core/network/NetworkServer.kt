package rs.dusk.core.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.*
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.GlobalEventExecutor
import rs.dusk.core.network.connection.Connectable

/**
 * A network server is a node in the network which can be connected to by other nodes.
 * It cannot connect to other nodes (in this design).
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since March 18, 2020
 */
abstract class NetworkServer : Connectable {
	
	/**
	 * The execution function of the server
	 */
	abstract fun listen()
	
	/**
	 * If the server is running
	 */
	var running = false
	
	/**
	 * The group of [channel][Channel]s connected
	 */
	private val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
	
	/**
	 * The event group used for the parent group
	 */
	private val bossGroup : EventLoopGroup = createGroup(true)
	
	/**
	 * The event group used for the child group
	 */
	private val workerGroup : EventLoopGroup = createGroup(false)
	
	/**
	 * The server bootstrap
	 */
	private var bootstrap = ServerBootstrap().group(bossGroup, workerGroup)
	
	/**
	 * The bootstrap is configured here by preparing the worker groups then binding the relevant options
	 */
	override fun configure(initializer : ChannelInitializer<SocketChannel>) = with(bootstrap) {
		channel(NioServerSocketChannel::class.java)
		option(ChannelOption.SO_BACKLOG, 25)
		option(ChannelOption.SO_REUSEADDR, true)
		option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
		childOption(ChannelOption.TCP_NODELAY, true)
		childOption(ChannelOption.SO_KEEPALIVE, true)
		childHandler(initializer)
	}
	
	/**
	 * The server is started by binding the server to the defined port
	 */
	override fun start(port : Int) : ChannelFuture = with(bootstrap) {
		val future = bind(port)
		running = true
		return future.syncUninterruptibly()
	}
	
	/**
	 * Shuts down the [EventLoopGroup]s used for this server gracefully
	 */
	fun shutdown() {
		bossGroup.shutdownGracefully()
		workerGroup.shutdownGracefully()
		running = false
	}
	
	companion object {
		fun createGroup(boss : Boolean) : NioEventLoopGroup {
			val serverWorkersCount = if (boss) {
				1
			} else {
				Runtime.getRuntime().availableProcessors() / 2
			}
			return NioEventLoopGroup(serverWorkersCount)
		}
	}
}