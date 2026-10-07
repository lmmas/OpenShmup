package engine.visual.style;

import types.RGBAValue;

import java.nio.file.Path;

public record TextStyle(
    Path fontFilepath,
    RGBAValue textColor,
    float textHeight
) {
}
