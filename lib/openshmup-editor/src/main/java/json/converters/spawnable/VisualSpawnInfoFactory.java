package json.converters.spawnable;

import engine.level.spawnable.SceneDisplaySpawnInfo;
import engine.level.spawnable.Spawnable;
import engine.types.Vec2D;
import json.SafeJsonNode;

public class VisualSpawnInfoFactory implements SpawnableFactory {

    @Override
    public Spawnable fromJson(SafeJsonNode node) {
        int id = node.checkAndGetInt("id");
        Vec2D positionVec = node.checkAndGetVec2D("position");
        return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
    }
}
