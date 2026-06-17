package json.converters.visual;

import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

public interface VisualConverter {

    EditionData fromJson(SafeJsonNode node, Path textureFolderPath);
}
