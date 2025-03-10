package entity;

import java.util.function.Function;

public class FixedTrajectory implements Trajectory{

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
    @Override
    public float getX(NonPlayerEntity entity) {
        if(relativeTrajectory){
            return entity.getStartingPosX() + trajectoryFunctionX.apply(entity.getLifetimeSeconds());
        }
        return trajectoryFunctionX.apply(entity.getLifetimeSeconds());
    }

    @Override
    public float getY(NonPlayerEntity entity) {
        if(relativeTrajectory){
            return entity.getStartingPosY() + trajectoryFunctionY.apply(entity.getLifetimeSeconds());
        }
        return trajectoryFunctionY.apply(entity.getLifetimeSeconds());
    }
}
