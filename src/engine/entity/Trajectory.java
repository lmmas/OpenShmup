package engine.entity;

public interface Trajectory {
    Trajectory copyIfNotReusable();
    void update(NonPlayerEntity entity);
}
