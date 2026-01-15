package engine.scene.visual.style;

import engine.types.RGBAValue;

import java.nio.file.Path;

public record TextStyle(
    Path fontFilepath,
    RGBAValue textColor,
    float textHeight
) {
}
