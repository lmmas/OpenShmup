package engine.level.entity.trajectory;

import engine.level.Level;
import engine.level.entity.Entity;
import engine.types.Vec2D;

import java.util.function.Function;

final public class FixedTrajectory implements Trajectory {

    final private Function<Double, Float> trajectoryFunctionX;

    final private Function<Double, Float> trajectoryFunctionY;

    final private boolean relativeTrajectory;

    public FixedTrajectory(Function<Double, Float> trajectoryFunctionX, Function<Double, Float> trajectoryFunctionY, boolean relativeTrajectory) {
        this.trajectoryFunctionX = trajectoryFunctionX;
        this.trajectoryFunctionY = trajectoryFunctionY;
        this.relativeTrajectory = relativeTrajectory;
    }

    public FixedTrajectory(Function<Double, Float> trajectoryFunctionX, Function<Double, Float> trajectoryFunctionY) {
        this(trajectoryFunctionX, trajectoryFunctionY, true);
    }

    public void update(Entity entity, Level scene) {
        float newPosX = trajectoryFunctionX.apply(entity.getLifetimeSeconds());
        float newPosY = trajectoryFunctionY.apply(entity.getLifetimeSeconds());
        if (relativeTrajectory) {
            Vec2D startingPosition = entity.getTrajectoryReferencePosition();
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
