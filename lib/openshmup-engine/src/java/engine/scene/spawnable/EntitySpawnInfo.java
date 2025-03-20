package engine.scene.spawnable;

import engine.EditorDataManager;
import engine.Vec2D;
import engine.entity.Entity;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;

public record EntitySpawnInfo(int id, Vec2D startingPosition, int trajectoryId) implements Spawnable {

    public EntitySpawnInfo(int id, float startingPositionX, float startingPositionY, int trajectoryId) {
        this(id, new Vec2D(startingPositionX, startingPositionY), trajectoryId);
    }
@Override
    public void spawn(EditorDataManager editorDataManager, LevelScene scene) {
        Entity newEntity = editorDataManager.buildCustomEntity(scene, id);
        newEntity.setStartingPosition(startingPosition.x, startingPosition.y);
        if(trajectoryId != -1){
            newEntity.setTrajectory(editorDataManager.getTrajectory(trajectoryId));
        }
        scene.addEntity(newEntity);
    }
}
