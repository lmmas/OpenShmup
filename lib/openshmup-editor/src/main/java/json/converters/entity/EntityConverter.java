package json.converters.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.EntityEditionData;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;

import java.nio.file.Path;

public interface EntityConverter {

    EntityEditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath);

    ObjectNode toJson(EntityEditionData entityData, ObjectNode node);
}
