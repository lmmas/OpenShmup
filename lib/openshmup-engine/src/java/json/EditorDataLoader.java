package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.EditorDataManager;
import engine.GlobalVars;
import engine.entity.Entity;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.entity.trajectory.Trajectory;
import engine.entity.trajectory.FixedTrajectory;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.SceneVisualSpawnInfo;
import engine.scene.visual.ScrollingBackGround;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        GlobalVars.EditionParameters.setEditionResolution(editionWitdth, editionHeight);
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
            if(type.equals("scrollingBackground")){
                String fileName = checkAndGetString(filepath, visualNode, "fileName");
                String imagePath = GlobalVars.Paths.editorTextureFolder + fileName;
                JsonNode sizeNode = checkAndGetArray(filepath, visualNode, "size");
                checkSize(filepath, sizeNode, 2);
                checkIfFloat(filepath, sizeNode.get(0));
                float sizeX = sizeNode.get(0).floatValue();
                float sizeY = sizeNode.get(1).floatValue();

                float speed = checkAndGetFloat(filepath, visualNode, "speed");
                boolean horizontalScrolling = checkAndGetBoolean(filepath, visualNode, "horizontalScrolling");
                editorDataManager.addCustomVisual(id, scene -> new ScrollingBackGround(imagePath, scene, layer, sizeX, sizeY, speed, horizontalScrolling));
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

            JsonNode sizeNode = checkAndGetArray(filepath, entityNode, "size");
            checkSize(filepath, sizeNode, 2);
            checkIfFloat(filepath, sizeNode.get(0));
            float sizeX = sizeNode.get(0).floatValue();
            float sizeY = sizeNode.get(1).floatValue();

            AtomicReference<Function<LevelScene, Entity.Builder>> customEntityBuilder = new AtomicReference<>(levelScene -> new Entity.Builder().setScene(levelScene));
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setId(id)));
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setSize(sizeX, sizeY)));

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
                String texturePath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, sizeNode, "fileName");

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
                    String type = checkAndGetString(filepath, trajectoryNode, "type");
                    if(type.equals("fixed")){
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
                    addSingleSpawnableToTimeline(filepath, editorDataManager, newTimeline, elementNode, time);
                }
            }
            else{
                checkIfObject(filepath, spawnableNode);
                addSingleSpawnableToTimeline(filepath, editorDataManager,newTimeline, spawnableNode, time);
            }
        }
        editorDataManager.addTimeline(newTimeline);
    }
    private void addSingleSpawnableToTimeline(String filepath, EditorDataManager editorDataManager,LevelTimeline timeline, JsonNode spawnableNode, float time){
        String type = checkAndGetString(filepath, spawnableNode, "type");
        if(type.equals("entity")){
            int id = checkAndGetInt(filepath, spawnableNode, "id");

            JsonNode positionNode = checkAndGetArray(filepath, spawnableNode, "startingPosition");
            checkSize(filepath, positionNode, 2);
            checkIfFloat(filepath, positionNode.get(0));
            float startingPositionX = positionNode.get(0).floatValue();
            float startingPositionY = positionNode.get(1).floatValue();

            EntitySpawnInfo spawnInfo;
            if(spawnableNode.has("trajectory")){
                int trajectoryId = checkAndGetInt(filepath, spawnableNode, "trajectory");
                spawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, trajectoryId);
            }else{
                spawnInfo = new EntitySpawnInfo(id, startingPositionX, startingPositionY, -1);
            }
            timeline.addSpawnable(time, spawnInfo);
        }else if(type.equals("visual")) {
            int id = checkAndGetInt(filepath, spawnableNode, "id");

            JsonNode positionNode = checkAndGetArray(filepath, spawnableNode, "position");
            checkSize(filepath, positionNode, 2);
            checkIfFloat(filepath, positionNode.get(0));
            float positionX = positionNode.get(0).floatValue();
            float positionY = positionNode.get(1).floatValue();
            SceneVisualSpawnInfo spawnInfo = new SceneVisualSpawnInfo(id, positionX, positionY);
            timeline.addSpawnable(time, spawnInfo);
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
}
