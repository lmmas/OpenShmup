package json.readers.spawn;

import json.SafeJsonNode;
import json.editionData.EditionData;

public interface SpawnReader {

    EditionData fromJson(SafeJsonNode node);

}
