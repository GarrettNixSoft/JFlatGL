package com.floober.engine.core.renderEngine.particles.systems;

import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.particles.ParticleTexture;
import com.floober.engine.core.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.FadeInOutBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.FireScreenBehavior;
import com.floober.engine.core.renderEngine.particles.emitters.LightParticleEmitter;
import com.floober.engine.core.renderEngine.particles.types.LightParticle;
import com.floober.engine.core.renderEngine.particles.types.Particle;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.input.KeyInput;
import com.floober.engine.core.util.math.Collisions;
import com.floober.engine.core.util.math.MathUtil;
import com.floober.engine.core.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class FireScreenParticleSystem extends ParticleSystem {

	// fire system parameters
	private ParticleInfluenceField baseInfluence;
	private final List<ParticleInfluenceField> influenceFields;
	private final int numInfluenceFields;
	private final int minInfluenceWidth, maxInfluenceWidth;
	private final int minInfluenceHeight, maxInfluenceHeight;
	private final float minInfluence, maxInfluence;
	private final float minBaseInfluence, maxBaseInfluence;

	// time parameters
	private final float minInfluenceLife, maxInfluenceLife;

	// particle emitter
	private LightParticleEmitter particleEmitter;
	private FireScreenBehavior particleBehavior;

	// toggle rendering for debug
	private boolean renderInfluenceDebug = false;

	public FireScreenParticleSystem() {
		influenceFields = new ArrayList<>();
		numInfluenceFields = 8;
		minInfluenceWidth = 300;
		maxInfluenceWidth = Config.INTERNAL_WIDTH;
		minInfluenceHeight = 150;
		maxInfluenceHeight = Config.INTERNAL_HEIGHT;
		minInfluence = 10;
		maxInfluence = 80;
		minBaseInfluence = 20;
		maxBaseInfluence = 50;
		minInfluenceLife = 3;
		maxInfluenceLife = 12;
		// start with base influence
		baseInfluence = new ParticleInfluenceField(screenBoundsWithPadding(400, 400), generateBaseForce(), RandomUtil.getFloat(minInfluenceLife, maxInfluenceLife));
		// create emitter
		initEmitter();
	}

	private void initEmitter() {
		// get the glow texture
		ParticleTexture particleTexture = new ParticleTexture(ImageLoader.loadTextureConverted("textures/particles/glow.png"), 1, true);
		// create and configure the particle behavior attributes
		ParticleBehavior particleBehavior = new ParticleBehavior(this.particleBehavior = new FireScreenBehavior(this),
																new FadeInOutBehavior(1, 0.15f, 0.5f));
		particleBehavior.getAppearanceBehavior().initSize(6, 200);
		particleBehavior.getMovementBehavior().initSpeed(100, 120);
		particleBehavior.initLife(2f, 4f);
		particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_RED_ORANGE);
		// now, create the emitter
		particleEmitter = new LightParticleEmitter(new Vector3f(DisplayManager.getPrimaryGameWindow().centerX(),
				Config.INTERNAL_HEIGHT * 1.1f, 0), particleTexture, particleBehavior);
		// particle settings
		particleEmitter.initInnerRadius(2, 8);
		particleEmitter.initOuterRadius(5, 12);
		particleEmitter.initLightIntensity(0.1f, 0.6f);
		particleEmitter.initLightRadius(13, 20);
		particleEmitter.setLightMode(LightParticle.SMOOTH);
		particleEmitter.setLightColor(Colors.RED_ORANGE_3);
		// generation area
		particleEmitter.setBoxMode(true);
		particleEmitter.initPositionDelta(-Config.INTERNAL_WIDTH / 1.5f, Config.INTERNAL_WIDTH / 1.5f);
		particleEmitter.initPositionVerticalDelta(-Config.INTERNAL_HEIGHT / 16f, Config.INTERNAL_HEIGHT / 16f);
		// timing and quantity
		particleEmitter.setBatchCount(5);
		particleEmitter.setParticleDelay(0.05f);
	}

	public void update() {
		// update emitter
		particleEmitter.update();
		// remove dead fields
		for (int i = 0; i < influenceFields.size(); i++) {
			if (influenceFields.get(i).expired()) {
				influenceFields.remove(i);
				i--;
			}
		}
		if (baseInfluence.expired()) baseInfluence = new ParticleInfluenceField(screenBoundsWithPadding(400, 400), generateBaseForce(),
															RandomUtil.getFloat(minInfluenceLife, maxInfluenceLife));
		// next, refill fields
		while (influenceFields.size() < numInfluenceFields) {
			generateInfluenceField();
		}
		// toggle render?
		if (KeyInput.isPressed(KeyInput.R)) renderInfluenceDebug = !renderInfluenceDebug;
	}

	public void render() {
		if (renderInfluenceDebug) {
			particleEmitter.renderBounds();
			baseInfluence.render();
			for (ParticleInfluenceField influenceField : influenceFields) influenceField.render();
		}
	}

	private void generateInfluenceField() {
		Vector4f bounds = generateBounds();
		// generate force
		Vector2f force = generateForce();
		// generate life duration
		float life = RandomUtil.getFloat(minInfluenceLife, maxInfluenceLife);
		// decrease force based on distance from bottom
		float y = (bounds.y + (bounds.w - bounds.y) / 2) / Config.INTERNAL_WIDTH; // y is a percentage representing how far down the range center is on screen
		float proportion = Math.max(1, y + 0.2f);
		force.mul(proportion);
		// add results to list as a ParticleInfluenceField object
		influenceFields.add(new ParticleInfluenceField(bounds, force, life));
	}

	private Vector4f generateBounds() {
		float centerX = RandomUtil.getFloat(Config.INTERNAL_WIDTH);
		float centerY = RandomUtil.getFloat(Config.INTERNAL_HEIGHT);
		float width = RandomUtil.getFloat(minInfluenceWidth, maxInfluenceWidth);
		float height = RandomUtil.getFloat(minInfluenceHeight, maxInfluenceHeight);
		return Collisions.createCollisionBox(new Vector2f(centerX, centerY), new Vector2f(width, height));
	}

	private Vector2f generateForce() {
		float magnitude = RandomUtil.getFloat(minInfluence, maxInfluence);
		float direction = RandomUtil.getFloat(-230, 50);
		Vector2f force = new Vector2f(magnitude, direction);
		return MathUtil.getCartesian(force);
	}

	private Vector2f generateBaseForce() {
		float magnitude = RandomUtil.getFloat(minInfluence, maxInfluence);
		float direction = RandomUtil.getFloat(-150, -30);
		Vector2f force = new Vector2f(magnitude, direction);
		return MathUtil.getCartesian(force);
	}

	private Vector4f screenBoundsWithPadding(int paddingX, int paddingY) {
		Vector4f screenBounds = Config.getScreenBounds();
		screenBounds.x -= paddingX;
		screenBounds.y -= paddingY;
		screenBounds.z += paddingX;
		screenBounds.w += paddingY;
		return screenBounds;
	}

	@Override
	public void interact(Particle particle) {
		// base influence
		if (Collisions.contains(baseInfluence.bounds(), particle.getPosition2f())) baseInfluence.influence(particle);
		// influence zones
		for (ParticleInfluenceField influenceField : influenceFields) {
			if (Collisions.contains(influenceField.bounds(), particle.getPosition2f())) {
				influenceField.influence(particle);
			}
		}
	}
}