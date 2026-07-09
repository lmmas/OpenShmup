package json.readers.visual;

import json.SafeJsonNode;
import json.editionData.EditionData;

import java.nio.file.Path;

public interface VisualReader {

    EditionData fromJson(SafeJsonNode node, Path textureFolderPath);
}
