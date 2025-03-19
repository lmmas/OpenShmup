package engine.entity;

import engine.EditorDataManager;
import engine.Game;
import engine.Vec2D;
import engine.scene.LevelScene;
import engine.scene.Spawnable;

public record EntitySpawnInfo(EditorDataManager editorDataManager, int id, Vec2D startingPosition, Trajectory trajectory) implements Spawnable {

    public EntitySpawnInfo(Game game, int id, float startingPositionX, float startingPositionY, Trajectory trajectory) {
        this(game.getCustomEntityManager(), id, new Vec2D(startingPositionX, startingPositionY), trajectory);
    }

    public EntitySpawnInfo(Game game, int id, float startingPositionX, float startingPositionY) {
        this(game.getCustomEntityManager(), id, new Vec2D(startingPositionX, startingPositionY), null);
    }

    public EntitySpawnInfo(Game game, int id, float startingPositionX, float startingPositionY, int trajectoryId) {
        this(game.getCustomEntityManager(), id, new Vec2D(startingPositionX, startingPositionY), game.getCustomEntityManager().getTrajectory(trajectoryId));
    }

    public void spawn(LevelScene scene) {
        Entity newEntity = editorDataManager.buildCustomEntity(scene, id);
        newEntity.setStartingPosition(startingPosition.x, startingPosition.y);
        if(trajectory != null){
            newEntity.setTrajectory(trajectory);
        }
        scene.addEntity(newEntity);
    }
}
