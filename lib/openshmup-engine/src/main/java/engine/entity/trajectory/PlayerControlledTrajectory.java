package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.GameControl;
import engine.scene.LevelScene;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerControlledTrajectory implements Trajectory{
    private LevelScene scene;
    private float speed;
    private float lastUpdateTimeSeconds;

    public PlayerControlledTrajectory(LevelScene scene, float speed) {
        this.scene = scene;
        this.speed = speed;
        this.lastUpdateTimeSeconds = scene.getSceneTimeSeconds();
    }

    @Override
    public void update(Entity entity) {
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
