package engine;

import lombok.Getter;
import lombok.Setter;

final public class Timer {

    private long referenceTimeMillis;

    private boolean alreadyStarted;

    private long pausedTime;
    @Getter
    private boolean isPaused;

    private double lastSceneTime;

    private long lastReadTimeMillis;
    @Setter
    private float speed;

    public Timer() {
        this.referenceTimeMillis = 0;
        this.alreadyStarted = false;
        this.pausedTime = 0;
        this.isPaused = false;
        this.lastSceneTime = 0.0d;
        this.lastReadTimeMillis = 0;
        this.speed = 1.0f;
    }

    public void start() {
        if (!alreadyStarted) {
            referenceTimeMillis = System.nanoTime();
            lastReadTimeMillis = referenceTimeMillis;
            alreadyStarted = true;
        }
    }

    public void pause() {
        if (alreadyStarted) {
            pausedTime = System.nanoTime();
            isPaused = true;
        }
    }

    public void resume() {
        if (alreadyStarted && isPaused) {
            long pauseIntervalMillis = System.nanoTime() - pausedTime;
            referenceTimeMillis += pauseIntervalMillis;
            lastReadTimeMillis += pauseIntervalMillis;
            isPaused = false;
        }
    }

    public void reset() {
        alreadyStarted = false;
    }

    public double getTimeSeconds() {
        if (alreadyStarted) {
            if (!isPaused) {
                long currentTimeMillis = System.nanoTime();
                double sceneTime = lastSceneTime + (double) (currentTimeMillis - lastReadTimeMillis) * speed / 1000000000.0;
                lastReadTimeMillis = currentTimeMillis;
                lastSceneTime = sceneTime;
                return sceneTime;
            }
            return lastSceneTime;
        }
        return 0.0d;
    }
}
