package json.converters.spawnable;

import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.Spawnable;
import engine.types.Vec2D;
import json.SafeJsonNode;

public class EntitySpawnInfoFactory implements SpawnableFactory {

    @Override
    public Spawnable fromJson(SafeJsonNode node) {
        int id = node.checkAndGetInt("id");
        Vec2D startingPositionVec = node.checkAndGetVec2D("startingPosition");
        int trajectoryId = node.checkAndGetInt("trajectory");
        return new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
    }
}
