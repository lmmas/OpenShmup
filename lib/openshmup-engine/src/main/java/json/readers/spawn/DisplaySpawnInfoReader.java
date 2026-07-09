package json.readers.spawn;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import static json.editionData.EditionData.Spawn;

final public class DisplaySpawnInfoReader implements SpawnReader {

    @Override
    public EditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.DisplaySpawnInfo.id);
        Vec2D position = node.safeGetVec2D(JsonFieldNames.DisplaySpawnInfo.position);
        return Spawn.DisplaySpawn(id, position);
    }

}
