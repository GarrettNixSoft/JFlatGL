package com.gnix.jflatgl.core.renderEngine.particles;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import org.json.JSONObject;

public record ParticleTexture(TextureComponent texture, int numRows, boolean useAdditiveBlend) {

	public int id() { return texture.id(); }
	public int width() { return texture.width(); }
	public int height() { return texture.height(); }
	public int numTextures() { return numRows * numRows; }
	public boolean additiveBlend() { return useAdditiveBlend; }

	public static ParticleTexture createParticleTextureFromJSON(JSONObject json) {
		TextureComponent texture = Game.getTexture(json.getString("name"));
		int rows = json.getInt("rows");
		boolean additive = json.getBoolean("additiveBlend");
		return new ParticleTexture(texture, rows, additive);
	}

}
