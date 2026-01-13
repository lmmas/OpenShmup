package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.Level;

public interface Trajectory {

    void update(Entity entity, Level scene);

    Trajectory copyIfNotReusable();

    static Trajectory DEFAULT_EMPTY() {
        return EmptyTrajectory.getInstance();
    }
}
