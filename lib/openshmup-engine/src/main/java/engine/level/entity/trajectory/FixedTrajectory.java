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

    public void update(Entity entity, Level level) {
        Vec2D newPosition = new Vec2D(trajectoryFunctionX.apply(entity.getLifetimeSeconds()), trajectoryFunctionY.apply(entity.getLifetimeSeconds()));
        if (relativeTrajectory) {
            Vec2D startingPosition = entity.getTrajectoryReferencePosition();
            newPosition = newPosition.add(startingPosition);
        }
        entity.setPosition(newPosition);
    }

    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }
}
