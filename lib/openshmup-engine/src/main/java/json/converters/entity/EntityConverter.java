package json.converters.entity;

import json.SafeJsonNode;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;

import java.nio.file.Path;

public interface EntityConverter {

    EditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath);

}
