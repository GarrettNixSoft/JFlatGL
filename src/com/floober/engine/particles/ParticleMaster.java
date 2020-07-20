package com.floober.engine.particles;

import com.floober.engine.renderEngine.ParticleRenderer;
import com.floober.engine.util.Logger;
import com.floober.engine.util.Sorting;
import org.joml.Vector4f;

import java.util.*;

public class ParticleMaster {

	private static final Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
	private static ParticleRenderer renderer;
	private static int numParticles = 0;

	public static void init() {
		renderer = new ParticleRenderer();
	}

	public static void addParticle(Particle particle) {
		numParticles++;
		// enforce hard particle limit
		if (numParticles >= ParticleRenderer.MAX_INSTANCES) {
			Logger.logError("Too many particles!");
			return;
		}
		// if limit not exceeded, add the particle
		List<Particle> particleList = particles.computeIfAbsent(particle.getTexture(), k -> new ArrayList<>());
		particleList.add(particle);
	}

	public static void update() {
		// get the key set to iterate over
		Set<ParticleTexture> keySet = particles.keySet();
		Iterator<ParticleTexture> iterator = keySet.iterator();

		// update each batch of particles
		while (iterator.hasNext()) {

			// get this batch of particles
			ParticleTexture particleTexture = iterator.next();
			List<Particle> particleList = particles.get(particleTexture);

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
	}

	public static void renderParticles() {
		renderer.render(particles);
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	// GETTERS
	public static int getParticleCount() {
		int total = 0;
		for (ParticleTexture particleTexture : particles.keySet()) {
			total += particles.get(particleTexture).size();
		}
		return total;
	}

	// ACTIONS
	public static void setColorForAllParticlesOfType(ParticleTexture key, Vector4f color) {
		List<Particle> particleList = particles.get(key);
		for (Particle particle : particleList) {
			particle.setColor(color.x(), color.y(), color.z(), particle.getColor().w());
		}
	}

}