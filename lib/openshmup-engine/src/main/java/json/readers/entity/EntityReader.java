package json.readers.entity;

import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.JsonDataReader;

import java.nio.file.Path;

public interface EntityReader {

    EditionData fromJson(SafeJsonNode node, JsonDataReader jsonDataReader, Path textureFolderPath);

}
