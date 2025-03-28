package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.*;
import engine.entity.Entity;
import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.scene.display.Animation;
import engine.scene.spawnable.EntitySpawnInfo;
import engine.entity.trajectory.Trajectory;
import engine.entity.trajectory.FixedTrajectory;
import engine.scene.display.AnimationInfo;
import engine.scene.LevelTimeline;
import engine.scene.spawnable.MultiSpawnable;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.scene.spawnable.Spawnable;
import engine.scene.display.ScrollingImage;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

        IVec2D resolution = checkAndGetIVec2D(filepath, rootNode, "resolution");
        GameConfig.setEditionResolution(resolution.x, resolution.y);

        JsonNode levelUINode = checkAndGetObject(filepath, rootNode, "levelUI");

        JsonNode livesNode = checkAndGetObject(filepath, levelUINode, "lives");

        GameConfig.LevelUI.Lives.textureFilepath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, livesNode, "fileName");
        GameConfig.LevelUI.Lives.size = checkAndConvertIntArrayToVec2D(filepath, livesNode, "size");
        GameConfig.LevelUI.Lives.position = checkAndConvertIntArrayToVec2D(filepath, livesNode, "position");
        GameConfig.LevelUI.Lives.stride = checkAndConvertIntArrayToVec2D(filepath, livesNode, "stride");
    }
    public void loadCustomDisplays(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException{
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom displays file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);
        for(JsonNode displayNode: rootNode){
            checkIfObject(filepath, displayNode);

            int id = checkAndGetInt(filepath, displayNode, "id");
            int layer = checkAndGetInt(filepath, displayNode, "layer");
            String type = checkAndGetString(filepath, displayNode, "type");
            Vec2D size = checkAndConvertIntArrayToVec2D(filepath, displayNode, "size");

            if(type.equals("scrollingImage")){

                String imagePath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, displayNode, "fileName");
                boolean horizontalScrolling = checkAndGetBoolean(filepath, displayNode, "horizontalScrolling");

                int speed = checkAndGetInt(filepath, displayNode, "speed");
                float normalizedSpeed;
                if(horizontalScrolling){
                    normalizedSpeed = (float) speed / GameConfig.getEditionWidth();
                }else{
                    normalizedSpeed = (float) speed / GameConfig.getEditionHeight();
                }

                editorDataManager.addCustomDisplays(id, new ScrollingImage(imagePath, layer, size.x, size.y, normalizedSpeed, horizontalScrolling));
            }
            else if(type.equals("animation")) {
                JsonNode animationInfoNode = checkAndGetObject(filepath, displayNode, "animationInfo");

                String animationFilepath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, animationInfoNode, "fileName");
                int frameCount = checkAndGetInt(filepath, animationInfoNode, "frameCount");
                IVec2D frameSize = checkAndGetIVec2D(filepath, animationInfoNode, "frameSize");
                IVec2D startingPosition = checkAndGetIVec2D(filepath, animationInfoNode, "startingPosition");
                IVec2D stride = checkAndGetIVec2D(filepath, animationInfoNode, "stride");

                float framePeriodSeconds = checkAndGetFloat(filepath, displayNode, "framePeriodSeconds");
                boolean looping = checkAndGetBoolean(filepath, displayNode, "looping");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSize.x, frameSize.y, startingPosition.x, startingPosition.y, stride.x, stride.y);
                editorDataManager.addCustomDisplays(id, new Animation(layer, animationInfo, framePeriodSeconds, looping, size.x, size.y));
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
            boolean isShip = checkAndGetBoolean(filepath, entityNode, "hasHP");

            Vec2D size = checkAndConvertIntArrayToVec2D(filepath, entityNode, "size");

            Entity.Builder customEntitybuilder = new Entity.Builder().setId(id).setSize(size.x, size.y);

            if (entityNode.has("evil")){
                boolean evil= checkAndGetBoolean(filepath, entityNode, "evil");
                customEntitybuilder = customEntitybuilder.setEvil(evil);
            }

            if(entityNode.has("deathSpawn")){
                JsonNode deathSpawnNode = entityNode.get("deathSpawn");
                Spawnable deathSpawn;
                if(deathSpawnNode.isArray()){
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    for(var deathSpawnElement: deathSpawnNode){
                        checkIfObject(filepath, deathSpawnElement);
                        spawnables.add(getSingleSpawnable(filepath, deathSpawnElement));
                    }
                    deathSpawn = new MultiSpawnable(spawnables);
                }
                else{
                    deathSpawn = getSingleSpawnable(filepath, deathSpawnNode);
                }
                customEntitybuilder = customEntitybuilder.setDeathSpawn(deathSpawn);
            }

            if(isShip && entityNode.has("hp")){
                checkIfInt(filepath, entityNode.get("hp"));
                int hp = entityNode.get("hp").intValue();
                customEntitybuilder = customEntitybuilder.setHitPoints(hp);
            }

            if (entityNode.has("shot")){
                JsonNode shotNode = checkAndGetObject(filepath, entityNode, "shot");

                float shotPeriod = checkAndGetFloat(filepath, shotNode, "shotPeriod");
                float firstShotTime = checkAndGetFloat(filepath, shotNode, "firstShotTime");

                checkForField(filepath, shotNode, "spawn");
                JsonNode spawnableNode = shotNode.get("spawn");
                Spawnable shot;
                if(spawnableNode.isArray()){
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    for(var spawnElement: spawnableNode){
                        checkIfObject(filepath, spawnElement);
                        spawnables.add(getSingleSpawnable(filepath, spawnElement));
                    }
                    shot = new MultiSpawnable(spawnables);
                }
                else {
                    checkIfObject(filepath, spawnableNode);
                    shot = getSingleSpawnable(filepath, spawnableNode);
                }
                customEntitybuilder = customEntitybuilder.createShot(shot, shotPeriod, firstShotTime);
            }

            JsonNode spriteNode = checkAndGetObject(filepath, entityNode, "sprite");
            int layer = checkAndGetInt(filepath, spriteNode, "layer");
            boolean orientable = checkAndGetBoolean(filepath, spriteNode, "orientable");

            if(spriteNode.has("animationInfo")){
                JsonNode animationInfoNode = checkAndGetObject(filepath, spriteNode, "animationInfo");

                String animationFilepath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, animationInfoNode, "fileName");
                int frameCount = checkAndGetInt(filepath, animationInfoNode, "frameCount");
                IVec2D frameSize = checkAndGetIVec2D(filepath, animationInfoNode, "frameSize");
                IVec2D startingPosition = checkAndGetIVec2D(filepath, animationInfoNode, "startingPosition");
                IVec2D stride = checkAndGetIVec2D(filepath, animationInfoNode, "stride");

                float framePeriodSeconds = checkAndGetFloat(filepath, spriteNode, "framePeriodSeconds");
                boolean looping = checkAndGetBoolean(filepath, spriteNode, "looping");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSize.x, frameSize.y, startingPosition.x, startingPosition.y, stride.x, stride.y);

                customEntitybuilder = customEntitybuilder.createSprite(layer, animationInfo, framePeriodSeconds, looping, orientable);
            }
            else{
                String texturePath = GlobalVars.Paths.editorTextureFolder + checkAndGetString(filepath, spriteNode, "fileName");


                customEntitybuilder = customEntitybuilder.createSprite(layer, texturePath, orientable);
            }
            if(id == 0){
                customEntitybuilder = customEntitybuilder.setTrajectory(new PlayerControlledTrajectory(GlobalVars.playerSpeed));
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
                customEntitybuilder = customEntitybuilder.setTrajectory(trajectory);
            }
            editorDataManager.addCustomEntity(id, customEntitybuilder.build());
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
                    trajectoryFunctionX = convertToFunction(functionXString);
                    trajectoryFunctionY = convertToFunction(functionYString);
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

            checkForField(filepath, childNode, "spawn");
            JsonNode spawnableNode = childNode.get("spawn");
            Spawnable newSpawnable;
            if(spawnableNode.isArray()){
                ArrayList<Spawnable> spawnables = new ArrayList<>();
                for(var spawnElement: spawnableNode){
                    checkIfObject(filepath, spawnElement);
                    spawnables.add(getSingleSpawnable(filepath, spawnElement));
                }
                newSpawnable = new MultiSpawnable(spawnables);
            }
            else{
                checkIfObject(filepath, spawnableNode);
                newSpawnable = getSingleSpawnable(filepath, spawnableNode);
            }
            String type = checkAndGetString(filepath, childNode, "type");
            if(type.equals("single")){
                float time = checkAndGetFloat(filepath, childNode, "time");
                newTimeline.addSpawnable(time, newSpawnable);
            } else if (type.equals("interval")) {
                float startTime = checkAndGetFloat(filepath, childNode, "startTime");
                float endTime = checkAndGetFloat(filepath, childNode, "endTime");
                float interval = checkAndGetFloat(filepath, childNode, "interval");
                for(float i = startTime; i <= endTime; i+=interval){
                    newTimeline.addSpawnable(i, newSpawnable);
                }
            }else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }

        }
        editorDataManager.addTimeline(newTimeline);
    }
    private Spawnable getSingleSpawnable(String filepath, JsonNode spawnableNode){
        String type = checkAndGetString(filepath, spawnableNode, "type");
        if(type.equals("entity")){
            int id = checkAndGetInt(filepath, spawnableNode, "id");

            Vec2D startingPositionVec = checkAndConvertIntArrayToVec2D(filepath, spawnableNode, "startingPosition");

            EntitySpawnInfo spawnInfo;
            if(spawnableNode.has("trajectory")){
                int trajectoryId = checkAndGetInt(filepath, spawnableNode, "trajectory");
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
            }else{
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, -1);
            }
            return spawnInfo;

        }else if(type.equals("display")) {
            int id = checkAndGetInt(filepath, spawnableNode, "id");
            Vec2D positionVec = checkAndConvertIntArrayToVec2D(filepath, spawnableNode, "position");
            return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
        }
        else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }
    private Function<Float, Float> convertToFunction(String expr) throws LambdaCreationException {
        if(expr.contains("{") || expr.contains("Systems") || expr.contains("Threads")){
            throw new IllegalArgumentException("Illegal character in trajectory function");
        }
        LambdaFactory lambdaFactory = LambdaFactory.get(
                LambdaFactoryConfiguration.get().withImports("static engine.entity.trajectory.TrajectoryFunctionUtils.MathFloatOverloads.*; import static engine.entity.trajectory.TrajectoryFunctionUtils.*")
        );
        return lambdaFactory.createLambda(
                "t -> (float)(" + expr + ")", new TypeReference<Function<Float, Float>>(){});
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

    private IVec2D checkAndGetIVec2D(String filepath, JsonNode parentNode, String fieldName){
        JsonNode arrayNode = checkAndGetArray(filepath, parentNode, fieldName);
        checkSize(filepath, arrayNode, 2);
        checkIfInt(filepath, arrayNode.get(0));
        checkIfInt(filepath, arrayNode.get(1));
        int vecX = arrayNode.get(0).intValue();
        int vecY = arrayNode.get(1).intValue();
        return new IVec2D(vecX, vecY);
    }

    private Vec2D checkAndConvertIntArrayToVec2D(String filepath, JsonNode parentNode, String fieldName){
        JsonNode arrayNode = checkAndGetArray(filepath, parentNode, fieldName);
        checkSize(filepath, arrayNode, 2);
        checkIfInt(filepath, arrayNode.get(0));
        checkIfInt(filepath, arrayNode.get(1));
        int vecX = arrayNode.get(0).intValue();
        int vecY = arrayNode.get(1).intValue();
        return new Vec2D((float) vecX / GameConfig.getEditionWidth(), (float) vecY / GameConfig.getEditionHeight());
    }
}
