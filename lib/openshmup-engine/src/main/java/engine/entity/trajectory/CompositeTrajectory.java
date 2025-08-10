package engine.entity.trajectory;

import engine.types.Vec2D;
import engine.entity.Entity;
import engine.scene.LevelScene;

final public class CompositeTrajectory implements Trajectory{
    final private float[] bounds;
    final private Trajectory[] trajectories;
    private int trajectoryIndex;

    public CompositeTrajectory(float[] bounds, Trajectory[] trajectories) {
        assert bounds.length == trajectories.length - 1: "Mismatching trajectories and bounds counts";
        this.bounds = bounds;
        this.trajectories = trajectories;
        this.trajectoryIndex = 0;
    }

    @Override
    public Trajectory copyIfNotReusable() {
        Trajectory[] newTrajectories = new Trajectory[trajectories.length];
        for(int i = 0; i < trajectories.length; i++){
            newTrajectories[i] = trajectories[i].copyIfNotReusable();
        }
        return new CompositeTrajectory(bounds, newTrajectories);
    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        if(trajectoryIndex < bounds.length){
            float currentTime = entity.getLifetimeSeconds();
            if(currentTime >= bounds[trajectoryIndex]){
                trajectoryIndex++;
                Vec2D currentPosition = entity.getPosition();
                entity.setTrajectoryStartingPosition(currentPosition.x, currentPosition.y);
            }
        }
        trajectories[trajectoryIndex].update(entity, scene);
    }

}
