package json.readers.trajectory;

import json.SafeJsonNode;
import json.editionData.EditionData;

public interface TrajectoryReader {

    EditionData fromJson(SafeJsonNode node);

}
