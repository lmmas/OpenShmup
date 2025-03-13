package engine.scene;

public class SceneTimer {
    private long referenceTimeMillis = 0;
    private boolean alreadyStarted = false;
    private long pausedTime = 0;
    private boolean isPaused = false;
    private float lastSceneTime = 0.0f;
    private long lastReadTimeMillis = 0;
    private float speed = 1.0f;
    public SceneTimer(){

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
