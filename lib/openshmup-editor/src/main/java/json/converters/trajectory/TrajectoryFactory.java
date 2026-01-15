package json.converters.trajectory;

import engine.level.entity.trajectory.Trajectory;
import json.SafeJsonNode;

public interface TrajectoryFactory {

    Trajectory fromJson(SafeJsonNode trajectoryNode);
}
