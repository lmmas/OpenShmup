package engine.graphics;

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

}
