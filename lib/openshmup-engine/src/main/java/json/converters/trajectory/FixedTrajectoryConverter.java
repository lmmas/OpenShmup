package json.converters.trajectory;

import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import static json.editionData.EditionData.Trajectory;

final public class FixedTrajectoryConverter implements TrajectoryConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.FixedTrajectory.id);
        String functionXString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionX);
        String functionYString = node.safeGetString(JsonFieldNames.FixedTrajectory.functionY);
        return Trajectory.FixedTrajectory(id, functionXString, functionYString);
    }

}
