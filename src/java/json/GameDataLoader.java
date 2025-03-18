package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.GlobalVars;
import engine.EditorDataManager;
import engine.entity.Entity;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class GameDataLoader {
    final private ObjectMapper objectMapper;
    public GameDataLoader(){
        this.objectMapper = new ObjectMapper();
    }

    public void loadCustomEntities(String filepath, EditorDataManager editorDataManager) throws FileNotFoundException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new FileNotFoundException("custom entities file not found: filepath '" + filepath + "'");
        }
        checkIfArray(filepath, rootNode);

        for(JsonNode customEntityNode: rootNode){
            checkForField(filepath, customEntityNode, "id");
            checkForField(filepath, customEntityNode, "size");
            checkForField(filepath, customEntityNode, "sprite");

            AtomicReference<Function<LevelScene, Entity.Builder>> customEntityBuilder = new AtomicReference<>(levelScene -> new Entity.Builder().setScene(levelScene));

            checkIfInt(filepath, customEntityNode.get("id"));
            int id = customEntityNode.get("id").intValue();
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setId(id)));
            JsonNode sizeNode = customEntityNode.get("size");
            checkIfArray(filepath, sizeNode);
            checkSize(filepath, sizeNode, 2);
            checkIfFloat(filepath, sizeNode.get(0));
            float sizeX = sizeNode.get(0).floatValue();
            float sizeY = sizeNode.get(1).floatValue();
            customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.setSize(sizeX, sizeY)));

            JsonNode spriteNode = customEntityNode.get("sprite");
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
                String animationFilepath = GlobalVars.Paths.EditorTextureFolder + animationInfoNode.get("fileName").textValue();

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
                String texturePath = GlobalVars.Paths.EditorTextureFolder + spriteNode.get("fileName").textValue();

                customEntityBuilder.set(customEntityBuilder.get().andThen(builder -> builder.createSprite(layer, texturePath, orientable)));
            }
            if(customEntityNode.has("trajectory")){

            }
            Function<LevelScene, Entity> customEntityConstructor = customEntityBuilder.get().andThen(Entity.Builder::build);
            editorDataManager.addCustomEntity(id, customEntityConstructor);
        }
    }

    private void checkForField(String filepath, JsonNode node, String field){
        if(!node.has(field)){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfArray(String filepath, JsonNode node){
        if(!node.isArray()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkSize(String filepath, JsonNode node, int size){
        if(node.size() != size){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfObject(String filepath, JsonNode node){
        if(!node.isObject()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfBoolean(String filepath, JsonNode node){
        if(!node.isBoolean()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfInt(String filepath, JsonNode node){
        if(!node.isInt()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfString(String filepath, JsonNode node){
        if(!node.isTextual()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }

    private void checkIfFloat(String filepath, JsonNode node){
        if(!node.isNumber()){
            throw new IllegalArgumentException("Invalid JSON format: '" + filepath + "'");
        }
    }
}
