package rs.dusk.core.network.message

import io.netty.channel.ChannelHandlerContext
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
abstract class MessageHandler<M : Message> {
	
	/**
	 * Handles what to do with message [M]
	 */
	abstract fun handle(ctx : ChannelHandlerContext, msg : M) : Any
	
	@Suppress("UNCHECKED_CAST")
	fun getGenericTypeClass() : KClass<M> {
		return try {
			val className =
				(javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0].typeName
			val clazz = Class.forName(className).kotlin
			clazz as KClass<M>
		} catch (e : Exception) {
			throw IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ", e)
		}
	}
}