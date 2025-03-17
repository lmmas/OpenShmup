package engine.entity;

import engine.Vec2D;
import engine.scene.LevelScene;
import engine.scene.Spawnable;

public record EntitySpawnInfo(int id, Vec2D startingPosition, Trajectory trajectory) implements Spawnable {

    public EntitySpawnInfo(int id, float startingPositionX, float startingPositionY, Trajectory trajectory) {
        this(id, new Vec2D(startingPositionX, startingPositionY), trajectory);
    }

    public EntitySpawnInfo(int id, float startingPositionX, float startingPositionY) {
        this(id, new Vec2D(startingPositionX, startingPositionY), null);
    }

    public void spawn(LevelScene scene) {
        Entity newEntity = scene.getCustomEntityManager().createCustomEntity(scene, id, startingPosition);
        if(trajectory != null){
            newEntity.setTrajectory(trajectory);
        }
        scene.addEntity(newEntity);
    }
}
