package json.converters.spawn;

import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;
import json.editionData.EditionData;

import static json.editionData.EditionData.Spawn;

final public class EntitySpawnInfoConverter implements SpawnConverter {

    @Override
    public EditionData fromJson(SafeJsonNode node) {
        int id = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.id);
        Vec2D startingPositionVec = node.safeGetVec2D(JsonFieldNames.EntitySpawnInfo.startingPosition);
        int trajectoryId = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.trajectory);
        return Spawn.EntitySpawn(id, trajectoryId, startingPositionVec);
    }

}
