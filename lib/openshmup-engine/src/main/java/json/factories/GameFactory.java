package json.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.entity.Entity;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.hitbox.Hitbox;
import engine.entity.trajectory.Trajectory;
import engine.gameData.GameConfig;
import engine.gameData.GameDataManager;
import engine.gameData.GamePaths;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.Spawnable;
import engine.types.IVec2D;
import engine.visual.SceneVisual;
import json.SafeJsonNode;
import json.factories.entity.EntityFactory;
import json.factories.entity.ProjectileFactory;
import json.factories.entity.ShipFactory;
import json.factories.extraComponent.ExtraComponentFactory;
import json.factories.extraComponent.ShotFactory;
import json.factories.hitbox.CompositeHitboxFactory;
import json.factories.hitbox.HitboxFactory;
import json.factories.hitbox.SimpleRectangleHitboxFactory;
import json.factories.spawnable.EntitySpawnInfoFactory;
import json.factories.spawnable.SpawnableFactory;
import json.factories.spawnable.VisualSpawnInfoFactory;
import json.factories.trajectory.FixedTrajectoryFactory;
import json.factories.trajectory.PlayerTrajectoryFactory;
import json.factories.trajectory.TrajectoryFactory;
import json.factories.visual.AnimationFactory;
import json.factories.visual.ScrollingImageFactory;
import json.factories.visual.VisualFactory;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

public class GameFactory {

    final private Map<String, VisualFactory> visualFactories;

    final private Map<String, TrajectoryFactory> trajectoryFactories;

    final private Map<String, HitboxFactory> hitboxFactories;

    final private Map<String, SpawnableFactory> spawnableFactories;

    final private Map<String, ExtraComponentFactory> extraComponentFactories;

    final private Map<String, EntityFactory> entityFactories;

    public GameFactory() {
        this.visualFactories = new HashMap<>(2);
        visualFactories.put("scrollingImage", new ScrollingImageFactory());
        visualFactories.put("animation", new AnimationFactory());

        this.trajectoryFactories = new HashMap<>(2);
        trajectoryFactories.put("fixed", new FixedTrajectoryFactory());
        trajectoryFactories.put("player", new PlayerTrajectoryFactory());

        this.hitboxFactories = new HashMap<>(2);
        hitboxFactories.put("simpleRectangle", new SimpleRectangleHitboxFactory());
        hitboxFactories.put("composite", new CompositeHitboxFactory());

        this.spawnableFactories = new HashMap<>(2);
        spawnableFactories.put("visual", new VisualSpawnInfoFactory());
        spawnableFactories.put("entity", new EntitySpawnInfoFactory());

        this.extraComponentFactories = new HashMap<>(1);
        extraComponentFactories.put("shot", new ShotFactory());

        this.entityFactories = new HashMap<>(2);
        entityFactories.put("ship", new ShipFactory());
        entityFactories.put("projectile", new ProjectileFactory());
    }

    public static Function<Double, Float> convertToFunction(String expressionString) {
        return t -> {
            Expression expr = new ExpressionBuilder(expressionString)
                .variable("t")
                .build()
                .setVariable("t", t);
            return (float) expr.evaluate();
        };
    }

    public SceneVisual visualFromJson(SafeJsonNode node, Path textureFolderpath) {
        String type = node.checkAndGetString("type");
        VisualFactory factory = visualFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return factory.fromJson(node, textureFolderpath);
    }

    public Trajectory trajectoryFromJSon(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        TrajectoryFactory factory = trajectoryFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return factory.fromJson(node);
    }

    public Hitbox hitboxFromJson(SafeJsonNode node, GamePaths paths) {
        String type = node.checkAndGetString("type");
        HitboxFactory factory = hitboxFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return factory.fromJson(node, paths);
    }

    public Spawnable spawnableFromJson(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        SpawnableFactory factory = spawnableFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return factory.fromJson(node);
    }

