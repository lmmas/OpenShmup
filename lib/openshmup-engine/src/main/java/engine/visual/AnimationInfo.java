package engine.visual;

import java.nio.file.Path;

public record AnimationInfo(
    Path filepath,
    int frameCount,
    float frameSizeX,
    float frameSizeY,
    float startPosX,
    float startPosY,
    float strideX,
    float strideY
) {

}
