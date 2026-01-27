package json.converters.hitbox;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.HitboxEditionData;
import json.SafeJsonNode;

import java.nio.file.Path;

public interface HitboxConverter {

    HitboxEditionData fromJson(SafeJsonNode node, Path textureFolderPath);

    ObjectNode toJson(HitboxEditionData hitboxData, ObjectNode node);
}
