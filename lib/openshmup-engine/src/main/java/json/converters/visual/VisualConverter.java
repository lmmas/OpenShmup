package json.converters.visual;

import com.fasterxml.jackson.databind.node.ObjectNode;
import json.SafeJsonNode;
import json.editionData.VisualEditionData;

import java.nio.file.Path;

public interface VisualConverter {
    VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath);

    ObjectNode toJson(VisualEditionData visualData, ObjectNode node);
}
