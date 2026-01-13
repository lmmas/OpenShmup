package json.converters.visual;

import editor.editionData.VisualEditionData;
import json.SafeJsonNode;

import java.nio.file.Path;

public interface VisualConverter {

    VisualEditionData fromJson(SafeJsonNode node, Path textureFolderPath);
}
