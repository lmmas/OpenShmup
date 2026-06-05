package json.editionData;

import engine.types.Vec2D;
import json.attribute.Attribute;
import json.attribute.StringAttribute;
import json.attribute.Vec2DAttribute;
import lombok.Getter;

import java.util.List;

@Getter final public class CustomHitboxEditionData implements HitboxEditionData {

    final private Vec2DAttribute size;

    final private StringAttribute fileName;

    private CustomHitboxEditionData() {
        this.size = new Vec2DAttribute(Keys.Hitbox.CustomHitbox.size);
        this.fileName = new StringAttribute(Keys.Hitbox.CustomHitbox.fileName);
    }

    public CustomHitboxEditionData(Vec2D size, String fileName) {
        this();
        this.size.setValue(size);
        this.fileName.setValue(fileName);
    }
    @Override
    public Category getCategory() {
        return Category.HITBOX;
    }
    @Override
    public Type getType() {
        return Types.Hitbox.custom;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(size, fileName);
    }
    @Override
    public void setToDefault() {
        this.size.setValue(Vec2D.ZERO);
        this.fileName.setValue("");
    }

    public static CustomHitboxEditionData DEFAULT() {
        CustomHitboxEditionData data = new CustomHitboxEditionData();
        data.setToDefault();
        return data;
    }
}
