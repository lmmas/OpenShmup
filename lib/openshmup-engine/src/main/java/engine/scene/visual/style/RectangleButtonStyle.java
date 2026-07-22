package engine.scene.visual.style;

import types.RGBAValue;

public record RectangleButtonStyle(
    float roundingRadius,
    float borderWidth,
    RGBAValue rectangleColor,
    RGBAValue borderColor,
    TextStyle textStyle
) {}
