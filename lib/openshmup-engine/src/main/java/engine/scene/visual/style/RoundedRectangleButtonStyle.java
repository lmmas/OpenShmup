package engine.scene.visual.style;

import types.RGBAValue;

public record RoundedRectangleButtonStyle(
    float roundingRadius,
    float borderWidth,
    RGBAValue rectangleColor,
    RGBAValue borderColor,
    TextStyle textStyle
) {}
