package com.gnix.jflatgl.core.renderEngine.particles;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.particles.types.LightParticle;
import com.gnix.jflatgl.core.renderEngine.particles.types.Particle;
import com.gnix.jflatgl.core.renderEngine.particles.types.TexturedParticle;
import com.gnix.jflatgl.core.renderEngine.renderers.ParticleLightRenderer;
import com.gnix.jflatgl.core.renderEngine.renderers.ParticleRenderer;
import com.gnix.jflatgl.core.renderEngine.renderers.ParticleTexturedRenderer;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.Sorting;
import org.joml.Vector4f;

import java.util.*;

public class ParticleMaster {

	@SuppressWarnings("unchecked") // Regular particles (Source or not)
	private final Map<ParticleTexture, List<Particle>>[] particles = new HashMap[Layers.NUM_LAYERS];
	private ParticleRenderer particleRenderer;

	@SuppressWarnings("unchecked") // Light-emitting particles
	private final List<LightParticle>[] lightEmittingParticles = new ArrayList[Layers.NUM_LAYERS];
	private ParticleLightRenderer lightParticleRenderer;

	@SuppressWarnings("unchecked") // Textured particles
	private final Map<ParticleTexture, List<TexturedParticle>>[] texturedParticles = new HashMap[Layers.NUM_LAYERS];
	private ParticleTexturedRenderer texturedParticleRenderer;

	// Total particle counter
	public int numParticles = 0;

	public static ParticleTexture GLOW_PARTICLE_TEXTURE;

	private final int RENDER_LAYERS;

	public ParticleMaster(int layers) {
		this.RENDER_LAYERS = layers;
	}

	/**
	 * Initialize the Particle Renderers and the Particle Collections for each layer.
	 */
	public void init() {
		particleRenderer = new ParticleRenderer();
		lightParticleRenderer = new ParticleLightRenderer();
		texturedParticleRenderer = new ParticleTexturedRenderer();
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			particles[i] = new HashMap<>();
			lightEmittingParticles[i] = new ArrayList<>();
			texturedParticles[i] = new HashMap<>();
		}
	}

	public static void initGlobals() {
		// init global particle textures
		GLOW_PARTICLE_TEXTURE = new ParticleTexture(Game.getTexture("glow_particle"), 1, true);
	}

	public void addParticle(Particle particle) {
		// enforce hard particle limit
		if (numParticles >= ParticleRenderer.MAX_INSTANCES) {
			Logger.logError("Too many particles!");
			return;
		}
		// if limit not exceeded, add the particle
		int layer = (int) particle.getLayer();
		// add the particle to its corresponding type collection
		if (particle instanceof LightParticle lp) {
			lightEmittingParticles[layer].add(lp);
		}
		else if (particle instanceof TexturedParticle tp) {
			List<TexturedParticle> particleList = texturedParticles[layer].computeIfAbsent(particle.getTexture(), _ -> new ArrayList<>());
			particleList.add(tp);
		}
		else {
			List<Particle> particleList = particles[layer].computeIfAbsent(particle.getTexture(), k -> new ArrayList<>());
			particleList.add(particle);
		}
		numParticles++;
	}

	public void update() {

		// FOR each LAYER
		for (int i = 0; i < RENDER_LAYERS; ++i) {

			// get the key set to iterate over
			Set<ParticleTexture> keySet = particles[i].keySet();
			Iterator<ParticleTexture> iterator = keySet.iterator();

			// update each batch of particles
			while (iterator.hasNext()) {

				// get this batch of particles
				ParticleTexture particleTexture = iterator.next();
				List<Particle> particleList = particles[i].get(particleTexture);

				// get the size of the particle list before cleaning it up
				int startSize = particleList.size();

				// update every particle, removing them if their life span has been reached
				particleList.removeIf(particle -> !particle.update());

				// check the remaining size; subtract the number removed from the particle counter
				int endSize = particleList.size();
				if (endSize < startSize) {
					numParticles -= (startSize - endSize);
				}

				// if this list has no more active particles, remove it
				if (particleList.isEmpty())
					iterator.remove();

				// otherwise, sort the list of particles if necessary
				if (!particleTexture.useAdditiveBlend())
					Sorting.insertionSort(particleList, Particle::compareTo);

			}

			// repeat the above for the textured particles
			// get the key set to iterate over
			keySet = texturedParticles[i].keySet();
			iterator = keySet.iterator();

			// update each batch of textured particles
			while (iterator.hasNext()) {

				// get this batch of particles
				ParticleTexture particleTexture = iterator.next();
				List<TexturedParticle> particleList = texturedParticles[i].get(particleTexture);

				// get the size of the particle list before cleaning it up
				int startSize = particleList.size();

				// update every particle, removing them if their life span has been reached
				particleList.removeIf(particle -> !particle.update());

				// check the remaining size; subtract the number removed from the particle counter
				int endSize = particleList.size();
				if (endSize < startSize)
					numParticles -= (startSize - endSize);

				// if this list has no more active particles, remove it
				if (particleList.isEmpty())
					iterator.remove();

				// otherwise, sort the list of particles if necessary
				if (!particleTexture.useAdditiveBlend())
					Sorting.insertionSort(particleList, Particle::compareTo);

			}

			// Update and remove dead LMPs
			int size = lightEmittingParticles[i].size();
			if (lightEmittingParticles[i].removeIf(particle -> !particle.update()))
				numParticles -= (size - lightEmittingParticles[i].size());
		}

	}

	public void renderParticles(int layer) {
		particleRenderer.render(particles[layer]);
		lightParticleRenderer.render(lightEmittingParticles[layer]);
		texturedParticleRenderer.render(texturedParticles[layer]);
	}

	public void cleanUp() {
		particleRenderer.cleanUp();
		lightParticleRenderer.cleanUp();
		texturedParticleRenderer.cleanUp();
	}

	// GETTERS
	public int getParticleCount() {
		int total = 0;
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			for (ParticleTexture particleTexture : particles[i].keySet()) {
				total += particles[i].get(particleTexture).size();
			}
			for (ParticleTexture particleTexture : texturedParticles[i].keySet()) {
				total += texturedParticles[i].get(particleTexture).size();
			}
			total += lightEmittingParticles[i].size();
		}
		return total;
	}

	// ACTIONS
	public void setColorForAllParticlesOfType(ParticleTexture key, Vector4f color) {
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			List<Particle> particleList = particles[i].get(key);
			for (Particle particle : particleList) {
				particle.setColor(color.x(), color.y(), color.z(), particle.getColor().w());
			}
		}
	}

}
