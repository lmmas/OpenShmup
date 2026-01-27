package json.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.EditorGameDataManager;
import editor.editionData.*;
import json.SafeJsonNode;
import json.converters.entity.EntityConverter;
import json.converters.entity.ProjectileConverter;
import json.converters.entity.ShipConverter;
import json.converters.extraComponent.ExtraComponentConverter;
import json.converters.extraComponent.ShotConverter;
import json.converters.hitbox.CompositeHitboxConverter;
import json.converters.hitbox.HitboxConverter;
import json.converters.hitbox.SimpleRectangleHitboxConverter;
import json.converters.spawnable.DisplaySpawnInfoConverter;
import json.converters.spawnable.EntitySpawnInfoConverter;
import json.converters.spawnable.SpawnableConverter;
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

public class JsonDataConverter {

    final private Map<String, VisualConverter> visualConverters;

    final private Map<String, TrajectoryConverter> trajectoryConverters;

    final private Map<String, HitboxConverter> hitboxConverters;

    final private Map<String, SpawnableConverter> spawnableConverters;

    final private Map<String, ExtraComponentConverter> extraComponentConverters;

    final private Map<String, EntityConverter> entityConverters;

    private final ObjectMapper objectMapper;

    public JsonDataConverter() {
        this.objectMapper = new ObjectMapper();

        this.visualConverters = new HashMap<>(2);
        this.visualConverters.put("animation", new AnimationConverter());
        this.visualConverters.put("scrollingImage", new ScrollingImageConverter());

        this.trajectoryConverters = new HashMap<>(2);
        this.trajectoryConverters.put("fixed", new FixedTrajectoryConverter());
        this.trajectoryConverters.put("player", new PlayerTrajectoryConverter());

        this.hitboxConverters = new HashMap<>(2);
        this.hitboxConverters.put("simpleRectangle", new SimpleRectangleHitboxConverter());
        this.hitboxConverters.put("composite", new CompositeHitboxConverter());

        this.spawnableConverters = new HashMap<>(2);
        this.spawnableConverters.put("display", new DisplaySpawnInfoConverter());
        this.spawnableConverters.put("entity", new EntitySpawnInfoConverter());

        this.extraComponentConverters = new HashMap<>(2);
        this.extraComponentConverters.put("shot", new ShotConverter());

        this.entityConverters = new HashMap<>(2);
        this.entityConverters.put("ship", new ShipConverter());
        this.entityConverters.put("projectile", new ProjectileConverter());
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

    public SpawnableEditionData spawnableEditionDataFromJSON(SafeJsonNode node) {
        String type = node.safeGetString("type");
        SpawnableConverter converter = spawnableConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return converter.fromJson(node);
    }

    public ObjectNode spawnableEditionDataToJSON(SpawnableEditionData spawnableData, ObjectNode node) {
        String typeStr = switch (spawnableData) {
            case DisplaySpawnInfoEditionData ignored -> "display";
            case EntitySpawnInfoEditionData ignored -> "entity";
        };
        node.put("type", typeStr);
        return spawnableConverters.get(typeStr).toJson(spawnableData, node);
    }

    public ObjectNode visualToJSON(VisualEditionData visualData, ObjectNode node) {
        String typeStr = switch (visualData) {
            case ScrollingImageEditionData ignored -> "scollingImage";
            case AnimationEditionData ignored -> "animation";
        };
        node.put("type", typeStr);
        return visualConverters.get(typeStr).toJson(visualData, node);
    }

    public ObjectNode hitboxToJSON(HitboxEditionData hitboxData, ObjectNode node) {
        String typeStr = switch (hitboxData) {
            case SimpleRectangleHitboxEditionData ignored -> "simpleRectangle";
            case CompositeHitboxEditionData ignored -> "composite";
        };
        node.put("type", typeStr);
        return hitboxConverters.get(typeStr).toJson(hitboxData, node);
    }

    public ObjectNode trajectoryToJSON(TrajectoryEditionData trajectoryData, ObjectNode node) {
        String typeStr = switch (trajectoryData) {
            case FixedTrajectoryEditionData ignored -> "fixed";
            case PlayerControlledTrajectoryEditionData ignored -> "player";
        };
        node.put("type", typeStr);
        return trajectoryConverters.get(typeStr).toJson(trajectoryData, node);
    }

    public ObjectNode entityToJSON(EntityEditionData entityData, ObjectNode node) {
        String typeStr = switch (entityData) {
            case ShipEditionData ignored -> "ship";
            case ProjectileEditionData ignored -> "projectile";
        };
        node.put("type", typeStr);
        return entityConverters.get(typeStr).toJson(entityData, node);
    }

    public ExtraComponentEditionData extraComponentEditionDataFromJSON(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath) {
        String type = node.safeGetString("type");
        ExtraComponentConverter converter = extraComponentConverters.get(type);
        if (converter == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": extra component type is not supported");
        }
        return converter.fromJson(node, jsonDataConverter, textureFolderPath);
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
