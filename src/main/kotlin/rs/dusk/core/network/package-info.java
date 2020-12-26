/**
 * The network codec for any implementation is composed of i/o functions regardless of the endpoint. This design
 * introduces an architecture to represent individual messages (regardless of the phase being decode, handle, or
 * encode), and swap between decoders, all via the same channel.
 * <p>
 * <p>
 * Channels additionally have a [session][Session] wrapper which provides higher level information about the network
 * connection, as well as high level functions.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since December 17, 2020
 */
package rs.dusk.core.network;