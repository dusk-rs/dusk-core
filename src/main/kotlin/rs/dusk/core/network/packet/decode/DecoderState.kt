package rs.dusk.core.network.packet.decode

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since December 17, 2020
 */
enum class DecoderState {
	
	DECODE_OPCODE,
	
	DECODE_LENGTH,
	
	DECODE_PAYLOAD
}