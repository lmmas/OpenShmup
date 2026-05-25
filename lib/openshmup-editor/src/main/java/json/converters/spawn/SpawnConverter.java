package json.converters.spawn;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.SpawnEditionData;
import json.SafeJsonNode;

public interface SpawnConverter {

    SpawnEditionData fromJson(SafeJsonNode node);

    ObjectNode toJson(SpawnEditionData spawnableData, ObjectNode node);
}
