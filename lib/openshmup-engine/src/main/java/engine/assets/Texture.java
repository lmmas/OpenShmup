package engine.assets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.*;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;

public class Texture {
    private Integer textureID;
    private final int width;
    private final int height;
    private final int channelCount;
    private final ByteBuffer imageBuffer;
    private boolean loadedInGPU;

    Texture(int width, int height, int channelCount, ByteBuffer imageBuffer) {
        this.textureID = null;
        this.width = width;
        this.height = height;
        this.channelCount = channelCount;
        this.imageBuffer = imageBuffer;
        this.loadedInGPU = false;
    }

    public static Texture createFromImageFile(String filepath) throws IOException {
        int[] widthArray = new int[1];
        int[] heightArray = new int[1];
        int[] channelCountArray = new int[1];
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer imageBuffer = stbi_load(filepath, widthArray, heightArray, channelCountArray, 0);
        if (imageBuffer == null)
            throw new IOException("Could not load texture file \"" + filepath + "\"" + stbi_failure_reason());
        assert channelCountArray[0] == 3 || channelCountArray[0] == 4: "Invalid number of channels :" + channelCountArray[0];
        boolean success = stbi_info(filepath, widthArray, heightArray, channelCountArray);
        if (success) {
            return new Texture(widthArray[0], heightArray[0], channelCountArray[0], imageBuffer);
        } else {
            System.err.println(stbi_failure_reason());
            throw new FileNotFoundException(filepath);
        }
    }
    public void loadInGPU(){
        assert !loadedInGPU: "texture already loaded in GPU";
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        if (channelCount == 1) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_R, this.width, this.height,
                    0, GL_R, GL_UNSIGNED_BYTE, imageBuffer);
        } else if (channelCount == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.width, this.height,
                    0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
        } else if (channelCount == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height,
                    0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
        }
        stbi_image_free(imageBuffer);
        loadedInGPU = true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public ByteBuffer getImageBuffer() {
        return imageBuffer;
    }

    public boolean isLoadedInGPU() {
        return loadedInGPU;
    }

    public void bind(int slot){
        assert loadedInGPU: "texture has not been loaded in GPU before binding";
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}