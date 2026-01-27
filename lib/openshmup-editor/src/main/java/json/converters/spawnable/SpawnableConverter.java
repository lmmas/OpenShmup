package json.converters.spawnable;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.SpawnableEditionData;
import json.SafeJsonNode;

public interface SpawnableConverter {

    SpawnableEditionData fromJson(SafeJsonNode node);

    ObjectNode toJson(SpawnableEditionData spawnableData, ObjectNode node);
}
