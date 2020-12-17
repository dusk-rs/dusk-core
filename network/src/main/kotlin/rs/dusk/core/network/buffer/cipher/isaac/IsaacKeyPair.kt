package rs.dusk.core.network.buffer.cipher.isaac

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class IsaacKeyPair(seed: IntArray) {
	
	val inCipher: IsaacCipher = IsaacCipher(seed)
	val outCipher: IsaacCipher
	
	init {
		for (i in seed.indices)
			seed[i] += 50
		outCipher = IsaacCipher(seed)
	}
	
}