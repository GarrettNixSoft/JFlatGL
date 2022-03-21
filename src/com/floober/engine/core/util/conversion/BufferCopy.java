package com.floober.engine.core.util.conversion;

import java.nio.ByteBuffer;

public class BufferCopy {

	/**
	 * Copy a ByteBuffer.
	 * @param original The ByteBuffer to copy.
	 * @return a deep copy of the given ByteBuffer.
	 *
	 * Thanks to jdmichal on StackOverflow for this code.
	 */
	public static ByteBuffer copyByteBuffer(ByteBuffer original) {
		// Create clone with same capacity as original.
		final ByteBuffer clone = (original.isDirect()) ?
				ByteBuffer.allocateDirect(original.capacity()) :
				ByteBuffer.allocate(original.capacity());

		// Create a read-only copy of the original.
		// This allows reading from the original without modifying it.
		final ByteBuffer readOnlyCopy = original.asReadOnlyBuffer();

		// Flip and read from the original.
		readOnlyCopy.flip();
		clone.put(readOnlyCopy);

		clone.position(original.position());
		clone.limit(original.limit());
		clone.order(original.order());

		return clone;
	}

}