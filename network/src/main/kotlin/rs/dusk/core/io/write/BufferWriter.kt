package rs.dusk.core.io.write

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.dusk.core.io.DataType
import rs.dusk.core.io.Endian
import rs.dusk.core.io.Modifier

/**
 * All functions relative to writing directly to a packet are done by this class
 *
 * @author Greg Hibberd <greg@greghibberd.com>
 * @author Tyluur <contact@kiaira.tech>
 * @since February 18, 2020
 */
open class BufferWriter(
        val buffer: ByteBuf = Unpooled.buffer()
) : Writer {
    private var bitIndex = 0

    override fun writeBytes(value: ByteArray): BufferWriter {
        buffer.writeBytes(value)
        return this
    }

    override fun writeBytes(value: ByteBuf): BufferWriter {
        buffer.writeBytes(value)
        return this
    }

    override fun writeBytes(data: ByteArray, offset: Int, length: Int): BufferWriter {
        buffer.writeBytes(data, offset, length)
        return this
    }

    override fun writeBytes(data: ByteBuf, offset: Int, length: Int): BufferWriter {
        buffer.writeBytes(data, offset, length)
        return this
    }


    override fun startBitAccess(): BufferWriter {
        bitIndex = buffer.writerIndex() * 8
        return this
    }

    override fun finishBitAccess(): BufferWriter {
        buffer.writerIndex((bitIndex + 7) / 8)
        return this
    }

    override fun writeBits(bitCount: Int, value: Boolean): BufferWriter {
        return writeBits(bitCount, if (value) 1 else 0)
    }

    override fun writeBits(bitCount: Int, value: Int): BufferWriter {
        var numBits = bitCount

        var bytePos = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        var requiredSpace = bytePos - buffer.writerIndex() + 1
        requiredSpace += (numBits + 7) / 8
        buffer.ensureWritable(requiredSpace)

        while (numBits > bitOffset) {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and BIT_MASKS[bitOffset].inv()
            tmp = tmp or (value shr numBits - bitOffset and BIT_MASKS[bitOffset])
            buffer.setByte(bytePos++, tmp)
            numBits -= bitOffset
            bitOffset = 8
        }
        if (numBits == bitOffset) {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and BIT_MASKS[bitOffset].inv()
            tmp = tmp or (value and BIT_MASKS[bitOffset])
            buffer.setByte(bytePos, tmp)
        } else {
            var tmp = buffer.getByte(bytePos).toInt()
            tmp = tmp and (BIT_MASKS[numBits] shl bitOffset - numBits).inv()
            tmp = tmp or (value and BIT_MASKS[numBits] shl bitOffset - numBits)
            buffer.setByte(bytePos, tmp)
        }
        return this
    }

    override fun skip(position: Int): BufferWriter {
        for (i in 0 until position) {
            writeByte(0)
        }
        return this
    }

    override fun position(): Int {
        return buffer.writerIndex()
    }

    override fun position(index: Int) {
        buffer.writerIndex(index)
    }

    override fun write(type: DataType, value: Number, modifier: Modifier, order: Endian) {
        val longValue = value.toLong()
        when (order) {
            Endian.BIG, Endian.LITTLE -> {
                val range = if (order == Endian.LITTLE) 0 until type.length else type.length - 1 downTo 0
                for (i in range) {
                    if (i == 0 && modifier != Modifier.NONE) {
                        when (modifier) {
                            Modifier.ADD -> buffer.writeByte((longValue + 128).toByte().toInt())
                            Modifier.INVERSE -> buffer.writeByte((-longValue).toByte().toInt())
                            Modifier.SUBTRACT -> buffer.writeByte((128 - longValue).toByte().toInt())
                            else -> throw IllegalArgumentException("Unknown byte modifier")
                        }
                    } else {
                        buffer.writeByte((longValue shr i * 8).toByte().toInt())
                    }
                }
            }
            Endian.MIDDLE -> {
                if (modifier != Modifier.NONE && modifier != Modifier.INVERSE) {
                    throw IllegalArgumentException("Middle endian doesn't support variable modifier $modifier")
                }

                if (type != DataType.INT) {
                    throw IllegalArgumentException("Middle endian can only be used with an integer")
                }

                val range = listOf(8, 0, 24, 16)
                //Reverse range if inverse modifier
                for (i in if (modifier == Modifier.NONE) range else range.reversed()) {
                    buffer.writeByte((longValue shr i).toByte().toInt())
                }
            }
        }
    }

    override fun toArray(): ByteArray {
        val data = ByteArray(position())
        System.arraycopy(buffer.array(), 0, data, 0, data.size)
        return data
    }

    companion object {
        /**
         * Bit masks for [writeBits]
         */
        private val BIT_MASKS = IntArray(32)

        init {
            for (i in BIT_MASKS.indices)
                BIT_MASKS[i] = (1 shl i) - 1
        }
    }
}