package engine.scene.visual;

import engine.types.Vec2D;

import java.nio.file.Path;

public record SpritesheetInfo(
    Path filepath,
    int frameCount,
    Vec2D frameSize,
    Vec2D startPos,
    Vec2D stride
) {

}
