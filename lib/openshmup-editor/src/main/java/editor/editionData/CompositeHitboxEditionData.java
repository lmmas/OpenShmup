package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.StringAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class CompositeHitboxEditionData implements HitboxEditionData {

    final private Vec2DAttribute size;

    final private StringAttribute fileName;

    private CompositeHitboxEditionData() {
        this.size = new Vec2DAttribute("Size", JsonFieldNames.CompositeHitbox.size);
        this.fileName = new StringAttribute("Texture file name", JsonFieldNames.CompositeHitbox.fileName);
    }

    public CompositeHitboxEditionData(Vec2D size, String fileName) {
        this();
        this.size.setValue(size);
        this.fileName.setValue(fileName);
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

    public static CompositeHitboxEditionData DEFAULT() {
        CompositeHitboxEditionData data = new CompositeHitboxEditionData();
        data.setToDefault();
        return data;
    }
}
