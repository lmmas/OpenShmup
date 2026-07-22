package engine.scene.visual.style;

import types.RGBAValue;

public record RoundedRectangleStyle(
    float roundingRadius,
    float borderWidth,
    RGBAValue rectangleColor,
    RGBAValue borderColor
) {}
