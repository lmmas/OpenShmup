package engine.entity.trajectory;

import engine.entity.Entity;

public interface Trajectory {
    Trajectory copyIfNotReusable();
    void update(Entity entity);
    static Trajectory DEFAULT(){
        return EmptyTrajectory.getInstance();
    }
}
