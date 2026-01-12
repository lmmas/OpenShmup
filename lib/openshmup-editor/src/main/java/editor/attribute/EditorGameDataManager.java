package editor.attribute;

import editor.editionData.visual.VisualEditionData;
import engine.gameData.GamePaths;
import json.converters.JsonDataConverter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EditorGameDataManager {

    private String gameFolderName;

    final public GamePaths paths;
    @Getter
    final private List<VisualEditionData> visualEditionDataList;

    public EditorGameDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.visualEditionDataList = new ArrayList<>();
    }

    public void loadGameContents() {
        JsonDataConverter jsonDataConverter = new JsonDataConverter();
        try {
            jsonDataConverter.loadVisualAttributes(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addVisual(VisualEditionData visualEditionData) {
        assert !visualEditionDataList.contains(visualEditionData) : "visual already defined";
        visualEditionDataList.add(visualEditionData);
    }

    public String getGameName() {
        return gameFolderName;
    }
}
