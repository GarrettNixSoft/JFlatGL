package com.gnix.jflatgl.core.util;

public class StringUtils {

	public static boolean containsAny(String str, String... targets) {
		for (String target : targets) {
			if (str.contains(target)) {
				return true;
			}
		}
		return false;
	}

}
