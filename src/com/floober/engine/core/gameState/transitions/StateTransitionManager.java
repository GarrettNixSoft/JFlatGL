package com.floober.engine.core.gameState.transitions;

import com.floober.engine.core.gameState.GameStateManager;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.time.Timer;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class StateTransitionManager {

	private final GameStateManager gsm;

	private final Timer timer;
	private int targetState;
	private int transitionState; // 0 = fade out, 1 = fade in

	private final Vector3f transitionColor = new Vector3f(Colors.BLACK_3);

	public StateTransitionManager(GameStateManager gsm) {
		this.gsm = gsm;
		timer = new Timer(1);
	}

	public void startTransition(int targetState, float time, Vector3f transitionColor) {
		this.transitionState = 0;
		setTargetState(targetState);
		setTransitionColor(transitionColor);
		timer.restart(time);
		TransitionFader.setDoFade(true);
	}

	public boolean transitionInProgress() {
		return timer.started();
	}

	public boolean transitionComplete() {
		return transitionState == 1 && timer.finished();
	}

	// RUNNING TRANSITIONS
	public void update() {
		if (timer.started()) { // transition in progress
			if (timer.finished()) { // this stage done
				if (transitionState == 0) { // stage 0 complete; set new state and reset timer
					gsm.setState(targetState);
					transitionState++;
					timer.restart();
				} else { // transition finished; reset the timer so it can be used again
					timer.reset();
					TransitionFader.setDoFade(false);
				}
			}

		}
	}

	public void render() {
		if (timer.started()) { // transition in progress
			float alpha = (transitionState == 0) ? timer.getProgress() : 1f - timer.getProgress();
//			Logger.log("State Transition Alpha = " + alpha);
//			Render.fillScreen(new Vector4f(transitionColor, alpha), Config.NEAR_CLIP);
			TransitionFader.setFadeColor(new Vector4f(transitionColor, alpha));
			TransitionFader.render();
//			Logger.log("Render transition fade");
		}
	}

	// GETTERS
	public int getTargetState() {
		return targetState;
	}
	public Vector3f getTransitionColor() {
		return transitionColor;
	}

	// SETTERS
	public void setTargetState(int targetState) {
		this.targetState = targetState;
	}
	public void setTransitionColor(Vector3f transitionColor) {
		this.transitionColor.set(transitionColor);
	}

}