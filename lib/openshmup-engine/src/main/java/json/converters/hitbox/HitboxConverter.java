package json.converters.hitbox;

import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

public interface HitboxConverter {

    EditionData fromJson(SafeJsonNode node, Path textureFolderPath);

}
