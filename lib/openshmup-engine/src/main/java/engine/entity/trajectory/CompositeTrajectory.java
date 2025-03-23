package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.LevelScene;

public class CompositeTrajectory implements Trajectory{
    final private float[] bounds;
    final private Trajectory[] trajectories;

    public CompositeTrajectory(float[] bounds, Trajectory[] trajectories) {
        assert bounds.length == trajectories.length - 1: "Mismatching trajectories and bounds counts";
        this.bounds = bounds;
        this.trajectories = trajectories;
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
    public void update(Entity entity) {
        float currentTime = entity.getLifetimeSeconds();
        for(int i = 0; i < bounds.length; i++){
            if(currentTime < bounds[i]){
                trajectories[i].update(entity);
                return;
            }
        }
        trajectories[bounds.length].update(entity);
    }

    @Override
    public void setScene(LevelScene scene) {
        for(var trajectory: trajectories){
            trajectory.setScene(scene);
        }
    }
}
