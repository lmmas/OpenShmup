package engine.entity.trajectory;

import engine.Engine;
import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.types.GameControl;
import engine.types.Vec2D;

final public class PlayerControlledTrajectory implements Trajectory {

    private final float speed;

    private double lastUpdateTimeSeconds;

    public PlayerControlledTrajectory(float speed) {
        this.speed = speed;
        this.lastUpdateTimeSeconds = 0.0d;
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        double currentTime = scene.getSceneTimeSeconds();
        float positionX = entity.getPosition().x;
        float positionY = entity.getPosition().y;
        double deltaTimeSeconds = currentTime - lastUpdateTimeSeconds;
        if (scene.getControlState(GameControl.MOVE_LEFT)) {
            positionX -= (float) (speed * deltaTimeSeconds);
        }
        if (scene.getControlState(GameControl.MOVE_RIGHT)) {
            positionX += (float) (speed * deltaTimeSeconds);
        }
        if (scene.getControlState(GameControl.MOVE_UP)) {
            positionY += (float) (speed * deltaTimeSeconds);
        }
        if (scene.getControlState(GameControl.MOVE_DOWN)) {
            positionY -= (float) (speed * deltaTimeSeconds);
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
