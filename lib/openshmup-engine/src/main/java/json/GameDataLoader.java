package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.GlobalVars;
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
import engine.scene.spawnable.MultiSpawnable;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.spawnable.Spawnable;
import engine.types.IVec2D;
import engine.types.Vec2D;
import engine.visual.Animation;
import engine.visual.AnimationInfo;
import engine.visual.ScrollingImage;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static engine.Application.assetManager;
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
        gameConfig.setEditionResolution(resolution.x, resolution.y);

        SafeJsonNode levelUINode = rootNode.checkAndGetObject("levelUI");

        SafeJsonNode livesNode = levelUINode.checkAndGetObject("lives");

        gameConfig.levelUI.lives.textureFilepath = gameDataManager.paths.editorTextureFolder + livesNode.checkAndGetString("fileName");
        gameConfig.levelUI.lives.size = convertToFloatVec(livesNode.checkAndGetIVec2D("size"));
        gameConfig.levelUI.lives.position = convertToFloatVec(livesNode.checkAndGetIVec2D("position"));
        gameConfig.levelUI.lives.stride = convertToFloatVec(livesNode.checkAndGetIVec2D("stride"));
    }

    public void loadCustomDisplays(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> visualList = rootNode.checkAndGetObjectsFromArray();
        for (SafeJsonNode visualNode : visualList) {
            int id = visualNode.checkAndGetInt("id");
            int layer = visualNode.checkAndGetInt("layer");
            String type = visualNode.checkAndGetString("type");
            Vec2D size = convertToFloatVec(visualNode.checkAndGetIVec2D("size"));

            if (type.equals("scrollingImage")) {

                String imagePath = gameDataManager.paths.editorTextureFolder + visualNode.checkAndGetString("fileName");
                boolean horizontalScrolling = visualNode.checkAndGetBoolean("horizontalScrolling");

                int speed = visualNode.checkAndGetInt("speed");
                float normalizedSpeed;
                if (horizontalScrolling) {
                    normalizedSpeed = (float) speed / gameConfig.getEditionWidth();
                }
                else {
                    normalizedSpeed = (float) speed / gameConfig.getEditionHeight();
                }

                gameDataManager.addCustomVisual(id, new ScrollingImage(assetManager.getTexture(imagePath), layer, size.x, size.y, normalizedSpeed, horizontalScrolling));
            }
            else if (type.equals("animation")) {
                SafeJsonNode animationInfoNode = visualNode.checkAndGetObject("animationInfo");

                String animationFilepath = gameDataManager.paths.editorTextureFolder + animationInfoNode.checkAndGetString("fileName");
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

    public void loadCustomEntities(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> customEntities = rootNode.checkAndGetObjectsFromArray();
        for (SafeJsonNode entityNode : customEntities) {

            int id = entityNode.checkAndGetInt("id");
            EntityType type = EntityType.fromString(entityNode.checkAndGetString("type"));
            boolean evil = entityNode.checkAndGetBoolean("evil");

            Vec2D size = convertToFloatVec(entityNode.checkAndGetIVec2D("size"));

            Entity.Builder customEntityBuilder = new Entity.Builder().setId(id).setType(type).setSize(size.x, size.y).setEvil(evil);
            if (entityNode.hasField("hitbox")) {
                SafeJsonNode hitboxNode = entityNode.checkAndGetObject("hitbox");
                String hitboxType = hitboxNode.checkAndGetString("type");
                if (hitboxType.equals("composite")) {
                    String hitboxFileName = hitboxNode.checkAndGetString("fileName");
                    Texture hitboxTexture = assetManager.getTexture(gameDataManager.paths.editorTextureFolder + hitboxFileName);
                    customEntityBuilder = customEntityBuilder.addCompositeHitbox(hitboxTexture, false);
                }
                if (hitboxType.equals("simpleRectangle")) {
                    customEntityBuilder = customEntityBuilder.addRectangleHitbox(false);
                }
            }
            if (entityNode.hasField("deathSpawn")) {
                SafeJsonNode deathSpawnNode = entityNode.checkAndGetObjectOrArray("deathSpawn");
                Spawnable deathSpawn;
                if (deathSpawnNode.isArray()) {
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    List<SafeJsonNode> elementsList = deathSpawnNode.checkAndGetObjectsFromArray();
                    for (var deathSpawnElement : elementsList) {
                        spawnables.add(getSingleSpawnable(deathSpawnElement));
                    }
                    deathSpawn = new MultiSpawnable(spawnables);
                }
                else {
                    deathSpawn = getSingleSpawnable(deathSpawnNode);
                }
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

                SafeJsonNode spawnableNode = shotNode.checkAndGetObjectOrArray("spawn");
                Spawnable shot;
                if (spawnableNode.isArray()) {
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    List<SafeJsonNode> elementsList = spawnableNode.checkAndGetObjectsFromArray();
                    for (var spawnElement : elementsList) {
                        spawnables.add(getSingleSpawnable(spawnElement));
                    }
                    shot = new MultiSpawnable(spawnables);
                }
                else {
                    shot = getSingleSpawnable(spawnableNode);
                }
                customEntityBuilder = customEntityBuilder.createShot(shot, shotPeriod, firstShotTime);
            }

            SafeJsonNode spriteNode = entityNode.checkAndGetObject("sprite");
            int layer = spriteNode.checkAndGetInt("layer");
            boolean orientable = spriteNode.checkAndGetBoolean("orientable");

            if (spriteNode.hasField("animationInfo")) {
                SafeJsonNode animationInfoNode = spriteNode.checkAndGetObject("animationInfo");

                String animationFilepath = gameDataManager.paths.editorTextureFolder + animationInfoNode.checkAndGetString("fileName");
                int frameCount = animationInfoNode.checkAndGetInt("frameCount");
                IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
                IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
                IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

                float framePeriodSeconds = spriteNode.checkAndGetFloat("framePeriodSeconds");
                boolean looping = spriteNode.checkAndGetBoolean("looping");
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

                customEntityBuilder = customEntityBuilder.createSprite(layer, animationTexture, animationInfo, framePeriodSeconds, looping, orientable);
            }
            else {
                String texturePath = gameDataManager.paths.editorTextureFolder + spriteNode.checkAndGetString("fileName");


                customEntityBuilder = customEntityBuilder.createSprite(layer, assetManager.getTexture(texturePath), orientable);
            }
            if (id == 0) {
                customEntityBuilder = customEntityBuilder.setTrajectory(new PlayerControlledTrajectory(GlobalVars.playerSpeed));
            }
            if (entityNode.hasField("defaultTrajectory")) {
                SafeJsonNode trajectoryNode = entityNode.checkAndGetObject("defaultTrajectory");
                Trajectory trajectory;
                if (trajectoryNode.hasField("id")) {
                    int trajectoryId = trajectoryNode.checkAndGetInt("id");
                    trajectory = gameDataManager.getTrajectory(trajectoryId);
                }
                else {
                    String trajectoryType = trajectoryNode.checkAndGetString("type");
                    if (trajectoryType.equals("fixed")) {
                        String functionXString = trajectoryNode.checkAndGetString("functionX");
                        String functionYString = trajectoryNode.checkAndGetString("functionY");
                        if (trajectoryNode.hasField("relative")) {
                            boolean relative = trajectoryNode.checkAndGetBoolean("relative");
                            try {
                                trajectory = new FixedTrajectory(convertToFunction(functionXString), convertToFunction(functionYString), relative);
                            } catch (LambdaCreationException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                        else {
                            try {
                                trajectory = new FixedTrajectory(convertToFunction(functionXString), convertToFunction(functionYString));
                            } catch (LambdaCreationException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
                    }
                }
                customEntityBuilder = customEntityBuilder.setTrajectory(trajectory);
            }
            gameDataManager.addCustomEntity(id, customEntityBuilder.build());
        }
    }

    public void loadCustomTrajectories(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        List<SafeJsonNode> elementList = rootNode.checkAndGetObjectsFromArray();
        for (SafeJsonNode trajectoryNode : elementList) {
            int id = trajectoryNode.checkAndGetInt("id");
            String type = trajectoryNode.checkAndGetString("type");

            Trajectory newTrajectory;
            if (type.equals("fixed")) {
                String functionXString = trajectoryNode.checkAndGetString("functionX");
                String functionYString = trajectoryNode.checkAndGetString("functionY");
                Function<Float, Float> trajectoryFunctionX;
                Function<Float, Float> trajectoryFunctionY;
                try {
                    trajectoryFunctionX = convertToFunction(functionXString);
                    trajectoryFunctionY = convertToFunction(functionYString);
                } catch (LambdaCreationException e) {
                    throw new IllegalArgumentException(e);
                }
                newTrajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
            }
            else {
                throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
            }
            gameDataManager.addTrajectory(id, newTrajectory);
        }
    }

    public void loadCustomTimeline(String filepath) throws IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(rootFolderAbsolutePath + filepath, objectMapper);
        float duration = rootNode.checkAndGetFloat("duration");
        SafeJsonNode spawnsNode = rootNode.checkAndGetObjectArray("spawns");
        LevelTimeline newTimeline = new LevelTimeline(gameDataManager, duration);
        List<SafeJsonNode> elementList = spawnsNode.checkAndGetObjectsFromArray();
        for (SafeJsonNode childNode : elementList) {
            SafeJsonNode spawnableNode = childNode.checkAndGetObjectOrArray("spawn");
            Spawnable newSpawnable;
            if (spawnableNode.isArray()) {
                ArrayList<Spawnable> spawnables = new ArrayList<>();
                List<SafeJsonNode> nodeList = spawnableNode.checkAndGetObjectsFromArray();
                for (var spawnElement : nodeList) {
                    spawnables.add(getSingleSpawnable(spawnElement));
                }
                newSpawnable = new MultiSpawnable(spawnables);
            }
            else {
                newSpawnable = getSingleSpawnable(spawnableNode);
            }
            String type = childNode.checkAndGetString("type");
            if (type.equals("single")) {
                float time = childNode.checkAndGetFloat("time");
                newTimeline.addSpawnable(time, newSpawnable);
            }
            else if (type.equals("interval")) {
                float startTime = childNode.checkAndGetFloat("startTime");
                float endTime = childNode.checkAndGetFloat("endTime");
                float interval = childNode.checkAndGetFloat("interval");
                for (float i = startTime; i <= endTime; i += interval) {
                    newTimeline.addSpawnable(i, newSpawnable);
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

            Vec2D startingPositionVec = convertToFloatVec(spawnableNode.checkAndGetIVec2D("startingPosition"));
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
            Vec2D positionVec = convertToFloatVec(spawnableNode.checkAndGetIVec2D("position"));
            return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
        }
        else {
            throw new IllegalArgumentException("Invalid JSON format: " + spawnableNode.getPath() + ": spwnable type can only be \"display\" or \"entity\"");
        }
    }

    private Function<Float, Float> convertToFunction(String expr) throws LambdaCreationException {
        if (expr.contains("{") || expr.contains("Systems") || expr.contains("Threads")) {
            throw new IllegalArgumentException("Illegal character in trajectory function");
        }
        LambdaFactory lambdaFactory = LambdaFactory.get(
            LambdaFactoryConfiguration.get().withImports("static engine.entity.trajectory.TrajectoryFunctionUtils.MathFloatOverloads.*; import static engine.entity.trajectory.TrajectoryFunctionUtils.*")
        );
        return lambdaFactory.createLambda(
            "t -> (float)(" + expr + ")", new TypeReference<Function<Float, Float>>() {
            });
    }

    private Vec2D convertToFloatVec(IVec2D pixelVec) {
        return new Vec2D((float) pixelVec.x / gameConfig.getEditionWidth(), (float) pixelVec.y / gameConfig.getEditionHeight());
    }
}
