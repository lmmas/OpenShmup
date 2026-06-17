package json;

import engine.gameData.GamePaths;
import json.converters.JsonDataConverter;
import json.editionData.EditionData;
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

    public void addVisual(EditionData visualData) {
        visualData.checkForCategory(EditionData.Category.VISUAL);
        assert !visualEditionDataList.contains(visualData) : "visual already defined";
        visualEditionDataList.add(visualData);
    }

    public String getGameName() {
        return gameFolderName;
    }

    public void addTrajectory(EditionData trajectoryData) {
        trajectoryData.checkForCategory(EditionData.Category.TRAJECTORY);
        assert !trajectoryEditionDataList.contains(trajectoryData) : "trajectory already defined";
        trajectoryEditionDataList.add(trajectoryData);
    }

    public void addEntity(EditionData entityData) {
        entityData.checkForCategory(EditionData.Category.ENTITY);
        assert !entityEditionDataList.contains(entityData) : "entity already defined";
        entityEditionDataList.add(entityData);
    }
}
