package engine;

import engine.assets.Shader;
import engine.assets.Texture;
import engine.entity.extraComponent.HitboxDebugDisplay;
import engine.graphics.Image2D;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class AssetManager {
    final private HashMap<String, Shader> shaderMap;
    final private HashMap<String, Texture> textureMap;
    public AssetManager(){
        this.shaderMap = new HashMap<>();
        this.textureMap = new HashMap<>();
        Image2D.setDefaultShader(loadShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl"));
        HitboxDebugDisplay.setHitboxShader(loadShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/debugRectangle.glsl"));
    }

    public Texture getTexture(String filepath){
        if(textureMap.containsKey(filepath)){
            return textureMap.get(filepath);
        }
        else{
            Texture newTexture;
            try {
                newTexture = new Texture(filepath);
            } catch (FileNotFoundException e) {
                try {
                    newTexture = new Texture(GlobalVars.Paths.missingTextureFile);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            textureMap.put(filepath, newTexture);
            return newTexture;
        }
    }

    public Shader loadShader(String filepath){
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
