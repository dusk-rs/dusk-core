package rs.dusk.core.network.codec

import io.netty.channel.Channel
import io.netty.util.AttributeKey
import rs.dusk.core.network.codec.Codec.Companion.CODEC_KEY
import rs.dusk.core.network.message.MessageDecoder
import rs.dusk.core.network.message.MessageEncoder
import rs.dusk.core.network.message.MessageHandler
import rs.dusk.core.network.message.Message
import rs.dusk.core.utility.ReflectionUtils
import kotlin.reflect.KClass

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since February 18, 2020
 */
abstract class Codec {
	
	/**
	 * The [codec repository][CodecRepository] for this codec, which contains the collections used for codec functions.
	 */
	private val repository = CodecRepository()
	
	/**
	 * The registration of all [components][CodecRepository] of this codec must be done here.
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
		 * The attribute in the [channel][Channel] that identifies the [codec][Codec]
		 */
		val CODEC_KEY : AttributeKey<Codec> = AttributeKey.valueOf("codec.key")
		
		
		/**
		 * This method calls the [register][Codec.register] function for all classes of type [Codec].
		 *
		 * @return The total amount of times the [register][Codec.register] function was called
		 */
		fun registerAll() : Int {
			var count = 0
			val codecs = ReflectionUtils.findSubclasses<Codec>()
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