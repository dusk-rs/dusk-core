package rs.dusk.core.network.buffer

/**
 * Byte Encode Modification
 * @author Greg Hibberd <greg@greghibberd.com>
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
enum class Modifier {
	NONE,
	
	/**
	 * Add 128 to the byte (value + 2^7)
	 */
	ADD,
	
	/**
	 * Writes the inverse value of the byte (0 - value)
	 */
	INVERSE,
	
	/**
	 * Subtract 128 from the byte (value + 2^7)
	 */
	SUBTRACT;
}