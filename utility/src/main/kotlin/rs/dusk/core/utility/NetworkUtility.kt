package rs.dusk.core.utility

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelPipeline
import java.util.*

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 03, 2020
 */
class NetworkUtility {
	
	companion object {
		
		/**
		 * Converts an IP-Address as string to Integer.
		 *
		 * @return The Integer.
		 */
		fun convertIPToNumber(ipAddress : String?) : Int {
			val st = StringTokenizer(ipAddress, ".")
			val ip = IntArray(4)
			var i = 0
			while (st.hasMoreTokens()) {
				ip[i++] = st.nextToken().toInt()
			}
			return ip[0] shl 24 or (ip[1] shl 16) or (ip[2] shl 8) or ip[3]
		}
		
	}
	
}

/**
 * Returns the contents of the pipeline in order from head to tail as a [List] of type [String]
 * @receiver Channel
 * @return String
 */
fun ChannelPipeline.getPipelineContents() : String {
	val list = mutableMapOf<String, String>()
	forEach { list[it.key] = it.value.javaClass.simpleName }
	return list.toString()
}

fun ChannelPipeline.replace(name : String, handler : ChannelHandler) : ChannelHandler? {
	return replace(name, name, handler)
}

/**
 * Returns the contents of the buffer in a readable format (hexadecimal)
 */
fun ByteBuf.getHexContents() : String {
	return ByteBufUtil.hexDump(toByteArraySafe())
}

fun ByteBuf.toByteArraySafe() : ByteArray {
	if (this.hasArray()) {
		return this.array()
	}
	
	val bytes = ByteArray(this.readableBytes())
	this.getBytes(this.readerIndex(), bytes)
	
	return bytes
}