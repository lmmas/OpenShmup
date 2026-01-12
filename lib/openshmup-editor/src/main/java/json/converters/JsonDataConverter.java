package json.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import editor.EditorGameDataManager;
import editor.editionData.visual.VisualEditionData;
import json.SafeJsonNode;
import json.converters.visual.AnimationConverter;
import json.converters.visual.ScrollingImageConverter;
import json.converters.visual.VisualConverter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDataConverter {

    final private Map<String, VisualConverter> visualConverters;

    private final ObjectMapper objectMapper;

    public JsonDataConverter() {
        this.objectMapper = new ObjectMapper();

        this.visualConverters = new HashMap<>(2);
        this.visualConverters.put("animation", new AnimationConverter());
        this.visualConverters.put("scrollingImage", new ScrollingImageConverter());
    }

    public VisualEditionData visualAttributesFromJSON(SafeJsonNode node, Path textureFolderPath) {
        String type = node.checkAndGetString("type");
        VisualConverter converter = visualConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return converter.fromJson(node, textureFolderPath);
    }

    public void loadVisualAttributes(EditorGameDataManager editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameVisualsFile, objectMapper);
        List<SafeJsonNode> visualNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var visualNode : visualNodesList) {
            VisualEditionData newVisual = visualAttributesFromJSON(visualNode, editorGameData.paths.gameTextureFolder);
            editorGameData.addVisual(newVisual);
        }
    }
}
