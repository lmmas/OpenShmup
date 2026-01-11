package json.converters.spawnable;

import engine.scene.spawnable.Spawnable;
import json.SafeJsonNode;

public interface SpawnableFactory {

    Spawnable fromJson(SafeJsonNode node);
}
