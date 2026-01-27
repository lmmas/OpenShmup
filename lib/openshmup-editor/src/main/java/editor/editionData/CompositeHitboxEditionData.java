package editor.editionData;

import editor.attribute.StringAttribute;
import editor.attribute.Vec2DAttribute;
import json.JsonFieldNames;
import lombok.Getter;

@Getter final public class CompositeHitboxEditionData implements EditionData, HitboxEditionData {

    Vec2DAttribute size;

    StringAttribute fileName;

    public CompositeHitboxEditionData(float sizeX, float sizeY, String fileName) {
        this.size = new Vec2DAttribute("Size", JsonFieldNames.CompositeHitbox.size, sizeX, sizeY);
        this.fileName = new StringAttribute("Texture file name", JsonFieldNames.CompositeHitbox.fileName, fileName);
    }
}
