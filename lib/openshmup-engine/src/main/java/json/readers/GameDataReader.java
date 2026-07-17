package json.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edition.EditionData;
import edition.GameEditionData;
import engine.types.IVec2D;
import engine.types.Vec2D;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edition.EditionData.*;

final public class GameDataReader {

    final private Map<String, VisualReader> visualReaders;

    final private Map<String, TrajectoryReader> trajectoryReaders;

    final private Map<String, HitboxReader> hitboxReaders;

    final private Map<String, SpawnReader> spawnReaders;

    final private Map<String, SpawnInfoReader> spawnInfoReaders;

    final private Map<String, EntityReader> entityReaders;

    private final ObjectMapper objectMapper;

    public GameDataReader() {
        this.objectMapper = new ObjectMapper();

        this.visualReaders = new HashMap<>(2);
        this.visualReaders.put(Types.Visual.animation.name(), VisualReader::Animation);
        this.visualReaders.put(Types.Visual.scrollingImage.name(), VisualReader::ScrollingImage);

        this.trajectoryReaders = new HashMap<>(2);
        this.trajectoryReaders.put(Types.Trajectory.fixed.name(), TrajectoryReader::FixedTrajectory);
        this.trajectoryReaders.put(Types.Trajectory.player.name(), TrajectoryReader::PlayerControlledTrajectory);

        this.hitboxReaders = new HashMap<>(2);
        this.hitboxReaders.put(Types.Hitbox.rectangle.name(), HitboxReader::RectangleHitbox);
        this.hitboxReaders.put(Types.Hitbox.custom.name(), HitboxReader::CompositeHitbox);

        this.spawnReaders = new HashMap<>(2);
        this.spawnReaders.put(Types.Spawn.display.name(), SpawnReader::DisplaySpawn);
        this.spawnReaders.put(Types.Spawn.entity.name(), SpawnReader::EntitySpawn);

        this.entityReaders = new HashMap<>(2);
        this.entityReaders.put(Types.Entity.ship.name(), EntityReader::Ship);
        this.entityReaders.put(Types.Entity.projectile.name(), EntityReader::Projectile);

        this.spawnInfoReaders = new HashMap<>(2);
        this.spawnInfoReaders.put(Types.SpawnInfo.single.name(), SpawnInfoReader::SingleSpawnInfo);
        this.spawnInfoReaders.put(Types.SpawnInfo.repeat.name(), SpawnInfoReader::RepeatSpawnInfo);
    }

