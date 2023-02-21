package com.gnix.jflatgl.core.renderEngine.elements.geometry;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.renderers.MasterRenderer;
import com.gnix.jflatgl.core.util.data.Pair;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class LineElement extends GeometryElement {

	private int vao;
	private int vbo;
	private float x2, y2;
	private FloatBuffer vertexData;

	// this is terrible
	private boolean temp;

	public LineElement(Vector4f color, float x1, float y1, float x2, float y2, int layer) {
		super(color, x1, y1, layer, false);
		this.x = x1;
		this.y = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.centered = false;
		vertexData = BufferUtils.createFloatBuffer(6);
		transform();
		initData();
	}

	public LineElement(Vector4f color, float x1, float y1, float x2, float y2, int layer, boolean temp) {
		super(color, x1, y1, layer, false);
		this.x = x1;
		this.y = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.centered = false;
		vertexData = BufferUtils.createFloatBuffer(6);
		transform();
		initData();
		// this is still terrible
		this.temp = temp;
	}

	private void initData() {

		glBindVertexArray(vao);

		float[] vertices = {
				DisplayManager.convertToScreenPos(x, true),
				DisplayManager.convertToScreenPos(y, false),
				MasterRenderer.getScreenZ(layer),
				DisplayManager.convertToScreenPos(x2, true),
				DisplayManager.convertToScreenPos(y2, false),
				MasterRenderer.getScreenZ(layer)
		};

		Pair<Integer, Integer> data = ModelLoader.loadToLineVAO(vertices, 3);
		vao = data.data1();
		vbo = data.data2();

	}

	public void setPosition(float x1, float y1, float x2, float y2, int layer) {
		this.x = x1;
		this.y = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.layer = layer;
		initData();
	}

	public int getVao() {
		return vao;
	}

	public int getVbo() {
		return vbo;
	}

	public FloatBuffer getVertexData() {
		return vertexData;
	}

	public boolean isTemp() {
		// dear lord this is horrendous
		return temp;
	}

	public void delete() {
		// I hate it so very much
		ModelLoader.deleteVAO(vao);
		ModelLoader.deleteVBO(vbo);
	}

}
