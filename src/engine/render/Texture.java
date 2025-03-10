package engine.render;

import org.lwjgl.*;

import java.nio.*;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

import java.util.HashMap;

public class Texture {
    private final static HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
    private final String filepath;
    private final int textureID;

    private final int width;

    private final int height;

    private Texture( String filepath){
        this.filepath = filepath;

        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        this.width = width.get(0);
        this.height = height.get(0);
        assert image != null: "Could not load texture file '" + filepath + "'";
        /*
        System.out.println("channels: " + channels.get(0));
        System.out.println("width: " + width.get(0));
        System.out.println("height: " + height.get(0));
        */
        assert channels.get(0) == 3 || channels.get(0) == 4: "Invalid number of channels \"" + channels.get(0) + "\"";
        if (channels.get(0) == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height,
                    0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else if (channels.get(0) == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }
        stbi_image_free(image);

    }

    public static Texture loadTexture(String filepath){
        if(textureMap.containsKey(filepath)){
            return textureMap.get(filepath);
        }
        else{
            Texture newTexture = new Texture(filepath);
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