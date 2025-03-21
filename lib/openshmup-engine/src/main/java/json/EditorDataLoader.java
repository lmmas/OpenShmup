package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.GameConfig;
import engine.EditorDataManager;
import engine.GlobalVars;
import engine.Vec2D;
import engine.entity.Entity;
import engine.entity.EntityType;
import engine.graphics.Animation;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.entity.trajectory.Trajectory;
import engine.entity.trajectory.FixedTrajectory;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.MultiSpawnable;
import engine.scene.spawnable.SceneVisualSpawnInfo;
import engine.scene.spawnable.Spawnable;
import engine.scene.visual.ScrollingBackGround;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class EditorDataLoader {
    final private ObjectMapper objectMapper;
    public EditorDataLoader(){
        this.objectMapper = new ObjectMapper();
    }

    public void loadGameParameters(String filepath) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("game Parameters file not found: filepath '" + filepath + "'");
        }
        checkIfObject(filepath, rootNode);

        JsonNode resolutionNode = checkAndGetArray(filepath, rootNode, "resolution");
        checkSize(filepath, resolutionNode, 2);
        checkIfInt(filepath, resolutionNode.get(0));
        int editionWitdth = resolutionNode.get(0).intValue();
        int editionHeight = resolutionNode.get(1).intValue();
        GameConfig.setEditionResolution(editionWitdth, editionHeight);
        JsonNode levelUINode = checkAndGetObject(filepath, rootNode, "levelUI");
        JsonNode livesNode = checkAndGetObject(filepath, levelUINode, "lives");
        String livesTextureName = checkAndGetString(filepath, livesNode, "fileName");
        GameConfig.LevelUI.Lives.textureFilepath = GlobalVars.Paths.editorTextureFolder + livesTextureName;

        JsonNode livesSizeNode = checkAndGetArray(filepath, livesNode, "size");
        checkSize(filepath, livesSizeNode, 2);
        checkIfFloat(filepath, livesSizeNode.get(0));
        float livesSizeX = livesSizeNode.get(0).floatValue();
        float livesSizeY = livesSizeNode.get(1).floatValue();
        GameConfig.LevelUI.Lives.size = new Vec2D(livesSizeX, livesSizeY);

        JsonNode livesPositionNode = checkAndGetArray(filepath, livesNode, "position");
        checkSize(filepath, livesPositionNode, 2);
        checkIfFloat(filepath, livesPositionNode.get(0));
        float livesPositionX = livesPositionNode.get(0).floatValue();
        float livesPositionY = livesPositionNode.get(1).floatValue();
        GameConfig.LevelUI.Lives.position = new Vec2D(livesPositionX, livesPositionY);

        JsonNode livesStrideNode = checkAndGetArray(filepath, livesNode, "stride");
        checkSize(filepath, livesStrideNode, 2);
        checkIfFloat(filepath, livesStrideNode.get(0));
        float livesStrideX = livesStrideNode.get(0).floatValue();
        float livesStrideY = livesStrideNode.get(1).floatValue();
        GameConfig.LevelUI.Lives.stride = new Vec2D(livesStrideX, livesStrideY);
    }
    public void loadCustomVisuals(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException{
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom visuals file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);
        for(JsonNode visualNode: rootNode){
            checkIfObject(filepath, visualNode);
            int id = checkAndGetInt(filepath, visualNode, "id");
            int layer = checkAndGetInt(filepath, visualNode, "layer");

            String type = checkAndGetString(filepath, visualNode, "type");
            if(type.equals("scrollingImage")){
                String fileName = checkAndGetString(filepath, visualNode, "fileName");
                String imagePath = GlobalVars.Paths.editorTextureFolder + fileName;
                JsonNode sizeNode = checkAndGetArray(filepath, visualNode, "size");
                checkSize(filepath, sizeNode, 2);
                checkIfFloat(filepath, sizeNode.get(0));
                float sizeX = sizeNode.get(0).floatValue();
                float sizeY = sizeNode.get(1).floatValue();

                float speed = checkAndGetFloat(filepath, visualNode, "speed");
                boolean horizontalScrolling = checkAndGetBoolean(filepath, visualNode, "horizontalScrolling");
                editorDataManager.addCustomVisual(id, new ScrollingBackGround(imagePath, layer, sizeX, sizeY, speed, horizontalScrolling));
            }
            else if(type.equals("animation")) {
                JsonNode animationInfoNode = checkAndGetObject(filepath, visualNode, "animationInfo");
                String animationFilepath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, animationInfoNode, "fileName");
                int frameCount = checkAndGetInt(filepath, animationInfoNode, "frameCount");
                JsonNode frameSize = checkAndGetArray(filepath, animationInfoNode, "frameSize");
                checkSize(filepath, frameSize, 2);
                checkIfInt(filepath, frameSize.get(0));
                int frameSizeX = frameSize.get(0).intValue();
                int frameSizeY = frameSize.get(1).intValue();

                JsonNode startingPosition = checkAndGetArray(filepath, animationInfoNode, "startingPosition");
                checkSize(filepath, startingPosition, 2);
                checkIfInt(filepath, startingPosition.get(0));
                int startPosX = startingPosition.get(0).intValue();
                int startPosY = startingPosition.get(1).intValue();

                JsonNode stride = checkAndGetArray(filepath, animationInfoNode, "stride");
                checkSize(filepath, stride, 2);
                checkIfInt(filepath, stride.get(0));
                int strideX = stride.get(0).intValue();
                int strideY = stride.get(1).intValue();

                float framePeriodSeconds = checkAndGetFloat(filepath, visualNode, "framePeriodSeconds");
                boolean looping = checkAndGetBoolean(filepath, visualNode, "looping");

                Vec2D sizeVec = checkAndGetVec2D(filepath, visualNode, "size");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSizeX, frameSizeY, startPosX, startPosY, strideX, strideY);
                editorDataManager.addCustomVisual(id, new Animation(layer, animationInfo, framePeriodSeconds, looping, sizeVec.x, sizeVec.y));
            }
            else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }
        }
    }
    public void loadCustomEntities(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom entities file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);

        for(JsonNode entityNode: rootNode){
            checkIfObject(filepath, entityNode);

            int id = checkAndGetInt(filepath, entityNode, "id");
            String entityTypeString = checkAndGetString(filepath, entityNode, "type");
            EntityType entityType;
            if(entityTypeString.equals("projectile")){
                entityType = EntityType.PROJECTILE;
            }else if(entityTypeString.equals("ship")){
                entityType = EntityType.SHIP;
            }else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }

            Vec2D sizeVec = checkAndGetVec2D(filepath, entityNode, "size");

            AtomicReference<Function<LevelScene, Entity.Builder>> customEntityBuilder = new AtomicReference<>(levelScene ->
                    new Entity.Builder()
                    .setScene(levelScene).setId(id).setType(entityType).setSize(sizeVec.x, sizeVec.y));

            if (entityNode.has("evil")){
                boolean evil= checkAndGetBoolean(filepath, entityNode, "evil");
                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setEvil(evil)));
            }

            if(entityNode.has("deathSpawn")){
                JsonNode deathSpawnNode = entityNode.get("deathSpawn");
                Spawnable deathSpawn;
                if(deathSpawnNode.isArray()){
                    Spawnable[] spawnables = new Spawnable[deathSpawnNode.size()];
                    for(int i = 0; i < deathSpawnNode.size(); i++){
                        spawnables[i] = getSingleSpawnable(filepath, deathSpawnNode.get(i));
                    }
                    deathSpawn = new MultiSpawnable(spawnables);
                }
                else{
                    deathSpawn = getSingleSpawnable(filepath, deathSpawnNode);
                }
                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setDeathSpawn(deathSpawn)));
            }

            if(entityType == EntityType.SHIP && entityNode.has("hp")){
                checkIfInt(filepath, entityNode.get("hp"));
                int hp = entityNode.get("hp").intValue();
                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setHitPoints(hp)));
            }

            if (entityType == EntityType.SHIP && entityNode.has("shot")){
                JsonNode shotNode = checkAndGetObject(filepath, entityNode, "shot");
                float shotPeriod = checkAndGetFloat(filepath, shotNode, "shotPeriod");

                float firstShotTime = checkAndGetFloat(filepath, shotNode, "firstShotTime");

                checkForField(filepath, shotNode, "spawn");
                JsonNode spawnableNode = shotNode.get("spawn");
                Spawnable shot;
                if(spawnableNode.isArray()){
                    ArrayList<Spawnable> spawnableList = new ArrayList<>(spawnableNode.size());
                    for(JsonNode elementNode: spawnableNode){
                        Spawnable spawnable = getSingleSpawnable(filepath, elementNode);
                        spawnableList.add(spawnable);
                    }
                    Spawnable[] spawnables = spawnableList.toArray(Spawnable[]::new);
                    shot = new MultiSpawnable(spawnables);
                }
                else {
                    checkIfObject(filepath, spawnableNode);
                    shot = getSingleSpawnable(filepath, spawnableNode);
                }
                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setShot(shot, shotPeriod, firstShotTime)));
            }

            JsonNode spriteNode = checkAndGetObject(filepath, entityNode, "sprite");
            int layer = checkAndGetInt(filepath, spriteNode, "layer");
            boolean orientable = checkAndGetBoolean(filepath, spriteNode, "orientable");

            if(spriteNode.has("animationInfo")){
                JsonNode animationInfoNode = checkAndGetObject(filepath, spriteNode, "animationInfo");
                String animationFilepath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, animationInfoNode, "fileName");
                int frameCount = checkAndGetInt(filepath, animationInfoNode, "frameCount");
                JsonNode frameSize = checkAndGetArray(filepath, animationInfoNode, "frameSize");
                checkSize(filepath, frameSize, 2);
                checkIfInt(filepath, frameSize.get(0));
                int frameSizeX = frameSize.get(0).intValue();
                int frameSizeY = frameSize.get(1).intValue();

                JsonNode startingPosition = checkAndGetArray(filepath, animationInfoNode, "startingPosition");
                checkSize(filepath, startingPosition, 2);
                checkIfInt(filepath, startingPosition.get(0));
                int startPosX = startingPosition.get(0).intValue();
                int startPosY = startingPosition.get(1).intValue();

                JsonNode stride = checkAndGetArray(filepath, animationInfoNode, "stride");
                checkSize(filepath, stride, 2);
                checkIfInt(filepath, stride.get(0));
                int strideX = stride.get(0).intValue();
                int strideY = stride.get(1).intValue();

                float framePeriodSeconds = checkAndGetFloat(filepath, spriteNode, "framePeriodSeconds");
                boolean looping = checkAndGetBoolean(filepath, spriteNode, "looping");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSizeX, frameSizeY, startPosX, startPosY, strideX, strideY);

                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.createSprite(layer, animationInfo, framePeriodSeconds, looping, orientable)));
            }
            else{
                String texturePath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, spriteNode, "fileName");

                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.createSprite(layer, texturePath, orientable)));
            }
            if(entityNode.has("defaultTrajectory")){
                JsonNode trajectoryNode = checkAndGetObject(filepath, entityNode, "defaultTrajectory");
                Trajectory trajectory;
                if(trajectoryNode.has("id")){
                    checkIfInt(filepath, trajectoryNode.get("id"));
                    int trajectoryId = trajectoryNode.get("id").intValue();
                    trajectory = editorDataManager.getTrajectory(trajectoryId);
                }
                else{
                    String trajectoryType = checkAndGetString(filepath, trajectoryNode, "type");
                    if(trajectoryType.equals("fixed")){
                        String functionXString = checkAndGetString(filepath, trajectoryNode, "functionX");
                        String functionYString = checkAndGetString(filepath, trajectoryNode, "functionY");
                        if(trajectoryNode.has("relative")){
                            boolean relative = checkAndGetBoolean(filepath, trajectoryNode, "relative");
                            try {
                                trajectory = new FixedTrajectory(convertToFunction(functionXString), convertToFunction(functionYString), relative);
                            } catch (LambdaCreationException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                        else{
                            try {
                                trajectory = new FixedTrajectory(convertToFunction(functionXString), convertToFunction(functionYString));
                            } catch (LambdaCreationException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
                    }
                }
                customEntityBuilder.set(customEntityBuilder.get().andThen(builder ->builder.setTrajectory(trajectory)));
            }
            Function<LevelScene, Entity> customEntityConstructor = customEntityBuilder.get().andThen(Entity.Builder::build);
            editorDataManager.addCustomEntity(id, customEntityConstructor);
        }
    }
    public void loadCustomTrajectories(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom trajectories file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);
        for(JsonNode trajectoryNode: rootNode){
            int id = checkAndGetInt(filepath, trajectoryNode, "id");
            String type = checkAndGetString(filepath, trajectoryNode, "type");

            Trajectory newTrajectory;
            if(type.equals( "fixed")) {
                String functionXString = checkAndGetString(filepath, trajectoryNode, "functionX");
                String functionYString = checkAndGetString(filepath, trajectoryNode, "functionY");
                Function<Float, Float> trajectoryFunctionX;
                Function<Float, Float> trajectoryFunctionY;
                try {
                    trajectoryFunctionX = convertToFunction("t -> (float)(" + functionXString + ")");
                    trajectoryFunctionY = convertToFunction("t -> (float)(" + functionYString + ")");
                } catch (LambdaCreationException e) {
                    throw new IllegalArgumentException(e);
                }
                newTrajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
            }
            else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }
            editorDataManager.addTrajectory(id, newTrajectory);
        }
    }
    public void loadCustomTimeline(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom timeline file not found: filepath '" + filepath + "'");
        }
        checkIfObject(filepath, rootNode);
        float duration = checkAndGetFloat(filepath, rootNode, "duration");
        JsonNode spawnsNode = checkAndGetArray(filepath, rootNode, "spawns");
        LevelTimeline newTimeline = new LevelTimeline(editorDataManager, duration);
        for(JsonNode childNode: spawnsNode){
            checkIfObject(filepath, childNode);
            float time = checkAndGetFloat(filepath, childNode, "time");

            checkForField(filepath, childNode, "spawn");
            JsonNode spawnableNode = childNode.get("spawn");
            if(spawnableNode.isArray()){
                for (JsonNode elementNode: spawnableNode){
                    checkIfObject(filepath, elementNode);
                    Spawnable newSpawnable = getSingleSpawnable(filepath, elementNode);
                    newTimeline.addSpawnable(time, newSpawnable);
                }
            }
            else{
                checkIfObject(filepath, spawnableNode);
                Spawnable newSpawnable = getSingleSpawnable(filepath, spawnableNode);
                newTimeline.addSpawnable(time, newSpawnable);
            }
        }
        editorDataManager.addTimeline(newTimeline);
    }
    private Spawnable getSingleSpawnable(String filepath, JsonNode spawnableNode){
        String type = checkAndGetString(filepath, spawnableNode, "type");
        if(type.equals("entity")){
            int id = checkAndGetInt(filepath, spawnableNode, "id");

            Vec2D startingPositionVec = checkAndGetVec2D(filepath, spawnableNode, "startingPosition");

            EntitySpawnInfo spawnInfo;
            if(spawnableNode.has("trajectory")){
                int trajectoryId = checkAndGetInt(filepath, spawnableNode, "trajectory");
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
            }else{
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, -1);
            }
            return spawnInfo;

        }else if(type.equals("visual")) {
            int id = checkAndGetInt(filepath, spawnableNode, "id");
            Vec2D positionVec = checkAndGetVec2D(filepath, spawnableNode, "position");
            return new SceneVisualSpawnInfo(id, positionVec.x, positionVec.y);
        }
        else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }
    private Function<Float, Float> convertToFunction(String expr) throws LambdaCreationException {
        LambdaFactory lambdaFactory = LambdaFactory.get(
                LambdaFactoryConfiguration.get().withImports("static engine.entity.trajectory.MathFloatOverloads.*")
        );
        return lambdaFactory.createLambda(
                expr, new TypeReference<Function<Float, Float>>(){});
    }

    private void checkForField(String filepath, JsonNode node, String field) throws IllegalArgumentException{
        if(!node.has(field)){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfArray(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isArray()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkSize(String filepath, JsonNode node, int size) throws IllegalArgumentException{
        if(node.size() != size){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfObject(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isObject()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfBoolean(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isBoolean()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfInt(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isInt()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfString(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isTextual()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfFloat(String filepath, JsonNode node) throws IllegalArgumentException{
        if(!node.isNumber()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private boolean checkAndGetBoolean(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfBoolean(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName).booleanValue();
    }

    private int checkAndGetInt(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfInt(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName).intValue();
    }

    private float checkAndGetFloat(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfFloat(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName).floatValue();
    }

    private String checkAndGetString(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfString(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName).textValue();
    }

    private JsonNode checkAndGetObject(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfObject(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName);
    }

    private JsonNode checkAndGetArray(String filepath, JsonNode parentNode, String fieldName) throws IllegalArgumentException{
        checkForField(filepath, parentNode, fieldName);
        checkIfArray(filepath, parentNode.get(fieldName));
        return parentNode.get(fieldName);
    }

    private Vec2D checkAndGetVec2D(String filepath, JsonNode parentNode, String fieldName){
        JsonNode arrayNode = checkAndGetArray(filepath, parentNode, fieldName);
        checkSize(filepath, arrayNode, 2);
        checkIfFloat(filepath, arrayNode.get(0));
        checkIfFloat(filepath, arrayNode.get(1));
        float vecX = arrayNode.get(0).floatValue();
        float vecY = arrayNode.get(1).floatValue();
        return new Vec2D(vecX, vecY);
    }
}
