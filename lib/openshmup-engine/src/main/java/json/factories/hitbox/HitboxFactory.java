package json.factories.hitbox;

import engine.entity.hitbox.Hitbox;
import engine.gameData.GamePaths;
import json.SafeJsonNode;

public interface HitboxFactory {

    Hitbox fromJson(SafeJsonNode hitboxNode, GamePaths paths);
}
