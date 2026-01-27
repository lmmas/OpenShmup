package json.factories;

import engine.level.entity.trajectory.FixedTrajectory;
import engine.level.entity.trajectory.PlayerControlledTrajectory;
import engine.level.entity.trajectory.Trajectory;
import json.JsonFieldNames;
import json.SafeJsonNode;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.function.Function;

public class TrajectoryFactories {

    final public static Function<SafeJsonNode, Trajectory> playerControlledTrajectoryFactory = node -> {
        float playerMovementSpeed = node.safeGetFloat(JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed);
        return new PlayerControlledTrajectory(playerMovementSpeed);
    };

    final public static Function<SafeJsonNode, Trajectory> fixedTrajectoryFactory = node -> {
        String functionXString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionX);
        String functionYString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionY);
        Function<Double, Float> trajectoryFunctionX = convertToFunction(functionXString);
        Function<Double, Float> trajectoryFunctionY = convertToFunction(functionYString);
        return new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
    };

    public static Function<Double, Float> convertToFunction(String expressionString) {
        return t -> {
            Expression expr = new ExpressionBuilder(expressionString)
                .variable("t")
                .build()
                .setVariable("t", t);
            return (float) expr.evaluate();
        };
    }
}
