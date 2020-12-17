package rs.dusk.core.network

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Tyluur <itstyluur></itstyluur>@gmail.com>
 * @since December 17, 2020
 */
internal class NetworkClientTest {
	
	lateinit var example : TestClient
	
	@BeforeEach
	fun setUp() {
		example = TestClient("localhost", 43594)
	}
	
	@Test
	fun start() {
		example.connect()
		assertTrue(example.connected)
	}
	
	@Test
	fun shutdown() {
		example.shutdown()
		assertFalse(example.connected)
	}
}

class TestClient(host : String, private val port : Int) : NetworkClient(host) {
	
	override fun connect() {
		configureDefault()
		start(port)
	}
	
}