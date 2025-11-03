package engine.scene.visual.style;

import engine.assets.Font;
import engine.types.RGBAValue;

public record TextStyle(
        Font font,
        RGBAValue textColor,
        float textHeight
) {
}
