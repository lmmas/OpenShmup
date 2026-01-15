package json.converters.trajectory;

import engine.level.entity.trajectory.PlayerControlledTrajectory;
import engine.level.entity.trajectory.Trajectory;
import json.SafeJsonNode;

public class PlayerTrajectoryFactory implements TrajectoryFactory {

    @Override
    public Trajectory fromJson(SafeJsonNode trajectoryNode) {
        float playerMovementSpeed = trajectoryNode.checkAndGetFloat("playerMovementSpeed");
        return new PlayerControlledTrajectory(playerMovementSpeed);
    }
}
