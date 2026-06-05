package json.converters.trajectory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.SafeJsonNode;
import json.editionData.TrajectoryEditionData;

public interface TrajectoryConverter {

    TrajectoryEditionData fromJson(SafeJsonNode node);

    ObjectNode toJson(TrajectoryEditionData trajectoryData, ObjectNode node);
}
