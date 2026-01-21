package engine.level.entity.trajectory;

import engine.level.Level;
import engine.level.entity.Entity;

final public class EmptyTrajectory implements Trajectory {

    private static EmptyTrajectory instance = null;

    private EmptyTrajectory() {

    }

    public static EmptyTrajectory getInstance() {
        if (instance == null) {
            instance = new EmptyTrajectory();
        }
        return instance;
    }

    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }

    @Override
    public void update(Entity entity, Level level) {

    }

}
