package com.floober.engine.renderEngine.models;

import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class ModelLoader {

	private static final List<Integer> vaos = new ArrayList<>();
	private static final List<Integer> vbos = new ArrayList<>();

	public static QuadModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return new QuadModel(vaoID, indices.length);
	}

	public static QuadModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return new QuadModel(vaoID, positions.length / 2);
	}

	public static QuadModel loadToVAO(float[] positions, int coordSize) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, coordSize, positions);
		unbindVAO();
		return new QuadModel(vaoID, positions.length / 2);
	}

	public static int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	// Special method specifically for GUIText instances, allowing them to delete all their old vertex data when they generate new data.
	public static int loadToVAO(float[] positions, float[] textureCoords, GUIText guiText) {
		int vaoID = createVAO();
		guiText.addVbo(storeDataInAttributeList(0, 2, positions));
		guiText.addVbo(storeDataInAttributeList(1, 2, textureCoords));
		unbindVAO();
		return vaoID;
	}

	public static int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	public static int createEmptyVBO(int floatCount) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, floatCount * 4, GL_STREAM_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public static int createLineVBO() {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, 4 * 4, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public static void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindVertexArray(vao);
		glVertexAttribPointer(attribute, dataSize, GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		glVertexAttribDivisor(attribute, 1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	private static int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private static void unbindVAO() {
		glBindVertexArray(0);
	}

	private static void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	// TEST
	public static int deletedPerFrame = 0;
	// END_TEST

	public static void deleteVAO(int vao) {
		glDeleteVertexArrays(vao);
		vaos.remove(Integer.valueOf(vao));
		deletedPerFrame++;
	}

	public static void deleteVBO(int vbo) {
		glDeleteBuffers(vbo);
		vbos.remove(Integer.valueOf(vbo));
	}

	public static void cleanUp() {
		System.out.println("Cleaning up " + vaos.size() + " vaos and " + vbos.size() + " vbos");
		for (int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
	}

}