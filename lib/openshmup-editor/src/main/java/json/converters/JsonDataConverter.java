package json.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.EditorGameDataManager;
import editor.editionData.*;
import json.SafeJsonNode;
import json.converters.entity.EntityConverter;
import json.converters.entity.ProjectileConverter;
import json.converters.entity.ShipConverter;
import json.converters.extraComponent.ShotConverter;
import json.converters.hitbox.CompositeHitboxConverter;
import json.converters.hitbox.HitboxConverter;
import json.converters.hitbox.SimpleRectangleHitboxConverter;
import json.converters.spawn.DisplaySpawnInfoConverter;
import json.converters.spawn.EntitySpawnInfoConverter;
import json.converters.spawn.SpawnConverter;
import json.converters.trajectory.FixedTrajectoryConverter;
import json.converters.trajectory.PlayerTrajectoryConverter;
import json.converters.trajectory.TrajectoryConverter;
import json.converters.visual.AnimationConverter;
import json.converters.visual.ScrollingImageConverter;
import json.converters.visual.VisualConverter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static editor.editionData.EditionData.Types;

public class JsonDataConverter {

    final private Map<String, VisualConverter> visualConverters;

    final private Map<String, TrajectoryConverter> trajectoryConverters;

    final private Map<String, HitboxConverter> hitboxConverters;

    final private Map<String, SpawnConverter> spawnConverters;

    final private ShotConverter shotConverter;

    final private Map<String, EntityConverter> entityConverters;

    private final ObjectMapper objectMapper;

    public JsonDataConverter() {
        this.objectMapper = new ObjectMapper();

        this.visualConverters = new HashMap<>(2);
        this.visualConverters.put(Types.Visual.animation.name(), new AnimationConverter());
        this.visualConverters.put(Types.Visual.scrollingImage.name(), new ScrollingImageConverter());

        this.trajectoryConverters = new HashMap<>(2);
        this.trajectoryConverters.put(Types.Trajectory.fixed.name(), new FixedTrajectoryConverter());
        this.trajectoryConverters.put(Types.Trajectory.player.name(), new PlayerTrajectoryConverter());

        this.hitboxConverters = new HashMap<>(2);
        this.hitboxConverters.put(Types.Hitbox.rectangle.name(), new SimpleRectangleHitboxConverter());
        this.hitboxConverters.put(Types.Hitbox.custom.name(), new CompositeHitboxConverter());

        this.spawnConverters = new HashMap<>(2);
        this.spawnConverters.put(Types.Spawn.display.name(), new DisplaySpawnInfoConverter());
        this.spawnConverters.put(Types.Spawn.entity.name(), new EntitySpawnInfoConverter());

        this.shotConverter = new ShotConverter();

        this.entityConverters = new HashMap<>(2);
        this.entityConverters.put(Types.Entity.ship.name(), new ShipConverter());
        this.entityConverters.put(Types.Entity.projectile.name(), new ProjectileConverter());
    }

    public VisualEditionData visualEditionDataFromJSON(SafeJsonNode node, Path textureFolderPath) {
        String type = node.safeGetString("type");
        VisualConverter converter = visualConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return converter.fromJson(node, textureFolderPath);
    }

    public void loadVisuals(EditorGameDataManager editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameVisualsFile, objectMapper);
        List<SafeJsonNode> visualNodesList = rootNode.safeGetObjectListFromArray();
        for (var visualNode : visualNodesList) {
            VisualEditionData newVisualData = visualEditionDataFromJSON(visualNode, editorGameData.paths.gameTextureFolder);
            editorGameData.addVisual(newVisualData);
        }
    }

    public TrajectoryEditionData trajectoryEditionDataFromJSON(SafeJsonNode node) {
        String type = node.safeGetString("type");
        TrajectoryConverter converter = trajectoryConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return converter.fromJson(node);
    }

    public void loadTrajectories(EditorGameDataManager editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameTrajectoriesFile, objectMapper);
        List<SafeJsonNode> trajectoryNodeList = rootNode.safeGetObjectListFromArray();
        for (var trajectoryNode : trajectoryNodeList) {
            TrajectoryEditionData newTrajectoryData = trajectoryEditionDataFromJSON(trajectoryNode);
            editorGameData.addTrajectory(newTrajectoryData);
        }
    }

    public HitboxEditionData hitboxEditionDataFromJSON(SafeJsonNode node, Path textureFolderPath) {
        String type = node.safeGetString("type");
        HitboxConverter converter = hitboxConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return converter.fromJson(node, textureFolderPath);
    }

    public SpawnEditionData spawnEditionDataFromJSON(SafeJsonNode node) {
        String type = node.safeGetString("type");
        SpawnConverter converter = spawnConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return converter.fromJson(node);
    }

    public ObjectNode spawnEditionDataToJSON(SpawnEditionData spawnData, ObjectNode node) {
        String typeStr = spawnData.getType().name();
        node.put("type", typeStr);
        return spawnConverters.get(typeStr).toJson(spawnData, node);
    }

    public ObjectNode visualToJSON(VisualEditionData visualData, ObjectNode node) {
        String typeStr = visualData.getType().name();
        node.put("type", typeStr);
        return visualConverters.get(typeStr).toJson(visualData, node);
    }

    public ObjectNode hitboxToJSON(HitboxEditionData hitboxData, ObjectNode node) {
        String typeStr = hitboxData.getType().name();
        node.put("type", typeStr);
        return hitboxConverters.get(typeStr).toJson(hitboxData, node);
    }

    public ObjectNode trajectoryToJSON(TrajectoryEditionData trajectoryData, ObjectNode node) {
        String typeStr = trajectoryData.getType().name();
        node.put("type", typeStr);
        return trajectoryConverters.get(typeStr).toJson(trajectoryData, node);
    }

    public ObjectNode entityToJSON(EntityEditionData entityData, ObjectNode node) {
        String typeStr = entityData.getType().name();
        node.put("type", typeStr);
        return entityConverters.get(typeStr).toJson(entityData, node);
    }

    public ShotEditionData shotEditionDataFromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        return (ShotEditionData) shotConverter.fromJson(node, jsonDataConverter, textureFolderPath);
    }

    public EntityEditionData entityEditionDataFromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        String type = node.safeGetString("type");
        EntityConverter converter = entityConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": entity type is not supported");
        }
        return converter.fromJson(node, jsonDataConverter, textureFolderPath);
    }

    public void loadEntities(EditorGameDataManager editorGameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(editorGameData.paths.gameEntitiesFile, objectMapper);
        List<SafeJsonNode> entityNodeList = rootNode.safeGetObjectListFromArray();
        for (var entityNode : entityNodeList) {
            EntityEditionData newEntityData = entityEditionDataFromJSON(entityNode, this, editorGameData.paths.gameVisualsFile);
            editorGameData.addEntity(newEntityData);
        }
    }
}
