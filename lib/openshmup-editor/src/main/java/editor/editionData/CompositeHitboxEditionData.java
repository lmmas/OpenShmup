package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.StringAttribute;
import editor.attribute.Vec2DAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class CompositeHitboxEditionData implements HitboxEditionData {

    Vec2DAttribute size;

    StringAttribute fileName;

    public CompositeHitboxEditionData(float sizeX, float sizeY, String fileName) {
        this.size = new Vec2DAttribute("Size", JsonFieldNames.CompositeHitbox.size, sizeX, sizeY);
        this.fileName = new StringAttribute("Texture file name", JsonFieldNames.CompositeHitbox.fileName, fileName);
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(size, fileName);
    }
}
