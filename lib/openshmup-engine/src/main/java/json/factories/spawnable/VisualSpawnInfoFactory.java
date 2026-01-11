package json.factories.spawnable;

import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.spawnable.Spawnable;
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
