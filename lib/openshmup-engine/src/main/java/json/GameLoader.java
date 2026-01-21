package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.gameData.GameConfig;
import engine.gameData.GameDataManager;
import engine.gameData.GamePaths;
import engine.level.LevelTimeline;
import engine.level.entity.Entity;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.IVec2D;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static json.factories.EntityFactories.projectileFactory;
import static json.factories.EntityFactories.shipFactory;
import static json.factories.ExtraComponentFactories.shotFactory;
import static json.factories.HitboxFactories.compositeHitboxFactory;
import static json.factories.HitboxFactories.simpleRectangleHitboxFactory;
import static json.factories.SpawnableFactories.entitySpawnInfoFactory;
import static json.factories.SpawnableFactories.visualSpawnInfoFactory;
import static json.factories.TrajectoryFactories.fixedTrajectoryFactory;
import static json.factories.TrajectoryFactories.playerControlledTrajectoryFactory;
import static json.factories.VisualFactories.animationFactory;
import static json.factories.VisualFactories.scrollingImageFactory;

final public class GameLoader {

    final private ObjectMapper objectMapper;

    final private Map<String, BiFunction<SafeJsonNode, Path, SceneVisual>> visualFactories;

    final private Map<String, Function<SafeJsonNode, Trajectory>> trajectoryFactories;

    final private Map<String, BiFunction<SafeJsonNode, Path, Hitbox>> hitboxFactories;

    final private Map<String, Function<SafeJsonNode, Spawnable>> spawnableFactories;

    final private Map<String, TetraFunction<SafeJsonNode, GameDataManager, GameLoader, Boolean, ExtraComponent>> extraComponentFactories;

    final private Map<String, TriFunction<SafeJsonNode, GameLoader, GameDataManager, Entity>> entityFactories;

    public GameLoader() {
        this.objectMapper = new ObjectMapper();

        this.visualFactories = new HashMap<>(2);
        visualFactories.put("scrollingImage", scrollingImageFactory);
        visualFactories.put("animation", animationFactory);

        this.trajectoryFactories = new HashMap<>(2);
        trajectoryFactories.put("fixed", fixedTrajectoryFactory);
        trajectoryFactories.put("player", playerControlledTrajectoryFactory);

        this.hitboxFactories = new HashMap<>(2);
        hitboxFactories.put("simpleRectangle", simpleRectangleHitboxFactory);
        hitboxFactories.put("composite", compositeHitboxFactory);

        this.spawnableFactories = new HashMap<>(2);
        spawnableFactories.put("display", visualSpawnInfoFactory);
        spawnableFactories.put("entity", entitySpawnInfoFactory);

        this.extraComponentFactories = new HashMap<>(1);
        extraComponentFactories.put("shot", shotFactory);

        this.entityFactories = new HashMap<>(2);
        entityFactories.put("ship", shipFactory);
        entityFactories.put("projectile", projectileFactory);
    }

    public SceneVisual visualFromJson(SafeJsonNode node, Path textureFolderpath) {
        String type = node.checkAndGetString("type");
        var factory = visualFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": visual type is not supported");
        }
        return factory.apply(node, textureFolderpath);
    }

    public Trajectory trajectoryFromJSon(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        var factory = trajectoryFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": trajectory type is not supported");
        }
        return factory.apply(node);
    }

    public Hitbox hitboxFromJson(SafeJsonNode node, GamePaths paths) {
        String type = node.checkAndGetString("type");
        var factory = hitboxFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": hitbox type is not supported");
        }
        return factory.apply(node, paths.gameTextureFolder);
    }

    public Spawnable spawnableFromJson(SafeJsonNode node) {
        String type = node.checkAndGetString("type");
        var factory = spawnableFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": spawnable type is not supported");
        }
        return factory.apply(node);
    }

    public ExtraComponent extraComponentFromJson(SafeJsonNode node, GameDataManager gameData, boolean isPlayer) {
        String type = "shot";
        var factory = extraComponentFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": extra component type is not supported");
        }
        return factory.apply(node, gameData, this, isPlayer);
    }

    public Entity entityFromJson(SafeJsonNode node, GameDataManager gameData) {
        String type = node.checkAndGetString("type");
        var factory = entityFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid JSON format: " + node.getFullPath() + ": entity type is not supported");
        }
        return factory.apply(node, this, gameData);
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

        gameConfig.levelUI.lives.textureFilepath = paths.gameTextureFolder.resolve(livesNode.checkAndGetString("fileName"));
        gameConfig.levelUI.lives.size = livesNode.checkAndGetVec2D("size");
        gameConfig.levelUI.lives.position = livesNode.checkAndGetVec2D("position");
        gameConfig.levelUI.lives.stride = livesNode.checkAndGetVec2D("stride");
        return gameConfig;
    }

    public void loadGameConfig(GameDataManager gameData) {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(gameData.paths.gameConfigFile, objectMapper);
        gameData.config = gameConfigFromJson(rootNode, gameData.paths);
    }

    public void loadGameVisuals(GameDataManager gameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(gameData.paths.gameVisualsFile, objectMapper);
        List<SafeJsonNode> visualNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var visualNode : visualNodesList) {
            int id = visualNode.checkAndGetInt("id");
            SceneVisual newVisual = visualFromJson(visualNode, gameData.paths.gameTextureFolder);
            gameData.addCustomVisual(id, newVisual);
        }
    }

    public void loadGameTrajectories(GameDataManager gameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(gameData.paths.gameTrajectoriesFile, objectMapper);
        List<SafeJsonNode> trajectoryNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var trajectoryNode : trajectoryNodesList) {
            int id = trajectoryNode.checkAndGetInt("id");
            Trajectory trajectory = trajectoryFromJSon(trajectoryNode);
            gameData.addTrajectory(id, trajectory);
        }
    }

    public void loadGameEntities(GameDataManager gameData) {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(gameData.paths.gameEntitiesFile, objectMapper);
        List<SafeJsonNode> entityNodesList = rootNode.checkAndGetObjectListFromArray();
        for (var entityNode : entityNodesList) {
            int id = entityNode.checkAndGetInt("id");
            Entity trajectory = entityFromJson(entityNode, gameData);
            gameData.addCustomEntity(id, trajectory);
        }
    }

    public void loadGameTimelines(GameDataManager gameData) {
        ObjectMapper mapper = new ObjectMapper();
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(gameData.paths.gameTimelineFile, mapper);
        gameData.addTimeline(timelineFromJson(rootNode, gameData));
    }
}
