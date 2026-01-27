package editor;

import editor.editionData.EntityEditionData;
import editor.editionData.TrajectoryEditionData;
import editor.editionData.VisualEditionData;
import engine.gameData.GamePaths;
import json.converters.JsonDataConverter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class EditorGameDataManager {

    private String gameFolderName;

    final public GamePaths paths;
    @Getter final private ArrayList<VisualEditionData> visualEditionDataList;
    @Getter final private ArrayList<TrajectoryEditionData> trajectoryEditionDataList;
    @Getter final private HashMap<Integer, EntityEditionData> entityEditionDataMap;


    public EditorGameDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.visualEditionDataList = new ArrayList<>();
        this.trajectoryEditionDataList = new ArrayList<>();
        this.entityEditionDataMap = new HashMap<>();
    }

    public void loadGameContents() {
        JsonDataConverter jsonDataConverter = new JsonDataConverter();
        try {
            jsonDataConverter.loadVisuals(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addVisual(VisualEditionData visualData) {
        assert !visualEditionDataList.contains(visualData) : "visual already defined";
        visualEditionDataList.add(visualData);
    }

    public String getGameName() {
        return gameFolderName;
    }

    public void addTrajectory(TrajectoryEditionData newTrajectoryData) {
        assert !trajectoryEditionDataList.contains(newTrajectoryData) : "trajectory already defined";
        trajectoryEditionDataList.add(newTrajectoryData);
    }

    public void addEntity(EntityEditionData newEntityData) {
        assert !entityEditionDataMap.containsKey(newEntityData.getId()) : "entity ID already taken";
        entityEditionDataMap.put(newEntityData.getId(), newEntityData);
    }
}