    public GameEditionData readGameData(Path gameFolderPath) {
        GameEditionData gameEditionData = new GameEditionData(gameFolderPath);
        try {
            readConfig(gameEditionData);
            readVisuals(gameEditionData);
            readTrajectories(gameEditionData);
            readEntities(gameEditionData);
            readTimeline(gameEditionData);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return gameEditionData;
    }

    public void readConfig(GameEditionData gameEditionData) {
        JsonNodeReader rootNode = JsonNodeReader.getObjectRootNode(gameEditionData.paths.gameConfigFile, objectMapper);
        JsonNodeReader generalNode = rootNode.readObject(Types.Config.general.name());
        IVec2D resolution = generalNode.readIVec2D(Keys.Config.General.resolution);
        EditionData generalConfig = EditionData.Config.General(resolution);
        gameEditionData.configs.put(Types.Config.general, generalConfig);
        JsonNodeReader levelUINode = rootNode.readObject(Types.Config.levelUI.name());
        JsonNodeReader livesNode = levelUINode.readObject(Keys.Config.LevelUI.lives);
        String fileName = livesNode.readString(Keys.Config.LevelUI.Lives.fileName);
        Vec2D size = livesNode.readVec2D(Keys.Config.LevelUI.Lives.size);
        Vec2D startPosition = livesNode.readVec2D(Keys.Config.LevelUI.Lives.position);
        Vec2D stride = livesNode.readVec2D(Keys.Config.LevelUI.Lives.stride);
        EditionData livesConfig = EditionData.Config.LevelUI.Lives(fileName, size, startPosition, stride);
        EditionData levelUIConfig = EditionData.Config.LevelUI(livesConfig);
        gameEditionData.configs.put(Types.Config.levelUI, levelUIConfig);
    }

    public EditionData visualEditionDataFromJSON(JsonNodeReader node, Path textureFolderPath) {
        String type = node.readString(EditionData.Keys.type);
        VisualReader reader = visualReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return reader.fromJson(node);
    }

    public void readVisuals(GameEditionData editorGameData) {
        JsonNodeReader rootNode = JsonNodeReader.getArrayRootNode(editorGameData.paths.gameVisualsFile, objectMapper);
        List<JsonNodeReader> visualNodesList = rootNode.getObjectListFromArray();
        for (var visualNode : visualNodesList) {
            EditionData newVisualData = visualEditionDataFromJSON(visualNode, editorGameData.paths.gameTextureFolder);
            editorGameData.addVisual(newVisualData);
        }
    }

    public EditionData trajectoryEditionDataFromJSON(JsonNodeReader node) {
        String type = node.readString(EditionData.Keys.type);
        TrajectoryReader reader = trajectoryReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return reader.fromJson(node);
    }

    public void readTrajectories(GameEditionData editorGameData) {
        JsonNodeReader rootNode = JsonNodeReader.getArrayRootNode(editorGameData.paths.gameTrajectoriesFile, objectMapper);
        List<JsonNodeReader> trajectoryNodeList = rootNode.getObjectListFromArray();
        for (var trajectoryNode : trajectoryNodeList) {
            EditionData newTrajectoryData = trajectoryEditionDataFromJSON(trajectoryNode);
            editorGameData.addTrajectory(newTrajectoryData);
        }
    }

    public EditionData hitboxEditionDataFromJSON(JsonNodeReader node, Path textureFolderPath) {
        String type = node.readString(EditionData.Keys.type);
        HitboxReader reader = hitboxReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return reader.fromJson(node);
    }

    public EditionData spawnEditionDataFromJSON(JsonNodeReader node) {
        String type = node.readString(EditionData.Keys.type);
        SpawnReader reader = spawnReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return reader.fromJson(node);
    }

    public EditionData shotEditionDataFromJSON(JsonNodeReader node, GameDataReader gameDataReader) {
        double shotPeriod = node.readDouble(Keys.Shot.shotPeriod);
        double firstShotTime = node.readDouble(Keys.Shot.firstShotTime);
        JsonNodeReader spawnsNode = node.readArray(Keys.Shot.spawn);
        List<JsonNodeReader> spawnableNodes = spawnsNode.getObjectListFromArray();
        List<EditionData> spawnableList = spawnableNodes.stream().map(gameDataReader::spawnEditionDataFromJSON).toList();
        return Shot(shotPeriod, firstShotTime, spawnableList);
    }

    public EditionData entityEditionDataFromJSON(JsonNodeReader node, GameDataReader gameDataReader, Path textureFolderPath) {
        String type = node.readString(EditionData.Keys.type);
        EntityReader reader = entityReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": entity type is not supported");
        }
        return reader.fromJson(node, gameDataReader, textureFolderPath);
    }

    public void readEntities(GameEditionData editorGameData) {
        JsonNodeReader rootNode = JsonNodeReader.getArrayRootNode(editorGameData.paths.gameEntitiesFile, objectMapper);
        List<JsonNodeReader> entityNodeList = rootNode.getObjectListFromArray();
        for (var entityNode : entityNodeList) {
            EditionData newEntityData = entityEditionDataFromJSON(entityNode, this, editorGameData.paths.gameVisualsFile);
            editorGameData.addEntity(newEntityData);
        }
    }

    public EditionData spawnInfoEditionDataFromJSON(JsonNodeReader node, GameDataReader gameDataReader) {
        String type = node.readString(EditionData.Keys.type);
        SpawnInfoReader reader = spawnInfoReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawn info type is not supported");
        }
        return reader.fromJSON(node, gameDataReader);
    }

    public void readTimeline(GameEditionData editorgameData) {
        JsonNodeReader rootNode = JsonNodeReader.getObjectRootNode(editorgameData.paths.gameTimelineFile, objectMapper);
        JsonNodeReader arrayNode = rootNode.readArray("spawns");
        List<JsonNodeReader> spawnInfoNodeList = arrayNode.getObjectListFromArray();
        for (var spawnInfoNode : spawnInfoNodeList) {
            EditionData newSpawnInfoData = spawnInfoEditionDataFromJSON(spawnInfoNode, this);
            editorgameData.addTimelineSpawnInfo(newSpawnInfoData);
        }
    }
}
