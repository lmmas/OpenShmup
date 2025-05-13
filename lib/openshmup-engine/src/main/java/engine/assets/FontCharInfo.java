package engine.assets;

import engine.types.Vec2D;

public record FontCharInfo(
        int codepoint,
        Vec2D normalizedQuadSize,
        Vec2D normalizedQuadPositionOffset,
        float normalizedAdvance,
        Vec2D bitmapTextureSize,
        Vec2D bitmapTexturePosition
        ) {
}
