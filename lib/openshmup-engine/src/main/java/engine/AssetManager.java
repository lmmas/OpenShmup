package engine;

import engine.assets.Shader;
import engine.assets.Texture;
import engine.entity.extraComponent.HitboxDebugDisplay;
import engine.graphics.Image2D;

import java.io.IOException;
import java.util.HashMap;

public class AssetManager {
    final private HashMap<String, Shader> shaderMap;
    final private HashMap<String, Texture> textureMap;
    public AssetManager() throws IOException {
        this.shaderMap = new HashMap<>();
        this.textureMap = new HashMap<>();
        Image2D.setDefaultShader(getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl"));
        HitboxDebugDisplay.setHitboxShader(getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl"));
        Texture placeholderTexture = Texture.createFromImageFile(GlobalVars.Paths.placeholderTextureFile);
        textureMap.put(GlobalVars.Paths.placeholderTextureFile, placeholderTexture);
    }

    public Texture getTexture(String filepath){
        if(textureMap.containsKey(filepath)){
            return textureMap.get(filepath);
        }
        else{
            Texture newTexture;
            try {
                newTexture = Texture.createFromImageFile(filepath);
            } catch (IOException e) {
                newTexture = textureMap.get(GlobalVars.Paths.placeholderTextureFile);
            }
            textureMap.put(filepath, newTexture);
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
