package editor.editionData;

import editor.attribute.Vec2DAttribute;
import json.JsonFieldNames;
import lombok.Getter;

@Getter final public class SimpleRectangleHitboxEditionData implements EditionData, HitboxEditionData {

    Vec2DAttribute size;

    public SimpleRectangleHitboxEditionData(float sizeX, float sizeY) {
        this.size = new Vec2DAttribute("Size (pixels)", JsonFieldNames.SimpleRectangleHitbox.size, sizeX, sizeY);
    }
}
