package json.converters.visual;

import engine.visual.SceneVisual;
import json.SafeJsonNode;

import java.nio.file.Path;

public interface VisualFactory {

    SceneVisual fromJson(SafeJsonNode node, Path textureFolderPath);
}
