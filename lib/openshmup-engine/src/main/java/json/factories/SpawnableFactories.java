package json.factories;

import engine.level.spawnable.EntitySpawnInfo;
import engine.level.spawnable.SceneDisplaySpawnInfo;
import engine.level.spawnable.Spawnable;
import engine.types.Vec2D;
import json.JsonFieldNames;
import json.SafeJsonNode;

import java.util.function.Function;

public class SpawnableFactories {

    final public static Function<SafeJsonNode, Spawnable> displaySpawnInfoFactory = node -> {
        int id = node.safeGetInt(JsonFieldNames.DisplaySpawnInfo.id);
        Vec2D positionVec = node.safeGetVec2D(JsonFieldNames.DisplaySpawnInfo.position);
        return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
    };

    final public static Function<SafeJsonNode, Spawnable> entitySpawnInfoFactory = node -> {
        int id = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.id);
        Vec2D startingPositionVec = node.safeGetVec2D(JsonFieldNames.EntitySpawnInfo.startingPosition);
        int trajectoryId = node.safeGetInt(JsonFieldNames.EntitySpawnInfo.trajectory);
        return new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
    };
}
