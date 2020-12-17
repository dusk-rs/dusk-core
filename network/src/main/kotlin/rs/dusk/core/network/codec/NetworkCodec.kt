package rs.dusk.core.network.codec

import io.netty.channel.Channel
import io.netty.util.AttributeKey
import rs.dusk.core.network.codec.NetworkCodec.Companion.CODEC_KEY
import rs.dusk.core.network.codec.message.MessageDecoder
import rs.dusk.core.network.codec.message.MessageEncoder
import rs.dusk.core.network.codec.message.MessageHandler
import rs.dusk.core.network.model.message.Message
import rs.dusk.core.utility.ReflectionUtils
import kotlin.reflect.KClass

/**
 * The
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since February 18, 2020
 */
abstract class NetworkCodec {
	
	/**
	 * The [codec repository][CodecRepository] for this codec, which contains the collections used for codec functions.
	 */
	private val repository = CodecRepository()
	
	/**
	 * The registration of all [components][CodecRepository] of this codec must be done here.
	 *
	 */
	abstract fun register()
	
	/**
	 * Finds a decoder by an [opcode][Int]
	 * @return MessageDecoder<*>?
	 */
	fun decoder(opcode : Int) : MessageDecoder<*>? {
		return repository.decoders[opcode]
	}
	
	/**
	 * This message finders a [handler][MessageHandler] by class
	 * @return MessageHandler<M>?
	 */
	@Suppress("UNCHECKED_CAST")
	fun <M : Message> handler(clazz : KClass<M>) : MessageHandler<M>? {
		return repository.handlers[clazz] as? MessageHandler<M>
	}
	
	/**
	 * Finds an [encoder][MessageEncoder] by class
	 * @return MessageEncoder<M>?
	 */
	@Suppress("UNCHECKED_CAST")
	fun <M : Message> encoder(clazz : KClass<M>) : MessageEncoder<M>? {
		return repository.encoders[clazz] as? MessageEncoder<M>
	}
	
	companion object {
		/**
		 * The attribute in the [channel][Channel] that identifies the [codec][NetworkCodec]
		 */
		val CODEC_KEY : AttributeKey<NetworkCodec> = AttributeKey.valueOf("codec.key")
		
		
		/**
		 * This method calls the [register][NetworkCodec.register] function for all classes of type [NetworkCodec].
		 *
		 * @return The total amount of times the [register][NetworkCodec.register] function was called
		 */
		fun registerAll() : Int {
			var count = 0
			val codecs = ReflectionUtils.findSubclasses<NetworkCodec>()
			val iterator = codecs.iterator()
			while (iterator.hasNext()) {
				with(iterator.next()) {
					register()
					count++
				}
			}
			return count
		}
	}
}

/**
 * Getting the codec of the channel
 * @receiver Channel
 */
fun Channel.getCodec() : NetworkCodec? {
	return attr(CODEC_KEY).get()
}

/**
 * Setting the codec of the channel
 * @receiver Channel
 */
fun Channel.setCodec(codec : NetworkCodec) {
	attr(CODEC_KEY).set(codec)
}