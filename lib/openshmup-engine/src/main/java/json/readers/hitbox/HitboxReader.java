package json.readers.hitbox;

import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

public interface HitboxReader {

    EditionData fromJson(SafeJsonNode node, Path textureFolderPath);

}
