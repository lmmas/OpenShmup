package json.factories.hitbox;

import engine.entity.hitbox.Hitbox;
import json.SafeJsonNode;

public interface HitboxFactory {

    Hitbox fromJson(SafeJsonNode hitboxNode);
}
