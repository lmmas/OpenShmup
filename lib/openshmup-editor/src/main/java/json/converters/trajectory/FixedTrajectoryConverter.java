package json.converters.trajectory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.attribute.Attribute;
import editor.editionData.FixedTrajectoryEditionData;
import editor.editionData.TrajectoryEditionData;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.List;

public class FixedTrajectoryConverter implements TrajectoryConverter {

    @Override
    public TrajectoryEditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.FixedTrajectory.id);
        String functionXString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionX);
        String functionYString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionY);
        return new FixedTrajectoryEditionData(id, functionXString, functionYString);
    }

    @Override
    public ObjectNode toJson(TrajectoryEditionData trajectoryData, ObjectNode node) {
        FixedTrajectoryEditionData fixedTrajectoryData = (FixedTrajectoryEditionData) trajectoryData;

        List<Attribute> attributes = List.of(fixedTrajectoryData.getId(), fixedTrajectoryData.getTrajectoryFunctionX(), fixedTrajectoryData.getTrajectoryFunctionY());
        attributes.forEach(attribute -> attribute.addToNode(node));
        return node;
    }
}
