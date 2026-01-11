package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.assets.Texture;
import engine.entity.Entity;
import engine.entity.EntityType;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.gameData.GameConfig;
import engine.gameData.GameDataManager;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.spawnable.Spawnable;
import engine.types.IVec2D;
import engine.types.Vec2D;
import engine.visual.Animation;
import engine.visual.AnimationInfo;
import engine.visual.SceneVisual;
import engine.visual.ScrollingImage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static engine.Engine.assetManager;
import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class GameDataLoader {

    final private GameDataManager gameDataManager;

    final private GameConfig gameConfig;

    final private ObjectMapper objectMapper;

    public GameDataLoader(GameDataManager gameDataManager) {
        this.gameDataManager = gameDataManager;
        this.gameConfig = gameDataManager.config;
        this.objectMapper = new ObjectMapper();
    }

    public void loadGameConfig(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(rootFolderAbsolutePath + filepath, objectMapper);

        IVec2D resolution = rootNode.checkAndGetIVec2D("resolution");
        gameConfig.setNativeResolution(resolution.x, resolution.y);

        SafeJsonNode levelUINode = rootNode.checkAndGetObject("levelUI");

        SafeJsonNode livesNode = levelUINode.checkAndGetObject("lives");

        gameConfig.levelUI.lives.textureFilepath = gameDataManager.paths.gameTextureFolder + livesNode.checkAndGetString("fileName");
        gameConfig.levelUI.lives.size = livesNode.checkAndGetVec2D("size");
        gameConfig.levelUI.lives.position = livesNode.checkAndGetVec2D("position");
        gameConfig.levelUI.lives.stride = livesNode.checkAndGetVec2D("stride");
    }

    public void loadGameVisuals(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> visualList = rootNode.checkAndGetObjectListFromArray();
        for (SafeJsonNode visualNode : visualList) {
            int id = visualNode.checkAndGetInt("id");
            int layer = visualNode.checkAndGetInt("layer");
            String type = visualNode.checkAndGetString("type");
            Vec2D size = visualNode.checkAndGetVec2D("size");

            if (type.equals("scrollingImage")) {

                String imagePath = gameDataManager.paths.gameTextureFolder + visualNode.checkAndGetString("fileName");
                boolean horizontalScrolling = visualNode.checkAndGetBoolean("horizontalScrolling");

                float speed = visualNode.checkAndGetFloat("speed");

                gameDataManager.addCustomVisual(id, new ScrollingImage(assetManager.getTexture(imagePath), layer, size.x, size.y, speed, horizontalScrolling));
            }
            else if (type.equals("animation")) {
                SafeJsonNode animationInfoNode = visualNode.checkAndGetObject("animationInfo");

                String animationFilepath = gameDataManager.paths.gameTextureFolder + animationInfoNode.checkAndGetString("fileName");
                int frameCount = animationInfoNode.checkAndGetInt("frameCount");
                IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
                IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
                IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

                float framePeriodSeconds = visualNode.checkAndGetFloat("framePeriodSeconds");
                boolean looping = visualNode.checkAndGetBoolean("looping");

                Texture animationTexture = assetManager.getTexture(animationFilepath);
                int animationTextureWidth = animationTexture.getWidth();
                int animationTextureHeight = animationTexture.getHeight();

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount,
                    (float) frameSize.x / animationTextureWidth,
                    (float) frameSize.y / animationTextureHeight,
                    (float) startingPosition.x / animationTextureWidth,
                    (float) startingPosition.y / animationTextureHeight,
                    (float) stride.x / animationTextureWidth,
                    (float) stride.y / animationTextureHeight);
                gameDataManager.addCustomVisual(id, new Animation(layer, assetManager.getTexture(animationFilepath), animationInfo, framePeriodSeconds, looping, size.x, size.y));
            }
            else {
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }
        }
    }

    public void loadGameTrajectories(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> elementList = rootNode.checkAndGetObjectListFromArray();
        for (SafeJsonNode trajectoryNode : elementList) {
            int id = trajectoryNode.checkAndGetInt("id");
            String type = trajectoryNode.checkAndGetString("type");

            Trajectory newTrajectory;
            if (type.equals("fixed")) {
                String functionXString = trajectoryNode.checkAndGetString("functionX");
                String functionYString = trajectoryNode.checkAndGetString("functionY");
                Function<Double, Float> trajectoryFunctionX = convertToFunction(functionXString);
                Function<Double, Float> trajectoryFunctionY = convertToFunction(functionYString);
                newTrajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
            }
            else if (type.equals("player")) {
                float playerMovementSpeed = trajectoryNode.checkAndGetFloat("playerMovementSpeed");
                newTrajectory = new PlayerControlledTrajectory(playerMovementSpeed);
            }
            else {
                throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
            }
            gameDataManager.addTrajectory(id, newTrajectory);
        }
    }

    public void loadGameEntities(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> customEntities = rootNode.checkAndGetObjectListFromArray();
        for (SafeJsonNode entityNode : customEntities) {

            int id = entityNode.checkAndGetInt("id");
            EntityType type = EntityType.fromString(entityNode.checkAndGetString("type"));
            boolean evil = entityNode.checkAndGetBoolean("evil");

            Vec2D size = entityNode.checkAndGetVec2D("size");

            Entity.Builder customEntityBuilder = new Entity.Builder().setId(id).setType(type).setSize(size.x, size.y).setEvil(evil);
            if (entityNode.hasField("hitbox")) {
                SafeJsonNode hitboxNode = entityNode.checkAndGetObject("hitbox");
                String hitboxType = hitboxNode.checkAndGetString("type");
                if (hitboxType.equals("composite")) {
                    String hitboxFileName = hitboxNode.checkAndGetString("fileName");
                    Texture hitboxTexture = assetManager.getTexture(gameDataManager.paths.gameTextureFolder + hitboxFileName);
                    customEntityBuilder = customEntityBuilder.addCompositeHitbox(hitboxTexture, false);
                }
                if (hitboxType.equals("simpleRectangle")) {
                    customEntityBuilder = customEntityBuilder.addRectangleHitbox(false);
                }
            }
            if (entityNode.hasField("deathSpawn")) {
                SafeJsonNode deathSpawnNode = entityNode.checkAndGetArray("deathSpawn");
                List<Spawnable> deathSpawn;
                ArrayList<Spawnable> spawnables = new ArrayList<>();
                List<SafeJsonNode> elementsList = deathSpawnNode.checkAndGetObjectListFromArray();
                for (var deathSpawnElement : elementsList) {
                    spawnables.add(getSingleSpawnable(deathSpawnElement));
                }
                deathSpawn = spawnables;
                customEntityBuilder = customEntityBuilder.setDeathSpawn(deathSpawn);
            }

            if (type == EntityType.SHIP && entityNode.hasField("hp")) {
                int hp = entityNode.checkAndGetInt("hp");
                customEntityBuilder = customEntityBuilder.setHitPoints(hp);
            }

            if (entityNode.hasField("shot")) {
                SafeJsonNode shotNode = entityNode.checkAndGetObject("shot");

                float shotPeriod = shotNode.checkAndGetFloat("shotPeriod");
                float firstShotTime = shotNode.checkAndGetFloat("firstShotTime");

                SafeJsonNode spawnableNode = shotNode.checkAndGetArray("spawn");
                List<Spawnable> shot;
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                List<SafeJsonNode> elementsList = spawnableNode.checkAndGetObjectListFromArray();
                    for (var spawnElement : elementsList) {
                        spawnables.add(getSingleSpawnable(spawnElement));
                    }
                shot = spawnables;
                customEntityBuilder = customEntityBuilder.createShot(shot, shotPeriod, firstShotTime);
            }

            int spriteVisualId = entityNode.checkAndGetInt("spriteVisualId");

            SceneVisual sprite = gameDataManager.getGameVisual(spriteVisualId);
            customEntityBuilder = customEntityBuilder.setSprite(sprite);

            if (entityNode.hasField("defaultTrajectoryId")) {
                int defaultTrajectoryId = entityNode.checkAndGetInt("defaultTrajectoryId");
                Trajectory trajectory = gameDataManager.getTrajectory(defaultTrajectoryId);
                customEntityBuilder = customEntityBuilder.setTrajectory(trajectory);
            }
            gameDataManager.addCustomEntity(id, customEntityBuilder.build());
        }
    }

    public void loadGameTimeline(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        float duration = rootNode.checkAndGetFloat("duration");
        SafeJsonNode spawnsNode = rootNode.checkAndGetArray("spawns");
        LevelTimeline newTimeline = new LevelTimeline(gameDataManager, duration);
        List<SafeJsonNode> elementList = spawnsNode.checkAndGetObjectListFromArray();
        for (SafeJsonNode childNode : elementList) {
            SafeJsonNode spawnableNode = childNode.checkAndGetArray("spawn");
            ArrayList<Spawnable> newSpawnables = new ArrayList<>();
            List<SafeJsonNode> nodeList = spawnableNode.checkAndGetObjectListFromArray();
            for (var spawnElement : nodeList) {
                newSpawnables.add(getSingleSpawnable(spawnElement));
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
                throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
            }

        }
        gameDataManager.addTimeline(newTimeline);
    }

    private Spawnable getSingleSpawnable(SafeJsonNode spawnableNode) {
        String type = spawnableNode.checkAndGetString("type");
        if (type.equals("entity")) {
            int id = spawnableNode.checkAndGetInt("id");

            Vec2D startingPositionVec = spawnableNode.checkAndGetVec2D("startingPosition");
            EntitySpawnInfo spawnInfo;
            if (spawnableNode.hasField("trajectory")) {
                int trajectoryId = spawnableNode.checkAndGetInt("trajectory");
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
            }
            else {
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, -1);
            }
            return spawnInfo;

        }
        else if (type.equals("display")) {
            int id = spawnableNode.checkAndGetInt("id");
            Vec2D positionVec = spawnableNode.checkAndGetVec2D("position");
            return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
        }
        else {
            throw new IllegalArgumentException("Invalid JSON format: " + spawnableNode.getFullPath() + ": spwnable type can only be \"display\" or \"entity\"");
        }
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
}
