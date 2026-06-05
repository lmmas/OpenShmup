package json.converters.spawn;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.SafeJsonNode;
import json.editionData.SpawnEditionData;

public interface SpawnConverter {

    SpawnEditionData fromJson(SafeJsonNode node);

    ObjectNode toJson(SpawnEditionData spawnableData, ObjectNode node);
}
