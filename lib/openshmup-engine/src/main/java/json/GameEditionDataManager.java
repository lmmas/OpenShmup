package json;

import engine.gameData.GamePaths;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;
import json.editionData.EntityEditionData;
import json.editionData.TrajectoryEditionData;
import json.editionData.VisualEditionData;
import lombok.Getter;

import java.util.ArrayList;

final public class GameEditionDataManager {

    private String gameFolderName;

    final public GamePaths paths;
    @Getter final private ArrayList<EditionData> visualEditionDataList;
    @Getter final private ArrayList<EditionData> trajectoryEditionDataList;
    @Getter final private ArrayList<EditionData> entityEditionDataList;


    public GameEditionDataManager(String gameFolderName) {
        this.gameFolderName = gameFolderName;
        this.paths = new GamePaths(gameFolderName);
        this.visualEditionDataList = new ArrayList<>();
        this.trajectoryEditionDataList = new ArrayList<>();
        this.entityEditionDataList = new ArrayList<>();
    }

    public void loadGameContents() {
        JsonDataConverter jsonDataConverter = new JsonDataConverter();
        try {
            jsonDataConverter.loadVisuals(this);
            jsonDataConverter.loadTrajectories(this);
            jsonDataConverter.loadEntities(this);
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
        assert !entityEditionDataList.contains(newEntityData) : "entity already defined";
        entityEditionDataList.add(newEntityData);
    }
}
