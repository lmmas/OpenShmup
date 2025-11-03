package engine;

import engine.assets.Font;
import engine.assets.Shader;
import engine.assets.Texture;

import java.io.IOException;
import java.util.HashMap;

import static engine.GlobalVars.Paths.debugFont;
import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class AssetManager {
    final private HashMap<String, Shader> shaderMap;
    final private HashMap<String, Texture> imageFileMap;
    final private HashMap<String, Font> fontFileMap;
    public AssetManager() throws IOException {
        this.shaderMap = new HashMap<>();
        this.imageFileMap = new HashMap<>();
        this.fontFileMap = new HashMap<>();

        Texture placeholderTexture = Texture.createFromImageFile(rootFolderAbsolutePath + GlobalVars.Paths.placeholderTextureFile);
        imageFileMap.put(GlobalVars.Paths.placeholderTextureFile, placeholderTexture);

        Font defaultFont = Font.createFromTTF(rootFolderAbsolutePath + debugFont);
        fontFileMap.put(debugFont, defaultFont);
        defaultFont.getBitmap().loadInGPU();
        imageFileMap.put(debugFont, defaultFont.getBitmap());
    }

    public Texture getTexture(String filepath){
        if(imageFileMap.containsKey(filepath)){
            return imageFileMap.get(filepath);
        }
        else{
            Texture newTexture;
            try {
                newTexture = Texture.createFromImageFile(rootFolderAbsolutePath + filepath);
            } catch (IOException e) {
                newTexture = imageFileMap.get(rootFolderAbsolutePath + GlobalVars.Paths.placeholderTextureFile);
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
            Shader newShader = new Shader(rootFolderAbsolutePath + filepath);
            shaderMap.put(filepath, newShader);
            newShader.compile();
            return newShader;
        }
    }

    public Font getFont(String filepath){
        if(fontFileMap.containsKey(filepath)){
            return fontFileMap.get(filepath);
        }
        else{
            try {
                Font newFont = Font.createFromTTF(rootFolderAbsolutePath + filepath);
                fontFileMap.put(filepath, newFont);
                return newFont;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
