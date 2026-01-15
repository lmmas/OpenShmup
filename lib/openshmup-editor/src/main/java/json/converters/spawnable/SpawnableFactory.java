package json.converters.spawnable;

import engine.level.spawnable.Spawnable;
import json.SafeJsonNode;

public interface SpawnableFactory {

    Spawnable fromJson(SafeJsonNode node);
}
