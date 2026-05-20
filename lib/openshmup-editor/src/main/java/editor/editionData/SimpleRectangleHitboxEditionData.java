package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class SimpleRectangleHitboxEditionData implements HitboxEditionData {

    Vec2DAttribute size;

    public SimpleRectangleHitboxEditionData(Vec2D size) {
        this.size = new Vec2DAttribute("Size (pixels)", JsonFieldNames.SimpleRectangleHitbox.size, size);
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(size);
    }
}
