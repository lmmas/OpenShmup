package json.converters.trajectory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.TrajectoryEditionData;
import json.SafeJsonNode;

public interface TrajectoryConverter {

    TrajectoryEditionData fromJson(SafeJsonNode node);

    ObjectNode toJson(TrajectoryEditionData trajectoryData, ObjectNode node);
}
