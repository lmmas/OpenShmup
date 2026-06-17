package json.converters.spawn;

import json.SafeJsonNode;
import json.editionData.EditionData;

public interface SpawnConverter {

    EditionData fromJson(SafeJsonNode node);

}
