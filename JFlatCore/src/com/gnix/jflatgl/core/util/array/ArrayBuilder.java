package com.gnix.jflatgl.core.util.array;

import java.util.Arrays;

public class ArrayBuilder {

	public static byte[] buildByteArray(int size, byte value) {
		byte[] result = new byte[size];
		Arrays.fill(result, value);
		return result;
	}

}
