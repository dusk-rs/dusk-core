package rs.dusk.core.network.buffer

import rs.dusk.core.network.buffer.Modifier.INVERSE

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
*/
enum class Endian {
    BIG,
    LITTLE,
    MIDDLE;

    fun getRange(modifier: Modifier, byteCount: Int) = when (this) {
        BIG -> byteCount - 1 downTo 0
        LITTLE -> 0 until byteCount
        MIDDLE -> if (modifier == INVERSE) MID_ENDIAN_INVERSE else MID_ENDIAN_ORDER
    }

    companion object {
        private val MID_ENDIAN_ORDER = listOf(1, 0, 3, 2)
        private val MID_ENDIAN_INVERSE = MID_ENDIAN_ORDER.reversed()
    }
}