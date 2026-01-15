package json.converters.trajectory;

import engine.level.entity.trajectory.FixedTrajectory;
import engine.level.entity.trajectory.Trajectory;
import json.SafeJsonNode;

import java.util.function.Function;

import static json.factories.TrajectoryFactories.convertToFunction;

public class FixedTrajectoryFactory implements TrajectoryFactory {

    @Override
    public Trajectory fromJson(SafeJsonNode trajectoryNode) {
        String functionXString = trajectoryNode.checkAndGetString("functionX");
        String functionYString = trajectoryNode.checkAndGetString("functionY");
        Function<Double, Float> trajectoryFunctionX = convertToFunction(functionXString);
        Function<Double, Float> trajectoryFunctionY = convertToFunction(functionYString);
        return new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
    }
}
