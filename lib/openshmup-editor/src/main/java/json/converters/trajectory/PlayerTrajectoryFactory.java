package json.converters.trajectory;

import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.entity.trajectory.Trajectory;
import json.SafeJsonNode;

public class PlayerTrajectoryFactory implements TrajectoryFactory {

    @Override
    public Trajectory fromJson(SafeJsonNode trajectoryNode) {
        float playerMovementSpeed = trajectoryNode.checkAndGetFloat("playerMovementSpeed");
        return new PlayerControlledTrajectory(playerMovementSpeed);
    }
}
