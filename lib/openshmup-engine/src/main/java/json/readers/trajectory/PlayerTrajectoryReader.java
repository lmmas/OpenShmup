package json.readers.trajectory;

import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import static json.editionData.EditionData.Trajectory;

final public class PlayerTrajectoryReader implements TrajectoryReader {

    @Override
    public EditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.PlayerControlledTrajectory.id);
        float playerMovementSpeed = node.safeGetFloat(JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed);
        return Trajectory.PlayerControlledTrajectory(id, playerMovementSpeed);
    }

}
