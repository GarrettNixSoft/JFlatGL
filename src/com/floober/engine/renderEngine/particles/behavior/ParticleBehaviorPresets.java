package com.floober.engine.renderEngine.particles.behavior;

import com.floober.engine.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.renderEngine.particles.behavior.movement.ConstantVelocityBehavior;
import com.floober.engine.renderEngine.particles.behavior.movement.MovementBehavior;

public class ParticleBehaviorPresets {

	public static ParticleBehavior ORB_BEHAVIOR;

	static {
		// ORB_BEHAVIOR
		AppearanceBehavior orbAppearance = new FadeOutBehavior(1, 0);
		orbAppearance.initSize(6, 200);
		MovementBehavior orbMotion = new ConstantVelocityBehavior(0, 360);
		orbMotion.initSpeed(100, 120);
		ORB_BEHAVIOR = new ParticleBehavior(orbMotion, orbAppearance);
		ORB_BEHAVIOR.initLife(2f, 4f);
	}

}