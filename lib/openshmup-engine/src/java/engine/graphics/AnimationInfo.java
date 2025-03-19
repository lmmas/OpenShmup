package engine.graphics;

import engine.render.Texture;

public record AnimationInfo(
        String filepath,
        int frameCount,
        float frameSizeX,
        float frameSizeY,
        float startPosX,
        float startPosY,
        float strideX,
        float strideY
) {
    public AnimationInfo(String filepath, int frameCount, int frameSizeXPixels, int frameSizeYPixels, int startPosXPixel, int startPosYPixel, int strideXPixel, int strideYPixel){
        this(
                filepath,
                frameCount,
                (float)frameSizeXPixels / (float)Texture.getTexture(filepath).getWidth(),
                (float)frameSizeYPixels / (float)Texture.getTexture(filepath).getHeight(),
                (float)startPosXPixel / (float)Texture.getTexture(filepath).getWidth(),
                (float)startPosYPixel / (float)Texture.getTexture(filepath).getHeight(),
                (float)strideXPixel / (float)Texture.getTexture(filepath).getWidth(),
                (float)strideYPixel / (float)Texture.getTexture(filepath).getHeight());
    }
}
