package engine.scene.visual.style;

import engine.types.RGBAValue;

public record TextStyle(
        String fontFilepath,
        RGBAValue textColor,
        float textHeight
) {
}
