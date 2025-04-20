package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.*;
import engine.entity.Entity;
import engine.entity.EntityType;
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
import engine.types.IVec2D;
import engine.types.Vec2D;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EditorDataLoader {
    final private ObjectMapper objectMapper;
    public EditorDataLoader(){
        this.objectMapper = new ObjectMapper();
    }

    public void loadGameParameters(String filepath) throws FileNotFoundException, IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(filepath, objectMapper);

        IVec2D resolution = rootNode.checkAndGetIVec2D("resolution");
        GameConfig.setEditionResolution(resolution.x, resolution.y);

        SafeJsonNode levelUINode = rootNode.checkAndGetObject("levelUI");

        SafeJsonNode livesNode = levelUINode.checkAndGetObject( "lives");

        GameConfig.LevelUI.Lives.textureFilepath = GlobalVars.Paths.editorTextureFolder + livesNode.checkAndGetString( "fileName");
        GameConfig.LevelUI.Lives.size = convertToFloatVec(livesNode.checkAndGetIVec2D( "size"));
        GameConfig.LevelUI.Lives.position = convertToFloatVec(livesNode.checkAndGetIVec2D( "position"));
        GameConfig.LevelUI.Lives.stride = convertToFloatVec(livesNode.checkAndGetIVec2D( "stride"));
    }
    public void loadCustomDisplays(String filepath, EditorDataManager editorDataManager) throws IllegalArgumentException, FileNotFoundException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(filepath, objectMapper);
        List<SafeJsonNode> displayList = rootNode.checkAndGetObjectsFromArray();
        for(SafeJsonNode displayNode: displayList){
            int id = displayNode.checkAndGetInt("id");
            int layer = displayNode.checkAndGetInt("layer");
            String type = displayNode.checkAndGetString("type");
            Vec2D size = convertToFloatVec( displayNode.checkAndGetIVec2D( "size"));

            if(type.equals("scrollingImage")){

                String imagePath = GlobalVars.Paths.editorTextureFolder + displayNode.checkAndGetString("fileName");
                boolean horizontalScrolling = displayNode.checkAndGetBoolean("horizontalScrolling");

                int speed = displayNode.checkAndGetInt("speed");
                float normalizedSpeed;
                if(horizontalScrolling){
                    normalizedSpeed = (float) speed / GameConfig.getEditionWidth();
                }else{
                    normalizedSpeed = (float) speed / GameConfig.getEditionHeight();
                }

                editorDataManager.addCustomDisplays(id, new ScrollingImage(imagePath, layer, size.x, size.y, normalizedSpeed, horizontalScrolling));
            }
            else if(type.equals("animation")) {
                SafeJsonNode animationInfoNode = displayNode.checkAndGetObject("animationInfo");

                String animationFilepath = GlobalVars.Paths.editorTextureFolder + animationInfoNode.checkAndGetString("fileName");
                int frameCount = animationInfoNode.checkAndGetInt("frameCount");
                IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
                IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
                IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

                float framePeriodSeconds = displayNode.checkAndGetFloat("framePeriodSeconds");
                boolean looping = displayNode.checkAndGetBoolean("looping");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSize.x, frameSize.y, startingPosition.x, startingPosition.y, stride.x, stride.y);
                editorDataManager.addCustomDisplays(id, new Animation(layer, animationInfo, framePeriodSeconds, looping, size.x, size.y));
            }
            else{
                throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
            }
        }
    }
    public void loadCustomEntities(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(filepath, objectMapper);
        List<SafeJsonNode> customEntities = rootNode.checkAndGetObjectsFromArray();
        for(SafeJsonNode entityNode: customEntities){

            int id = entityNode.checkAndGetInt("id");
            EntityType type = EntityType.fromString(entityNode.checkAndGetString("type"));
            boolean evil = entityNode.checkAndGetBoolean("evil");

            Vec2D size = convertToFloatVec(entityNode.checkAndGetIVec2D("size"));

            Entity.Builder customEntityBuilder = new Entity.Builder().setId(id).setType(type).setSize(size.x, size.y).setEvil(evil);
            if(entityNode.hasField("hitbox")){
                SafeJsonNode hitboxNode = entityNode.checkAndGetObject("hitbox");
                String hitboxType = hitboxNode.checkAndGetString("type");
                if(hitboxType.equals("composite")){
                    String hitboxFileName = hitboxNode.checkAndGetString("fileName");
                    customEntityBuilder = customEntityBuilder.addCompositeHitbox(GlobalVars.Paths.editorTextureFolder+ hitboxFileName, false);
                }
                if(hitboxType.equals("simpleRectangle")){
                    customEntityBuilder = customEntityBuilder.addRectangleHitbox(false);
                }
            }
            if(entityNode.hasField("deathSpawn")){
                SafeJsonNode deathSpawnNode = entityNode.checkAndGetObjectOrArray("deathSpawn");
                Spawnable deathSpawn;
                if(deathSpawnNode.isArray()){
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    List<SafeJsonNode> elementsList = deathSpawnNode.checkAndGetObjectsFromArray();
                    for(var deathSpawnElement: elementsList){
                        spawnables.add(getSingleSpawnable(deathSpawnElement));
                    }
                    deathSpawn = new MultiSpawnable(spawnables);
                }
                else{
                    deathSpawn = getSingleSpawnable(deathSpawnNode);
                }
                customEntityBuilder = customEntityBuilder.setDeathSpawn(deathSpawn);
            }

            if(type == EntityType.SHIP && entityNode.hasField("hp")){
                int hp = entityNode.checkAndGetInt("hp");
                customEntityBuilder = customEntityBuilder.setHitPoints(hp);
            }

            if (entityNode.hasField("shot")){
                SafeJsonNode shotNode = entityNode.checkAndGetObject("shot");

                float shotPeriod = shotNode.checkAndGetFloat("shotPeriod");
                float firstShotTime = shotNode.checkAndGetFloat("firstShotTime");

                SafeJsonNode spawnableNode = shotNode.checkAndGetObjectOrArray("spawn");
                Spawnable shot;
                if(spawnableNode.isArray()){
                    ArrayList<Spawnable> spawnables = new ArrayList<>();
                    List<SafeJsonNode> elementsList = spawnableNode.checkAndGetObjectsFromArray();
                    for(var spawnElement: elementsList){
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

            if(spriteNode.hasField("animationInfo")){
                SafeJsonNode animationInfoNode = spriteNode.checkAndGetObject("animationInfo");

                String animationFilepath = GlobalVars.Paths.editorTextureFolder + animationInfoNode.checkAndGetString("fileName");
                int frameCount = animationInfoNode.checkAndGetInt("frameCount");
                IVec2D frameSize = animationInfoNode.checkAndGetIVec2D("frameSize");
                IVec2D startingPosition = animationInfoNode.checkAndGetIVec2D("startingPosition");
                IVec2D stride = animationInfoNode.checkAndGetIVec2D("stride");

                float framePeriodSeconds = spriteNode.checkAndGetFloat("framePeriodSeconds");
                boolean looping = spriteNode.checkAndGetBoolean("looping");

                AnimationInfo animationInfo = new AnimationInfo(animationFilepath, frameCount, frameSize.x, frameSize.y, startingPosition.x, startingPosition.y, stride.x, stride.y);

                customEntityBuilder = customEntityBuilder.createSprite(layer, animationInfo, framePeriodSeconds, looping, orientable);
            }
            else{
                String texturePath = GlobalVars.Paths.editorTextureFolder + spriteNode.checkAndGetString("fileName");


                customEntityBuilder = customEntityBuilder.createSprite(layer, texturePath, orientable);
            }
            if(id == 0){
                customEntityBuilder = customEntityBuilder.setTrajectory(new PlayerControlledTrajectory(GlobalVars.playerSpeed));
            }
            if(entityNode.hasField("defaultTrajectory")){
                SafeJsonNode trajectoryNode = entityNode.checkAndGetObject("defaultTrajectory");
                Trajectory trajectory;
                if(trajectoryNode.hasField("id")){
                    int trajectoryId = trajectoryNode.checkAndGetInt("id");
                    trajectory = editorDataManager.getTrajectory(trajectoryId);
                }
                else{
                    String trajectoryType = trajectoryNode.checkAndGetString("type");
                    if(trajectoryType.equals("fixed")){
                        String functionXString = trajectoryNode.checkAndGetString("functionX");
                        String functionYString = trajectoryNode.checkAndGetString("functionY");
                        if(trajectoryNode.hasField("relative")){
                            boolean relative = trajectoryNode.checkAndGetBoolean("relative");
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
                        throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
                    }
                }
                customEntityBuilder = customEntityBuilder.setTrajectory(trajectory);
            }
            editorDataManager.addCustomEntity(id, customEntityBuilder.build());
        }
    }
    public void loadCustomTrajectories(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getArrayRootNode(filepath, objectMapper);
        List<SafeJsonNode> elementList = rootNode.checkAndGetObjectsFromArray();
        for(SafeJsonNode trajectoryNode: elementList){
            int id = trajectoryNode.checkAndGetInt("id");
            String type = trajectoryNode.checkAndGetString("type");

            Trajectory newTrajectory;
            if(type.equals( "fixed")) {
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
            else{
                throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
            }
            editorDataManager.addTrajectory(id, newTrajectory);
        }
    }
    public void loadCustomTimeline(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException, IllegalArgumentException {
        SafeJsonNode rootNode = SafeJsonNode.getObjectRootNode(filepath, objectMapper);
        float duration = rootNode.checkAndGetFloat("duration");
        SafeJsonNode spawnsNode = rootNode.checkAndGetObjectArray("spawns");
        LevelTimeline newTimeline = new LevelTimeline(editorDataManager, duration);
        List<SafeJsonNode> elementList = spawnsNode.checkAndGetObjectsFromArray();
        for(SafeJsonNode childNode: elementList){
            SafeJsonNode spawnableNode = childNode.checkAndGetObjectOrArray("spawn");
            Spawnable newSpawnable;
            if(spawnableNode.isArray()){
                ArrayList<Spawnable> spawnables = new ArrayList<>();
                List<SafeJsonNode> nodeList = spawnableNode.checkAndGetObjectsFromArray();
                for(var spawnElement: nodeList){
                    spawnables.add(getSingleSpawnable(spawnElement));
                }
                newSpawnable = new MultiSpawnable(spawnables);
            }
            else{
                newSpawnable = getSingleSpawnable(spawnableNode);
            }
            String type = childNode.checkAndGetString("type");
            if(type.equals("single")){
                float time = childNode.checkAndGetFloat("time");
                newTimeline.addSpawnable(time, newSpawnable);
            } else if (type.equals("interval")) {
                float startTime = childNode.checkAndGetFloat("startTime");
                float endTime = childNode.checkAndGetFloat("endTime");
                float interval = childNode.checkAndGetFloat("interval");
                for(float i = startTime; i <= endTime; i+=interval){
                    newTimeline.addSpawnable(i, newSpawnable);
                }
            }else{
                throw new IllegalArgumentException("Invalid JSON format: \"" + filepath + "\"");
            }

        }
        editorDataManager.addTimeline(newTimeline);
    }
    private Spawnable getSingleSpawnable(SafeJsonNode spawnableNode){
        String type = spawnableNode.checkAndGetString("type");
        if(type.equals("entity")){
            int id = spawnableNode.checkAndGetInt("id");

            Vec2D startingPositionVec = convertToFloatVec(spawnableNode.checkAndGetIVec2D("startingPosition"));
            EntitySpawnInfo spawnInfo;
            if(spawnableNode.hasField("trajectory")){
                int trajectoryId = spawnableNode.checkAndGetInt("trajectory");
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, trajectoryId);
            }else{
                spawnInfo = new EntitySpawnInfo(id, startingPositionVec.x, startingPositionVec.y, -1);
            }
            return spawnInfo;

        }else if(type.equals("display")) {
            int id = spawnableNode.checkAndGetInt("id");
            Vec2D positionVec = convertToFloatVec(spawnableNode.checkAndGetIVec2D("position"));
            return new SceneDisplaySpawnInfo(id, positionVec.x, positionVec.y);
        }
        else{
            throw new IllegalArgumentException("Invalid JSON format: " + spawnableNode.getPath() + ": spwnable type can only be \"display\" or \"entity\"");
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

    private Vec2D convertToFloatVec(IVec2D pixelVec){
        return new Vec2D((float) pixelVec.x / GameConfig.getEditionWidth(), (float) pixelVec.y / GameConfig.getEditionHeight());
    }
}
