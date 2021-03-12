package com.floober.engine.renderEngine.particles.systems;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.geometry.CircleElement;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.particles.types.Particle;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.time.Timer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ParticleInfluenceField {

	private final Vector4f bounds;
	private final Vector2f force;
	private final Timer life;

	public ParticleInfluenceField(Vector4f bounds, Vector2f force, float life) {
		this.bounds = bounds;
		this.force = force;
		this.life = new Timer(life);
		this.life.start();
	}

	// GETTERS
	public Vector4f bounds() { return bounds; }
	public Vector2f force() { return force; }

	public boolean expired() {
		return life.finished();
	}

	// Use this class
	public void influence(Particle particle) {
		float time = DisplayManager.getFrameTimeSeconds();
		Vector2f forceApplied = new Vector2f(force).mul(time);
		particle.getVelocity().add(forceApplied);
	}

	// Visualize
	public void render() {
		// show the bounds of this field
		OutlineElement outline = new OutlineElement(Colors.GREEN, bounds, 0, 2, false);
		Render.drawOutline(outline);
		// Crosshairs to center
		LineElement horizCross = new LineElement(Colors.GREEN, bounds.x, bounds.y + (bounds.w - bounds.y) / 2,
				bounds.z, bounds.y + (bounds.w - bounds.y) / 2, 0, 2);
		LineElement vertiCross = new LineElement(Colors.GREEN, bounds.x + (bounds.z - bounds.x) / 2,
				bounds.y, bounds.x + (bounds.z - bounds.x) / 2, bounds.w, 0, 2);
		Render.drawLine(horizCross);
		Render.drawLine(vertiCross);
		// show the influence direction
		Vector2f polar = MathUtil.getPolar(force);
		CircleElement circle = new CircleElement(Colors.RED, bounds.x + (bounds.z - bounds.x) / 2, bounds.y + (bounds.w - bounds.y) / 2,
				0, polar.x * 0.9f, polar.x, polar.y / -360f + 0.5f, -90);
		Render.drawCircle(circle);
		// show the remaining life
		CircleElement lifeCircle = new CircleElement(Colors.CYAN, bounds.x + (bounds.z - bounds.x) / 2, bounds.y + (bounds.w - bounds.y) / 2,
				0, polar.x * 0.4f, polar.x * 0.5f, 1 - life.getProgress(), 0);
		Render.drawCircle(lifeCircle);
	}

}