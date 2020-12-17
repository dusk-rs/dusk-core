package rs.dusk.core.network

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tyluur <itstyluur></itstyluur>@gmail.com>
 * @since December 17, 2020
 */
internal class NetworkServerTest {
	
	lateinit var example : TestServer
	
	@BeforeEach
	fun setUp() {
		example = TestServer(43594)
	}
	
	@Test
	fun start() {
		example.configureDefault()
		assertTrue(example.running)
	}
	
	@Test
	fun shutdown() {
		example.shutdown()
		assertFalse(example.running)
	}
}

class TestServer(private val port : Int) : NetworkServer() {
	
	override fun listen() {
		configureDefault()
		start(port)
	}
	
	
}