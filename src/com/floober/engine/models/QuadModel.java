package com.floober.engine.models;

public class QuadModel {

	private final int vaoID;
	private final int vertexCount;

	public QuadModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}