package rs.dusk.core.network

import get
import inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import rs.dusk.core.network.codec.NetworkCodecRepository
import rs.dusk.core.network.codec.message.NetworkMessageReader
import rs.dusk.core.network.codec.message.decode.OpcodeMessageDecoder
import rs.dusk.core.network.codec.message.encode.GenericMessageEncoder
import rs.dusk.core.network.codec.packet.decode.SimplePacketDecoder
import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.event.ChannelEventChain
import rs.dusk.core.network.connection.event.ChannelEventListener
import rs.dusk.core.network.connection.event.ChannelEventType.DEREGISTER
import rs.dusk.core.network.connection.event.ChannelEventType.EXCEPTION
import rs.dusk.core.network.connection.event.type.ChannelExceptionEvent

/**
 * @author Tyluur <itstyluur></itstyluur>@gmail.com>
 * @since December 17, 2020
 */
internal class NetworkServerTest {
	
	lateinit var example : ExampleServer
	
	@BeforeEach
	fun setUp() {
		example = ExampleServer(43594)
	}
	
	@Test
	fun start() {
		example.boot()
		assertTrue(example.running)
	}
	
	@Test
	fun shutdown() {
		example.shutdown()
		assertFalse(example.running)
	}
}

class ExampleServer(val port : Int) : NetworkServer() {
	
	fun boot() {
		val chain = ChannelEventChain().apply {
			append(EXCEPTION, ChannelExceptionEvent())
		}
		
		val pipeline = ConnectionPipeline {
			val channel = it.channel()
			
			it.addLast("packet.decoder", SimplePacketDecoder())
			it.addLast("message.decoder", OpcodeMessageDecoder())
			it.addLast("message.reader", NetworkMessageReader())
			it.addLast("message.encoder", GenericMessageEncoder())
			it.addLast("channel.listener", ChannelEventListener(chain))
		}
		configure(pipeline)
		start(port)
	}
	
	override fun onConnect() {
		TODO("Not yet implemented")
	}
	
	override fun onDisconnect() {
		TODO("Not yet implemented")
	}
	
	
}