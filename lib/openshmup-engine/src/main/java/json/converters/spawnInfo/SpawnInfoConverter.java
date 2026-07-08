package json.converters.spawnInfo;

import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

public interface SpawnInfoConverter {

    EditionData fromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter);
}
