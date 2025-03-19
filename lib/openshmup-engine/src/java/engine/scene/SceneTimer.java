package engine.scene;

public class SceneTimer {
    private long referenceTimeMillis;
    private boolean alreadyStarted;
    private long pausedTime;
    private boolean isPaused;
    private float lastSceneTime;
    private long lastReadTimeMillis;
    private float speed;
    public SceneTimer(){
        this.referenceTimeMillis = 0;
        this.alreadyStarted = false;
        this.pausedTime = 0;
        this.isPaused = false;
        this.lastSceneTime = 0.0f;
        this.lastReadTimeMillis = 0;
        this.speed = 1.0f;
    }
    public void start(){
        if(!alreadyStarted){
            referenceTimeMillis = System.currentTimeMillis();
            lastReadTimeMillis = referenceTimeMillis;
            alreadyStarted = true;
        }
    }

    public void pause(){
        if(alreadyStarted){
            pausedTime = System.currentTimeMillis();
            isPaused = true;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void resume(){
        if(alreadyStarted && isPaused){
            long pauseIntervalMillis = System.currentTimeMillis() - pausedTime;
            referenceTimeMillis += pauseIntervalMillis;
            lastReadTimeMillis += pauseIntervalMillis;
            isPaused = false;
        }
    }

    public void reset(){
        alreadyStarted = false;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getTimeSeconds(){
        if(alreadyStarted){
            if(!isPaused){
                long currentTimeMillis = System.currentTimeMillis();
                float sceneTime = lastSceneTime + (float)(currentTimeMillis - lastReadTimeMillis) * speed / 1000.0f;
                lastReadTimeMillis = currentTimeMillis;
                lastSceneTime = sceneTime;
                return sceneTime;
            }
            return lastSceneTime;
        }
        return 0.0f;
    }
}
