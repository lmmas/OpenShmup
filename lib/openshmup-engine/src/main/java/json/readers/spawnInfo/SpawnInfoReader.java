package json.readers.spawnInfo;

import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

public interface SpawnInfoReader {

    EditionData fromJSON(SafeJsonNode node, JsonDataReader jsonDataReader);
}
