package json;

import engine.gameData.GamePaths;
import json.editionData.EditionData;
import json.readers.JsonDataReader;
import lombok.Getter;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

final public class GameEditionData implements Serializable {

    @Getter
    private final String gameName;

    public transient GamePaths paths;

    final private String fileFolderPathString;
    @Getter
    final private ArrayList<EditionData> visualEditionDataList;
    @Getter
    final private ArrayList<EditionData> trajectoryEditionDataList;
    @Getter
    final private ArrayList<EditionData> entityEditionDataList;
    @Getter
    final private ArrayList<EditionData> timelineDataList;

    public GameEditionData(String gameName, Path gameFolderPath) {
        this.gameName = gameName;
        this.paths = new GamePaths(gameFolderPath);
        this.fileFolderPathString = paths.gameFolder.toString();
        this.visualEditionDataList = new ArrayList<>();
        this.trajectoryEditionDataList = new ArrayList<>();
        this.entityEditionDataList = new ArrayList<>();
        this.timelineDataList = new ArrayList<>();
    }

    public void reloadPaths() {
        this.paths = new GamePaths(Path.of(fileFolderPathString));
    }

    public void loadGameContents() {
        JsonDataReader jsonDataReader = new JsonDataReader();
        try {
            jsonDataReader.loadVisuals(this);
            jsonDataReader.loadTrajectories(this);
            jsonDataReader.loadEntities(this);
            jsonDataReader.loadTimeline(this);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addVisual(EditionData visualData) {
        visualData.checkForCategory(EditionData.Category.VISUAL);
        assert !visualEditionDataList.contains(visualData) : "visual already defined";
        visualEditionDataList.add(visualData);
        visualEditionDataList.sort(Comparator.comparingInt(EditionData::getVisualId));
    }

    public void addTrajectory(EditionData trajectoryData) {
        trajectoryData.checkForCategory(EditionData.Category.TRAJECTORY);
        assert !trajectoryEditionDataList.contains(trajectoryData) : "trajectory already defined";
        trajectoryEditionDataList.add(trajectoryData);
        trajectoryEditionDataList.sort(Comparator.comparingInt(EditionData::getTrajectoryId));
    }

    public void addEntity(EditionData entityData) {
        entityData.checkForCategory(EditionData.Category.ENTITY);
        assert !entityEditionDataList.contains(entityData) : "entity already defined";
        entityEditionDataList.add(entityData);
        entityEditionDataList.sort(Comparator.comparingInt(EditionData::getEntityId));
    }

    public void addTimelineSpawnInfo(EditionData spawnInfoData) {
        spawnInfoData.checkForCategory(EditionData.Category.SPAWN_INFO);
        assert !timelineDataList.contains(spawnInfoData) : "spawn info already defined";
        timelineDataList.add(spawnInfoData);
    }
}
