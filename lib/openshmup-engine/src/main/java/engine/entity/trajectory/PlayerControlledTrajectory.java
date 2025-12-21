package engine.entity.trajectory;

import engine.Engine;
import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.types.GameControl;
import engine.types.Vec2D;

final public class PlayerControlledTrajectory implements Trajectory {
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
            positionX -= speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_RIGHT)) {
            positionX += speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_UP)) {
            positionY += speed * deltaTimeSeconds;
        }
        if (scene.getControlState(GameControl.MOVE_DOWN)) {
            positionY -= speed * deltaTimeSeconds;
        }
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        if (positionX < 0.0f) {
            positionX = 0.0f;
        }
        if (positionX > resolution.x) {
            positionX = resolution.x;
        }
        if (positionY < 0.0f) {
            positionY = 0.0f;
        }
        if (positionY > resolution.y) {
            positionY = resolution.y;
        }
        entity.setPosition(positionX, positionY);
        lastUpdateTimeSeconds = currentTime;
    }

    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }
}
