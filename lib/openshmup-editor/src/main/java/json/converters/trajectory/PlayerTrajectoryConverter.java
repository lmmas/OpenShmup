package json.converters.trajectory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.PlayerControlledTrajectoryEditionData;
import editor.editionData.TrajectoryEditionData;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.List;

public class PlayerTrajectoryConverter implements TrajectoryConverter {

    @Override
    public TrajectoryEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.PlayerControlledTrajectory.id);
        float playerMovementSpeed = node.safeGetFloat(JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed);
        return new PlayerControlledTrajectoryEditionData(id, playerMovementSpeed);
    }

    @Override
    public ObjectNode toJson(TrajectoryEditionData trajectoryData, ObjectNode node) {
        PlayerControlledTrajectoryEditionData playerControlledTrajectoryData = (PlayerControlledTrajectoryEditionData) trajectoryData;

        List<Attribute> attributes = List.of(playerControlledTrajectoryData.getId(), playerControlledTrajectoryData.getPlayerMovementSpeed());
        attributes.forEach(attribute -> attribute.addToNode(node));

        return node;
    }
}
