package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.LevelScene;

public interface Trajectory {
    Trajectory copyIfNotReusable();
    void update(Entity entity);
    void setScene(LevelScene scene);
    static Trajectory DEFAULT(){
        return EmptyTrajectory.getInstance();
    }
}
