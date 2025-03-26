package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.LevelScene;

public interface Trajectory {
    void update(Entity entity);
    void setScene(LevelScene scene);
    Trajectory copyIfNotReusable();
    static Trajectory DEFAULT(){
        return EmptyTrajectory.getInstance();
    }
}
