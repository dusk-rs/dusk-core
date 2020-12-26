package rs.dusk.core.network.packet.access

import io.netty.buffer.ByteBuf
import rs.dusk.core.network.buffer.cipher.isaac.IsaacCipher

/**
 * In order to send a packet from the server to the client, it must first be built, then wrote to.
 * This class provides the functionality to do the building, as well as pass the writing on to the appropriate writer.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since February 18, 2020
 */
class PacketBuilder(private val cipher : IsaacCipher? = null, private val sized : Boolean = cipher != null) {
	
	fun build(buffer : ByteBuf, build : (PacketWriter) -> Unit) {
		val writer = PacketWriter(buffer = buffer, cipher = cipher)
		build(writer)
		if (sized) {
			writer.writeSize()
			
		}
	}
	
}