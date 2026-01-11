package json.factories;

import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.entity.trajectory.Trajectory;
import json.SafeJsonNode;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.function.Function;

public class TrajectoryFactories {

    final public static Function<SafeJsonNode, Trajectory> playerControlledTrajectoryFactory = node -> {
        float playerMovementSpeed = node.checkAndGetFloat("playerMovementSpeed");
        return new PlayerControlledTrajectory(playerMovementSpeed);
    };

    final public static Function<SafeJsonNode, Trajectory> fixedTrajectoryFactory = node -> {
        String functionXString = node.checkAndGetString("functionX");
        String functionYString = node.checkAndGetString("functionY");
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
