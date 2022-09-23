package com.gnix.jflatgl.core.renderEngine.util;

import static org.lwjgl.opengl.GL11.*;

public class Stencil {

	public static void enableStencilWrite() {
		glEnable(GL_STENCIL_TEST);
		glStencilMask(0xFF);
		glStencilFunc(GL_NEVER, 1, 0xFF);
		glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
//		Logger.log("Stencil writing enabled");
	}

	public static void disableStencilWrite() {
		glDisable(GL_STENCIL_TEST);
		glStencilMask(0x00);
		glStencilFunc(GL_EQUAL, 1, 0x00);
//		Logger.log("Stencil writing disabled");
	}

}
