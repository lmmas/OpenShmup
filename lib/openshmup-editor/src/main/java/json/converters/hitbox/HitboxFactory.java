package json.converters.hitbox;

import engine.gameData.GamePaths;
import engine.level.entity.hitbox.Hitbox;
import json.SafeJsonNode;

public interface HitboxFactory {

    Hitbox fromJson(SafeJsonNode hitboxNode, GamePaths paths);
}
