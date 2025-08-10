package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.LevelScene;

public interface Trajectory {
    void update(Entity entity, LevelScene scene);
    Trajectory copyIfNotReusable();
    static Trajectory DEFAULT_EMPTY(){
        return EmptyTrajectory.getInstance();
    }
}
