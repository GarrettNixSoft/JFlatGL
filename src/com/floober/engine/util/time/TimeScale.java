package com.floober.engine.util.time;

/*
    @author Floober
    The TimeScale class allows for scalar manipulation of in-game time. This can be used as an
    effect for slow-motion or speeding things up. The DisplayManager class has multiple methods
    for getting time elapsed; the default method will use the scalar from TimeScale to modify
    the reported passage of time, causing all time-dependent systems to react as if time were
    slower or faster.

    For systems that should not be bound to any time changes, there is a method to get Raw Time,
    which bypasses the scalar manipulation.
 */
public class TimeScale {

    // time scalar
    private static float TIME_SCALE = 1.0f;

    // change time over time
    private static long startTime;
    private static long endTime;
    private static long period;
    private static float startScale;
    private static float targetScale = TIME_SCALE;
    private static float delta;

    // GET TIME SCALE
    public static float getTimeScale() {
        return TIME_SCALE;
    }

    // GET SCALED TIME
    public static long getScaledTime(long start) {
        return (long) ((System.nanoTime() - start) / 1_000_000 * getTimeScale());
    }

    public static long getRawTime(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }

    // set instant time scale
    public static void setTimeScaleInstant(float scaleInstant) {
        TIME_SCALE = targetScale = scaleInstant;
    }

    // set gradual time scale
    public static void setTimeScaleTransition(float target, long targetTimeMillis) {
        // time
        startTime = System.nanoTime();
        endTime = startTime + (targetTimeMillis * 1_000_000);
        period = targetTimeMillis * 1_000_000;
        // scale
        startScale = TIME_SCALE;
        targetScale = target;
        delta = targetScale - startScale;
    }

    // update time scale
    public static void update() {
        if (targetScale != TIME_SCALE) {
            // currently in transition
            long elapsed = System.nanoTime() - startTime;
            // check complete
            if (elapsed >= period) {
                // complete
                TIME_SCALE = targetScale;
                return;
            }
            // set new scale
            float progress = (float) (elapsed) / (float) (endTime - startTime);
            TIME_SCALE = startScale + (delta * progress);
        }
    }

}
