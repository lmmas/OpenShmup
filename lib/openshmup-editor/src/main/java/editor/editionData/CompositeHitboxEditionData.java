package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.StringAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class CompositeHitboxEditionData implements HitboxEditionData {

    Vec2DAttribute size;

    StringAttribute fileName;

    public CompositeHitboxEditionData(Vec2D size, String fileName) {
        this.size = new Vec2DAttribute("Size", JsonFieldNames.CompositeHitbox.size, size);
        this.fileName = new StringAttribute("Texture file name", JsonFieldNames.CompositeHitbox.fileName, fileName);
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(size, fileName);
    }
}
