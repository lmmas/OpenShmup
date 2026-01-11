package json.converters.visual;

import editor.objectAttributes.visual.VisualAttributes;
import json.SafeJsonNode;

import java.nio.file.Path;

public interface VisualConverter {

    VisualAttributes fromJson(SafeJsonNode node, Path textureFolderPath);
}
