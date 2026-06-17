package engine.assets;

import engine.types.IVec2D;
import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

final public class Texture {

    private Integer textureID;
    @Getter
    final private IVec2D resolution;
    @Getter
    private final int channelCount;
    @Getter
    private ByteBuffer imageBuffer;
    @Getter
    private boolean loadedInGPU;

    Texture(IVec2D resolution, int channelCount, ByteBuffer imageBuffer) {
        this.textureID = null;
        this.resolution = resolution;
        this.channelCount = channelCount;
        this.imageBuffer = imageBuffer;
        this.loadedInGPU = false;
    }

    public static Texture createFromImageFile(Path filepath) throws IOException {
        int[] widthArray = new int[1];
        int[] heightArray = new int[1];
        int[] channelCountArray = new int[1];
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer imageBuffer = stbi_load(filepath.toString(), widthArray, heightArray, channelCountArray, 0);
        if (imageBuffer == null)
            throw new IOException("Could not load texture file \"" + filepath + "\"" + stbi_failure_reason());
        //assert channelCountArray[0] == 3 || channelCountArray[0] == 4: "Invalid number of channels :" + channelCountArray[0];
        return new Texture(new IVec2D(widthArray[0], heightArray[0]), channelCountArray[0], imageBuffer);
    }

    public void loadInGPU() {
        assert !loadedInGPU : "texture already loaded in GPU";
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        if (channelCount == 1) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_R, GL_RED);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_G, GL_RED);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_B, GL_RED);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_SWIZZLE_A, GL_RED);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_R8, this.resolution.x, this.resolution.y,
                0, GL_RED, GL_UNSIGNED_BYTE, imageBuffer);
        }
        else if (channelCount == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, this.resolution.x, this.resolution.y,
                0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
        }
        else if (channelCount == 4) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.resolution.x, this.resolution.y,
                0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
        }
        stbi_image_free(imageBuffer);
        loadedInGPU = true;
    }

    public void flipImageBuffer() {
        assert imageBuffer != null : "trying to flip image buffer while it is null";
        ByteBuffer flippedBuffer = BufferUtils.createByteBuffer(imageBuffer.capacity());
        for (int y = 0; y < resolution.y; y++) {
            for (int x = 0; x < resolution.x; x++) {
                byte b = imageBuffer.get((resolution.y - 1 - y) * resolution.x + x);
                flippedBuffer.put(y * resolution.x + x, b);
            }
        }
        this.imageBuffer = flippedBuffer;
    }

    public void bind(int slot) {
        if (!loadedInGPU) {
            loadInGPU(); //FIXME: quick hack to mitigate the bug when textures aren't properly loaded in GPU
        }
        assert loadedInGPU : "texture has not been loaded in GPU before binding";
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}