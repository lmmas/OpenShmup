package editor.attribute;

import editor.objectAttributes.visual.VisualAttributes;
import engine.gameData.GamePaths;
import json.converters.JsonDataConverter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EditorGameDataManager {

    private String gameFolderName;

    final public GamePaths paths;
    @Getter
    final private List<VisualAttributes> visualAttributesList;

    public EditorGameDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.visualAttributesList = new ArrayList<>();
    }

    public void loadGameContents() {
        JsonDataConverter jsonDataConverter = new JsonDataConverter();
        try {
            jsonDataConverter.loadVisualAttributes(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addVisual(VisualAttributes visualAttributes) {
        assert !visualAttributesList.contains(visualAttributes) : "visual already defined";
        visualAttributesList.add(visualAttributes);
    }

    public String getGameName() {
        return gameFolderName;
    }
}
