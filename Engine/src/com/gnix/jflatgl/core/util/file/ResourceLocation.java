package com.gnix.jflatgl.core.util.file;

import java.io.InputStream;
import java.net.URL;

/*
	Copied from the Slick2D library.
 */
public interface ResourceLocation {
	InputStream getResourceAsStream(String var1);

	URL getResource(String var1);
}
