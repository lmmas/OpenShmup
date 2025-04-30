package engine.assets;

import engine.types.Vec2D;

public record FontCharInfo(
        Vec2D normalizedQuadSize,
        float normalizedAdvanceWidth,
        Vec2D bitMapTextureSize,
        Vec2D bitMapTexturePosition
        ) {
}
