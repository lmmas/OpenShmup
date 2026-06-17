package json.converters.trajectory;

import json.SafeJsonNode;
import json.editionData.EditionData;

public interface TrajectoryConverter {

    EditionData fromJson(SafeJsonNode node);

}
