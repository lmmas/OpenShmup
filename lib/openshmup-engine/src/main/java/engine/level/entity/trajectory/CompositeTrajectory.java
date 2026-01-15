package engine.level.entity.trajectory;

import engine.level.Level;
import engine.level.entity.Entity;
import engine.types.Vec2D;

final public class CompositeTrajectory implements Trajectory {

    final private float[] bounds;

    final private Trajectory[] trajectories;

    private int trajectoryIndex;

    public CompositeTrajectory(float[] bounds, Trajectory[] trajectories) {
        assert bounds.length == trajectories.length - 1 : "Mismatching trajectories and bounds counts";
        this.bounds = bounds;
        this.trajectories = trajectories;
        this.trajectoryIndex = 0;
    }

    @Override
    public Trajectory copyIfNotReusable() {
        Trajectory[] newTrajectories = new Trajectory[trajectories.length];
        for (int i = 0; i < trajectories.length; i++) {
            newTrajectories[i] = trajectories[i].copyIfNotReusable();
        }
        return new CompositeTrajectory(bounds, newTrajectories);
    }

    @Override
    public void update(Entity entity, Level scene) {
        if (trajectoryIndex < bounds.length) {
            double currentTime = entity.getLifetimeSeconds();
            if (currentTime >= bounds[trajectoryIndex]) {
                trajectoryIndex++;
                Vec2D currentPosition = entity.getPosition();
                entity.setTrajectoryStartingPosition(currentPosition.x, currentPosition.y);
            }
        }
        trajectories[trajectoryIndex].update(entity, scene);
    }

}
