package engine.level.entity.trajectory;

import engine.level.Level;
import engine.level.entity.Entity;

public interface Trajectory {

    void update(Entity entity, Level level);

    Trajectory copyIfNotReusable();

    static Trajectory DEFAULT_EMPTY() {
        return EmptyTrajectory.getInstance();
    }
}
