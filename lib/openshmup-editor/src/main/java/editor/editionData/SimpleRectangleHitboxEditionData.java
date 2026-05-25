package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class SimpleRectangleHitboxEditionData implements HitboxEditionData {

    final private Vec2DAttribute size;

    private SimpleRectangleHitboxEditionData() {
        this.size = new Vec2DAttribute("Size (pixels)", JsonFieldNames.SimpleRectangleHitbox.size);
    }

    public SimpleRectangleHitboxEditionData(Vec2D size) {
        this();
        this.size.setValue(size);
    }

    @Override
    public List<Attribute> getAttributes() {
        return List.of(size);
    }

    @Override
    public void setToDefault() {
        this.size.setValue(Vec2D.ZERO);
    }

    public static SimpleRectangleHitboxEditionData DEFAULT() {
        SimpleRectangleHitboxEditionData data = new SimpleRectangleHitboxEditionData();
        data.setToDefault();
        return data;
    }
}
