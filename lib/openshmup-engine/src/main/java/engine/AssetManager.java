package engine;

import engine.assets.Shader;
import engine.assets.Texture;
import engine.entity.extraComponent.HitboxDebugDisplay;
import engine.graphics.Image2D;

import java.io.IOException;
import java.util.HashMap;

public class AssetManager {
    final private HashMap<String, Shader> shaderMap;
    final private HashMap<String, Texture> imageFileMap;
    public AssetManager() throws IOException {
        this.shaderMap = new HashMap<>();
        this.imageFileMap = new HashMap<>();
        Image2D.setDefaultShader(getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl"));
        HitboxDebugDisplay.setHitboxShader(getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl"));
        Texture placeholderTexture = Texture.createFromImageFile(GlobalVars.Paths.placeholderTextureFile);
        imageFileMap.put(GlobalVars.Paths.placeholderTextureFile, placeholderTexture);
    }

    public Texture getTexture(String filepath){
        if(imageFileMap.containsKey(filepath)){
            return imageFileMap.get(filepath);
        }
        else{
            Texture newTexture;
            try {
                newTexture = Texture.createFromImageFile(filepath);
            } catch (IOException e) {
                newTexture = imageFileMap.get(GlobalVars.Paths.placeholderTextureFile);
            }
            imageFileMap.put(filepath, newTexture);
            return newTexture;
        }
    }

    public Shader getShader(String filepath){
        if (shaderMap.containsKey(filepath)){
            return shaderMap.get(filepath);
        }
        else{
            Shader newShader = new Shader(filepath);
            shaderMap.put(filepath, newShader);
            newShader.compile();
            return newShader;
        }
    }
}
