package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.EditorDataManager;
import engine.Game;
import engine.GlobalVars;
import engine.entity.Entity;
import engine.entity.EntitySpawnInfo;
import engine.entity.Trajectory;
import engine.entity.trajectory.FixedTrajectory;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;
import engine.scene.LevelTimeline;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class GameDataLoader {
    final private ObjectMapper objectMapper;
    public GameDataLoader(){
        this.objectMapper = new ObjectMapper();
    }

    public void loadCustomTrajectories(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException, LambdaCreationException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom entities file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);
        for(JsonNode trajectoryNode: rootNode){

            checkForField(filepath, trajectoryNode, "id");
            checkIfInt(filepath, trajectoryNode.get("id"));
            int id = trajectoryNode.get("id").intValue();

            checkForField(filepath, trajectoryNode, "type");
            checkIfString(filepath, trajectoryNode.get("type"));
            String type = trajectoryNode.get("type").textValue();

            Trajectory newTrajectory;
            if(type.equals( "fixed")) {
                checkForField(filepath, trajectoryNode, "functionX");
                checkIfString(filepath, trajectoryNode.get("functionX"));
                String functionXString = trajectoryNode.get("functionX").textValue();

                checkForField(filepath, trajectoryNode, "functionY");
                checkIfString(filepath, trajectoryNode.get("functionY"));
                String functionYString = trajectoryNode.get("functionY").textValue();
                Function<Float, Float> trajectoryFunctionX;
                Function<Float, Float> trajectoryFunctionY;
                try {
                    trajectoryFunctionX = convertToFunction("t -> " + functionXString);
                    trajectoryFunctionY = convertToFunction("t -> " + functionYString);
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

    public void loadCustomEntities(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom entities file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);

        for(JsonNode entityNode: rootNode){
            checkForField(filepath, entityNode, "id");
            checkForField(filepath, entityNode, "size");
            checkForField(filepath, entityNode, "sprite");

            AtomicReference<Function<LevelScene, Entity.Builder>> customEntityBuilder = new AtomicReference<>(levelScene -> new Entity.Builder().setScene(levelScene));

            checkIfInt(filepath, entityNode.get("id"));
            int id = entityNode.get("id").intValue();
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setId(id)));
            JsonNode sizeNode = entityNode.get("size");
            checkIfArray(filepath, sizeNode);
            checkSize(filepath, sizeNode, 2);
            checkIfFloat(filepath, sizeNode.get(0));
            float sizeX = sizeNode.get(0).floatValue();
            float sizeY = sizeNode.get(1).floatValue();
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setSize(sizeX, sizeY)));

            JsonNode spriteNode = entityNode.get("sprite");
            checkIfObject(filepath, spriteNode);

            checkForField(filepath, spriteNode, "layer");
            checkIfInt(filepath, spriteNode.get("layer"));
            int layer = spriteNode.get("layer").intValue();

            checkForField(filepath, spriteNode, "orientable");
            checkIfBoolean(filepath, spriteNode.get("orientable"));
            boolean orientable = spriteNode.get("orientable").booleanValue();

            if(spriteNode.has("animationInfo")){
                JsonNode animationInfoNode = spriteNode.get("animationInfo");
                checkIfObject(filepath, animationInfoNode);

                checkForField(filepath, animationInfoNode, "fileName");
                checkIfString(filepath, animationInfoNode.get("fileName"));
                String animationFilepath = GlobalVars.Paths.editorTextureFolder + animationInfoNode.get("fileName").textValue();

                checkForField(filepath, animationInfoNode, "frameCount");
                checkIfInt(filepath, animationInfoNode.get("frameCount"));
                int frameCount = animationInfoNode.get("frameCount").intValue();

                checkForField(filepath, animationInfoNode, "frameSize");
                JsonNode frameSize = animationInfoNode.get("frameSize");
                checkIfArray(filepath, frameSize);
                checkSize(filepath, frameSize, 2);
                checkIfInt(filepath, frameSize.get(0));
                int frameSizeX = frameSize.get(0).intValue();
                int frameSizeY = frameSize.get(1).intValue();

                checkForField(filepath, animationInfoNode, "startingPosition");
                JsonNode startingPosition = animationInfoNode.get("startingPosition");
                checkIfArray(filepath, startingPosition);
                checkSize(filepath, startingPosition, 2);
                checkIfInt(filepath, startingPosition.get(0));
                int startPosX = startingPosition.get(0).intValue();
                int startPosY = startingPosition.get(1).intValue();

                checkForField(filepath, animationInfoNode, "stride");
                JsonNode stride = animationInfoNode.get("stride");
                checkIfArray(filepath, stride);
                checkSize(filepath, stride, 2);
                checkIfInt(filepath, stride.get(0));
                int strideX = stride.get(0).intValue();
                int strideY = stride.get(1).intValue();

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSizeX, frameSizeY, startPosX, startPosY, strideX, strideY);

                checkForField(filepath, spriteNode, "framePeriodSeconds");
                checkIfFloat(filepath, spriteNode.get("framePeriodSeconds"));
                int framePeriodSeconds = spriteNode.get("framePeriodSeconds").intValue();

                checkForField(filepath, spriteNode, "looping");
                checkIfBoolean(filepath, spriteNode.get("looping"));
                boolean looping = spriteNode.get("looping").booleanValue();

                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.createSprite(layer, animationInfo, framePeriodSeconds, looping, orientable)));
            }
            else{
                checkForField(filepath, spriteNode, "fileName");
                checkIfString(filepath, spriteNode.get("fileName"));
                String texturePath = GlobalVars.Paths.editorTextureFolder + spriteNode.get("fileName").textValue();

                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.createSprite(layer, texturePath, orientable)));
            }
            if(entityNode.has("trajectory")){
                JsonNode trajectoryNode = entityNode.get("trajectory");
                checkIfObject(filepath, trajectoryNode);
                Trajectory trajectory;
                if(trajectoryNode.has("id")){
                    checkIfInt(filepath, trajectoryNode.get("id"));
                    int trajectoryId = trajectoryNode.get("id").intValue();
                    trajectory = editorDataManager.getTrajectory(trajectoryId);
                }
                else{
                    checkForField(filepath, trajectoryNode, "type");
                }
            }
            Function<LevelScene, Entity> customEntityConstructor = customEntityBuilder.get().andThen(Entity.Builder::build);
            editorDataManager.addCustomEntity(id, customEntityConstructor);
        }
    }


    public void loadCustomTimeline(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom entities file not found: filepath '" + filepath + "'");
        }
        checkIfObject(filepath, rootNode);
        checkForField(filepath, rootNode, "duration");
        checkIfFloat(filepath, rootNode.get("duration"));
        float duration = rootNode.get("duration").floatValue();
        checkForField(filepath, rootNode, "spawns");
        JsonNode spawnsNode = rootNode.get("spawns");
        checkIfArray(filepath, spawnsNode);
        LevelTimeline newTimeline = new LevelTimeline(editorDataManager, duration);
        for(JsonNode childNode: spawnsNode){
            checkForField(filepath, childNode, "time");
            checkIfFloat(filepath, childNode.get("time"));
            float time = childNode.get("time").floatValue();

            checkForField(filepath, childNode, "spawnable");
            JsonNode spawnableNode = childNode.get("spawnable");
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
        checkForField(filepath, spawnableNode, "type");
        checkIfString(filepath, spawnableNode.get("type"));
        String type = spawnableNode.get("type").textValue();
        if(type.equals("entity")){
            checkForField(filepath, spawnableNode, "id");
            checkIfInt(filepath, spawnableNode.get("id"));
            int id = spawnableNode.get("id").intValue();

            checkForField(filepath,spawnableNode , "startingPosition");
            checkIfArray(filepath, spawnableNode.get("startingPosition"));
            JsonNode positionNode = spawnableNode.get("startingPosition");
            checkSize(filepath, positionNode, 2);
            checkIfFloat(filepath, positionNode.get(0));
            float startingPositionX = positionNode.get(0).floatValue();
            float startingPositionY = positionNode.get(1).floatValue();

            EntitySpawnInfo spawnInfo;
            if(spawnableNode.has("trajectory")){
                checkIfInt(filepath, spawnableNode.get("trajectory"));
                int trajectoryId = spawnableNode.get("trajectory").intValue();
                spawnInfo = new EntitySpawnInfo(editorDataManager, id, startingPositionX, startingPositionY, trajectoryId);
            }else{
                spawnInfo = new EntitySpawnInfo(editorDataManager, id, startingPositionX, startingPositionY);
            }
            timeline.addSpawnable(time, spawnInfo);
        }else{
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }
    private Function<Float, Float> convertToFunction(String expr) throws LambdaCreationException {
        LambdaFactory lambdaFactory = LambdaFactory.get(
                LambdaFactoryConfiguration.get().withImports("static java.lang.Math.*")
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
}
