package json.factories;

import engine.level.spawnable.EntitySpawnInfo;
import engine.level.spawnable.SceneDisplaySpawnInfo;
import engine.level.spawnable.Spawnable;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.util.function.Function;

public class SpawnableFactories {

    final public static Function<SafeJsonNode, Spawnable> visualSpawnInfoFactory = node -> {
        int id = node.checkAndGetInt("id");
        Vec2D positionVec = node.checkAndGetVec2D("position");
        return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
    };

    final public static Function<SafeJsonNode, Spawnable> entitySpawnInfoFactory = node -> {
        int id = node.checkAndGetInt("id");
        Vec2D startingPositionVec = node.checkAndGetVec2D("startingPosition");
        int trajectoryId = node.checkAndGetInt("trajectory");
        return new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
    };
}
