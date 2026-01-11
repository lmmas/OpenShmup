package json.factories.trajectory;

import engine.entity.trajectory.Trajectory;
import json.SafeJsonNode;

public interface TrajectoryFactory {

    Trajectory fromJson(SafeJsonNode trajectoryNode);
}
