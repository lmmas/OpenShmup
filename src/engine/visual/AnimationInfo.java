package engine.visual;

public record AnimationInfo(
        String filepath,
        int frameCount,
        int frameSizeX,
        int frameSizeY,
        int startPosX,
        int startPosY,
        int strideX,
        int strideY
) {

}