    public ExtraComponent extraComponentFromJson(SafeJsonNode node, GameDataManager gameData, boolean isPlayer) {
        String type = "shot";
        ExtraComponentFactory factory = extraComponentFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": extra component type is not supported");
        }
        return factory.fromJson(node, gameData, this, isPlayer);
    }

    public Entity entityFromJson(SafeJsonNode node, GameDataManager gameData) {
        String type = node.checkAndGetString("type");
        EntityFactory factory = entityFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": entity type is not supported");
        }
        return factory.fromJson(node, this, gameData);
    }

    public LevelTimeline timelineFromJson(SafeJsonNode node, GameDataManager gameData) {
        float duration = node.checkAndGetFloat("duration");
        SafeJsonNode spawnsNode = node.checkAndGetArray("spawns");
        LevelTimeline newTimeline = new LevelTimeline(gameData, duration);
        List<SafeJsonNode> elementList = spawnsNode.checkAndGetObjectListFromArray();
        for (SafeJsonNode childNode : elementList) {
            SafeJsonNode spawnableNode = childNode.checkAndGetArray("spawn");
            ArrayList<Spawnable> newSpawnables = new ArrayList<>();
            List<SafeJsonNode> nodeList = spawnableNode.checkAndGetObjectListFromArray();
            for (var spawnElement : nodeList) {
                newSpawnables.add(spawnableFromJson(spawnElement));
            }
            String type = childNode.checkAndGetString("type");
            if (type.equals("single")) {
                float time = childNode.checkAndGetFloat("time");
                for (var spawnable : newSpawnables) {
                    newTimeline.addSpawnable(time, spawnable);
                }
            }
            else if (type.equals("interval")) {
                float startTime = childNode.checkAndGetFloat("startTime");
                float endTime = childNode.checkAndGetFloat("endTime");
                float interval = childNode.checkAndGetFloat("interval");
                for (float i = startTime; i <= endTime; i += interval) {
                    for (var spawnable : newSpawnables) {
                        newTimeline.addSpawnable(i, spawnable);
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Invalid JSON format: " + childNode.getFullPath() + ": spawn type is not supported");
            }

        }
        return newTimeline;
    }

    public GameConfig gameConfigFromJson(SafeJsonNode node, GamePaths paths) {
        GameConfig gameConfig = new GameConfig();
        IVec2D resolution = node.checkAndGetIVec2D("resolution");
        gameConfig.setNativeResolution(resolution.x, resolution.y);

        SafeJsonNode levelUINode = node.checkAndGetObject("levelUI");

        SafeJsonNode livesNode = levelUINode.checkAndGetObject("lives");

        gameConfig.levelUI.lives.textureFilepath = paths.gameTextureFolder + livesNode.checkAndGetString("fileName");
        gameConfig.levelUI.lives.size = livesNode.checkAndGetVec2D("size");
        gameConfig.levelUI.lives.position = livesNode.checkAndGetVec2D("position");
        gameConfig.levelUI.lives.stride = livesNode.checkAndGetVec2D("stride");
        return gameConfig;
    }

    public void loadGameConfig(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(rootFolderAbsolutePath + gameData.paths.gameConfigFile, mapper);
        gameData.config = gameConfigFromJson(rootNode, gameData.paths);
    }

    public void loadGameVisuals(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + gameData.paths.gameVisualsFile, mapper);
        List<SafeJsonNode> visualNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var visualNode : visualNodesList) {
            int id = visualNode.checkAndGetInt("id");
            SceneVisual newVisual = visualFromJson(visualNode, Paths.get(gameData.paths.gameTextureFolder));
            gameData.addCustomVisual(id, newVisual);
        }
    }

    public void loadGameTrajectories(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + gameData.paths.gameTrajectoriesFile, mapper);
        List<SafeJsonNode> trajectoryNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var trajectoryNode : trajectoryNodesList) {
            int id = trajectoryNode.checkAndGetInt("id");
            Trajectory trajectory = trajectoryFromJSon(trajectoryNode);
            gameData.addTrajectory(id, trajectory);
        }
    }

    public void loadGameEntities(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + gameData.paths.gameEntitiesFile, mapper);
        List<SafeJsonNode> entityNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var entityNode : entityNodesList) {
            int id = entityNode.checkAndGetInt("id");
            Entity trajectory = entityFromJson(entityNode, gameData);
            gameData.addCustomEntity(id, trajectory);
        }
    }

    public void loadGameTimelines(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(rootFolderAbsolutePath + gameData.paths.gameTimelineFile, mapper);
        gameData.addTimeline(timelineFromJson(rootNode, gameData));
    }
}
