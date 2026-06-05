package json.editionData;

import engine.types.Vec2D;
import json.attribute.Attribute;
import json.attribute.Vec2DAttribute;
import lombok.Getter;

import java.util.List;

@Getter final public class RectangleHitboxEditionData implements HitboxEditionData {

    final private Vec2DAttribute size;

    private RectangleHitboxEditionData() {
        this.size = new Vec2DAttribute(Keys.Hitbox.RectangleHitbox.size);
    }

    public RectangleHitboxEditionData(Vec2D size) {
        this();
        this.size.setValue(size);
    }
    @Override
    public Category getCategory() {
        return Category.HITBOX;
    }
    @Override
    public Type getType() {
        return Types.Hitbox.rectangle;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(size);
    }
    @Override
    public void setToDefault() {
        this.size.setValue(Vec2D.ZERO);
    }

    public static RectangleHitboxEditionData DEFAULT() {
        RectangleHitboxEditionData data = new RectangleHitboxEditionData();
        data.setToDefault();
        return data;
    }
}
