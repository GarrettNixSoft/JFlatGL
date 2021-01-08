package com.floober.engine.renderEngine.particles;

import com.floober.engine.game.Game;
import com.floober.engine.renderEngine.textures.Texture;
import org.json.JSONObject;

public record ParticleTexture(Texture texture, int numRows, boolean useAdditiveBlend) {

	public int id() { return texture.id(); }
	public int width() { return texture.width(); }
	public int height() { return texture.height(); }
	public int numTextures() { return numRows * numRows; }
	public boolean additiveBlend() { return useAdditiveBlend; }

	public static ParticleTexture createParticleTextureFromJSON(JSONObject json, Game game) {
		Texture texture = game.getTexture(json.getString("name"));
		int rows = json.getInt("rows");
		boolean additive = json.getBoolean("additiveBlend");
		return new ParticleTexture(texture, rows, additive);
	}

}