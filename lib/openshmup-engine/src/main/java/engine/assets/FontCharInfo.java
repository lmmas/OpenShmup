package engine.assets;

import types.Vec2D;

public record FontCharInfo(
    int codepoint,
    Vec2D normalizedQuadSize,
    Vec2D normalizedQuadPositionOffset,
    float normalizedAdvance,
    Vec2D bitmapTextureSize,
    Vec2D bitmapTexturePosition
) {
}
