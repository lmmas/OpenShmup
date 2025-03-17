package engine.entity;

import engine.entity.trajectory.EmptyTrajectory;

public interface Trajectory {
    Trajectory copyIfNotReusable();
    void update(Entity entity);
    static Trajectory DEFAULT(){
        return EmptyTrajectory.getInstance();
    }
}
