package json.converters.trajectory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.attribute.Attribute;
import json.editionData.PlayerControlledTrajectoryEditionData;
import json.editionData.TrajectoryEditionData;

import java.util.List;

final public class PlayerTrajectoryConverter implements TrajectoryConverter {

    @Override
    public TrajectoryEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.PlayerControlledTrajectory.id);
        float playerMovementSpeed = node.safeGetFloat(JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed);
        return new PlayerControlledTrajectoryEditionData(id, playerMovementSpeed);
    }

    @Override
    public ObjectNode toJson(TrajectoryEditionData trajectoryData, ObjectNode node) {
        PlayerControlledTrajectoryEditionData playerControlledTrajectoryData = (PlayerControlledTrajectoryEditionData) trajectoryData;

        List<Attribute> attributes = List.of(playerControlledTrajectoryData.getIdAttribute(), playerControlledTrajectoryData.getPlayerMovementSpeed());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
