package json.readers;

import edition.EditionData;
import json.JsonNodeReader;
import types.Vec2D;

@FunctionalInterface
public interface SpawnReader {

    EditionData fromJson(JsonNodeReader node);

    static EditionData DisplaySpawn(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Spawn.DisplaySpawn.id);
        Vec2D position = node.readVec2D(EditionData.Keys.Spawn.DisplaySpawn.position);
        return EditionData.Spawn.DisplaySpawn(id, position);
    }

    static EditionData EntitySpawn(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Spawn.EntitySpawn.id);
        Vec2D startingPositionVec = node.readVec2D(EditionData.Keys.Spawn.EntitySpawn.startingPosition);
        int trajectoryId = node.readInt(EditionData.Keys.Spawn.EntitySpawn.trajectory);
        return EditionData.Spawn.EntitySpawn(id, trajectoryId, startingPositionVec);
    }
}
