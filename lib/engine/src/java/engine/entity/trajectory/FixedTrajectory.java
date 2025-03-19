package engine.entity.trajectory;

import engine.Vec2D;
import engine.entity.Entity;
import engine.entity.Trajectory;

import java.util.function.Function;

public class FixedTrajectory implements Trajectory {

    final private Function<Float, Float> trajectoryFunctionX;
    final private Function<Float, Float> trajectoryFunctionY;
    final private boolean relativeTrajectory;
    public FixedTrajectory(Function<Float, Float> trajectoryFunctionX, Function<Float, Float> trajectoryFunctionY, boolean relativeTrajectory) {
        this.trajectoryFunctionX = trajectoryFunctionX;
        this.trajectoryFunctionY = trajectoryFunctionY;
        this.relativeTrajectory = relativeTrajectory;
    }

    public FixedTrajectory(Function<Float, Float> trajectoryFunctionX, Function<Float, Float> trajectoryFunctionY) {
        this(trajectoryFunctionX, trajectoryFunctionY, true);
    }

    public void update(Entity entity){
        float newPosX = trajectoryFunctionX.apply(entity.getLifetimeSeconds());
        float newPosY = trajectoryFunctionY.apply(entity.getLifetimeSeconds());
        if(relativeTrajectory){
            Vec2D startingPosition = entity.getStartingPosition();
            newPosX += startingPosition.x;
            newPosY += startingPosition.y;
        }
        entity.setPosition(newPosX, newPosY);
    }

    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }
}
