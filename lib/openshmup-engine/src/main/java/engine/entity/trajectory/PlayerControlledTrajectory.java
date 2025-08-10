package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.GameControl;
import engine.scene.LevelScene;

final public class PlayerControlledTrajectory implements Trajectory{
    private LevelScene scene;
    private float speed;
    private float lastUpdateTimeSeconds;

    public PlayerControlledTrajectory(float speed) {
        this.scene = null;
        this.speed = speed;
        this.lastUpdateTimeSeconds = 0.0f;
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        float currentTime = scene.getSceneTimeSeconds();
        float positionX = entity.getPosition().x;
        float positionY = entity.getPosition().y;
        float deltaTimeSeconds = currentTime - lastUpdateTimeSeconds;
        if (scene.getControlState(GameControl.MOVE_LEFT)) {
            positionX-=speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_RIGHT)) {
            positionX+=speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_UP)) {
            positionY+=speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_DOWN)) {
            positionY-=speed * deltaTimeSeconds;
        }
        if(positionX < 0.0f){
            positionX = 0.0f;
        }
        if(positionX > 1.0f){
            positionX = 1.0f;
        }
        if(positionY < 0.0f){
            positionY = 0.0f;
        }
        if(positionY > 1.0f){
            positionY = 1.0f;
        }
        entity.setPosition(positionX, positionY);
        lastUpdateTimeSeconds = currentTime;
    }

    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }
}
