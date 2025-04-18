package engine.render;

import engine.GlobalVars;
import org.lwjgl.*;

import java.io.FileNotFoundException;
import java.nio.*;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

import java.util.HashMap;

public class Texture {
    private final static HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
    private final String filepath;
    private int textureID;
    private final int width;
    private final int height;

    private Texture( String filepath) throws FileNotFoundException {

        int[] widthArray = new int[1];
        int[] heightArray = new int[1];
        int[] channelNumber = new int[1];
        boolean success = stbi_info(filepath, widthArray, heightArray, channelNumber);
        if (success) {
            this.width = widthArray[0];
            this.height = heightArray[0];
            this.filepath = filepath;
        } else {
            System.out.println(stbi_failure_reason());
            throw new FileNotFoundException(filepath);
        }
    }
    public void loadInGPU(){
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        assert image != null: "Could not load texture file '" + filepath + "'";
        assert channels.get(0) == 3 || channels.get(0) == 4: "Invalid number of channels :" + channels.get(0);
        if (channels.get(0) == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height,
                    0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else if (channels.get(0) == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }
        stbi_image_free(image);
    }

    public static Texture getTexture(String filepath){
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
    public String getFilepath() {
        return filepath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind(int slot){
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}