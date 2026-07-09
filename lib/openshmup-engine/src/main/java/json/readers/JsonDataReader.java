package json.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import json.GameEditionData;
import json.SafeJsonNode;
import json.editionData.EditionData;
import json.readers.entity.EntityReader;
import json.readers.entity.ProjectileReader;
import json.readers.entity.ShipReader;
import json.readers.extraComponent.ShotReader;
import json.readers.hitbox.CompositeHitboxReader;
import json.readers.hitbox.HitboxReader;
import json.readers.hitbox.SimpleRectangleHitboxReader;
import json.readers.spawn.DisplaySpawnInfoReader;
import json.readers.spawn.EntitySpawnInfoReader;
import json.readers.spawn.SpawnReader;
import json.readers.spawnInfo.RepeatSpawnInfoReader;
import json.readers.spawnInfo.SingleSpawnInfoReader;
import json.readers.spawnInfo.SpawnInfoReader;
import json.readers.trajectory.FixedTrajectoryReader;
import json.readers.trajectory.PlayerTrajectoryReader;
import json.readers.trajectory.TrajectoryReader;
import json.readers.visual.AnimationReader;
import json.readers.visual.ScrollingImageReader;
import json.readers.visual.VisualReader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static json.editionData.EditionData.Types;

final public class JsonDataReader {

    final private Map<String, VisualReader> visualReaders;

    final private Map<String, TrajectoryReader> trajectoryReaders;

    final private Map<String, HitboxReader> hitboxReaders;

    final private Map<String, SpawnReader> spawnReaders;

    final private Map<String, SpawnInfoReader> spawnInfoReaders;

    final private ShotReader shotReader;

    final private Map<String, EntityReader> entityReaders;

    private final ObjectMapper objectMapper;

    public JsonDataReader() {
        this.objectMapper = new ObjectMapper();

        this.visualReaders = new HashMap<>(2);
        this.visualReaders.put(Types.Visual.animation.name(), new AnimationReader());
        this.visualReaders.put(Types.Visual.scrollingImage.name(), new ScrollingImageReader());

        this.trajectoryReaders = new HashMap<>(2);
        this.trajectoryReaders.put(Types.Trajectory.fixed.name(), new FixedTrajectoryReader());
        this.trajectoryReaders.put(Types.Trajectory.player.name(), new PlayerTrajectoryReader());

        this.hitboxReaders = new HashMap<>(2);
        this.hitboxReaders.put(Types.Hitbox.rectangle.name(), new SimpleRectangleHitboxReader());
        this.hitboxReaders.put(Types.Hitbox.custom.name(), new CompositeHitboxReader());

        this.spawnReaders = new HashMap<>(2);
        this.spawnReaders.put(Types.Spawn.display.name(), new DisplaySpawnInfoReader());
        this.spawnReaders.put(Types.Spawn.entity.name(), new EntitySpawnInfoReader());

        this.shotReader = new ShotReader();

        this.entityReaders = new HashMap<>(2);
        this.entityReaders.put(Types.Entity.ship.name(), new ShipReader());
        this.entityReaders.put(Types.Entity.projectile.name(), new ProjectileReader());

        this.spawnInfoReaders = new HashMap<>(2);
        this.spawnInfoReaders.put(Types.SpawnInfo.single.name(), new SingleSpawnInfoReader());
        this.spawnInfoReaders.put(Types.SpawnInfo.repeat.name(), new RepeatSpawnInfoReader());
    }

    public EditionData visualEditionDataFromJSON(SafeJsonNode node, Path textureFolderPath) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        VisualReader reader = visualReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return reader.fromJson(node, textureFolderPath);
    }

    public void loadVisuals(GameEditionData editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameVisualsFile, objectMapper);
        List<SafeJsonNode> visualNodesList = rootNode.safeGetObjectListFromArray();
        for (var visualNode : visualNodesList) {
            EditionData newVisualData = visualEditionDataFromJSON(visualNode, editorGameData.paths.gameTextureFolder);
            editorGameData.addVisual(newVisualData);
        }
    }

    public EditionData trajectoryEditionDataFromJSON(SafeJsonNode node) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        TrajectoryReader reader = trajectoryReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return reader.fromJson(node);
    }

    public void loadTrajectories(GameEditionData editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameTrajectoriesFile, objectMapper);
        List<SafeJsonNode> trajectoryNodeList = rootNode.safeGetObjectListFromArray();
        for (var trajectoryNode : trajectoryNodeList) {
            EditionData newTrajectoryData = trajectoryEditionDataFromJSON(trajectoryNode);
            editorGameData.addTrajectory(newTrajectoryData);
        }
    }

    public EditionData hitboxEditionDataFromJSON(SafeJsonNode node, Path textureFolderPath) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        HitboxReader reader = hitboxReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return reader.fromJson(node, textureFolderPath);
    }

    public EditionData spawnEditionDataFromJSON(SafeJsonNode node) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        SpawnReader reader = spawnReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return reader.fromJson(node);
    }

    public EditionData shotEditionDataFromJSON(SafeJsonNode node, JsonDataReader jsonDataReader, Path textureFolderPath) {
        return shotReader.fromJson(node, jsonDataReader, textureFolderPath);
    }

    public EditionData entityEditionDataFromJSON(SafeJsonNode node, JsonDataReader jsonDataReader, Path textureFolderPath) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        EntityReader reader = entityReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": entity type is not supported");
        }
        return reader.fromJson(node, jsonDataReader, textureFolderPath);
    }

    public void loadEntities(GameEditionData editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameEntitiesFile, objectMapper);
        List<SafeJsonNode> entityNodeList = rootNode.safeGetObjectListFromArray();
        for (var entityNode : entityNodeList) {
            EditionData newEntityData = entityEditionDataFromJSON(entityNode, this, editorGameData.paths.gameVisualsFile);
            editorGameData.addEntity(newEntityData);
        }
    }

    public EditionData spawnInfoEditionDataFromJSON(SafeJsonNode node, JsonDataReader jsonDataReader) {
        String type = node.safeGetString(EditionData.Keys.type.name());
        SpawnInfoReader reader = spawnInfoReaders.get(type);
        if (reader == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawn info type is not supported");
        }
        return reader.fromJSON(node, jsonDataReader);
    }

    public void loadTimeline(GameEditionData editorgameData) {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(editorgameData.paths.gameTimelineFile, objectMapper);
        SafeJsonNode arrayNode = rootNode.safeGetArray("spawns");
        List<SafeJsonNode> spawnInfoNodeList = arrayNode.safeGetObjectListFromArray();
        for (var spawnInfoNode : spawnInfoNodeList) {
            EditionData newSpawnInfoData = spawnInfoEditionDataFromJSON(spawnInfoNode, this);
            editorgameData.addTimelineSpawnInfo(newSpawnInfoData);
        }
    }
}
