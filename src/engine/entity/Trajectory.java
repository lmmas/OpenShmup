package engine.entity;

import engine.entity.trajectory.EmptyTrajectory;
import engine.entity.trajectory.FixedTrajectory;

import java.util.function.Function;

public interface Trajectory {
    Trajectory copyIfNotReusable();
    void update(Entity entity);
    static Trajectory DEFAULT(){
        return EmptyTrajectory.getInstance();
    }
}
