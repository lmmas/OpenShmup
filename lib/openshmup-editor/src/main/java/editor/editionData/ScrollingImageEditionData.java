package editor.editionData;

import editor.attribute.*;
import json.JsonFieldNames;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class ScrollingImageEditionData implements EditionData, VisualEditionData {

    private IntegerAttribute idAttribute;

    private IntegerAttribute layer;

    private Vec2DAttribute size;

    private StringAttribute fileName;

    private FloatAttribute speed;

    private BooleanAttribute horizontalScrolling;

    public ScrollingImageEditionData(int id, int layer, float sizeX, float sizeY, String imageFileName, float speed, boolean horizontalScrolling) {
        this.idAttribute = new IntegerAttribute("Visual ID", JsonFieldNames.ScrollingImage.id, id);
        this.layer = new IntegerAttribute("Scene layer", JsonFieldNames.ScrollingImage.layer, layer);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.ScrollingImage.size, sizeX, sizeY);
        this.fileName = new StringAttribute("Image file name", JsonFieldNames.ScrollingImage.fileName, imageFileName);
        this.speed = new FloatAttribute("Scrolling playerMovementSpeed", JsonFieldNames.ScrollingImage.speed, speed);
        this.horizontalScrolling = new BooleanAttribute("Horizontal scrolling", JsonFieldNames.ScrollingImage.horizontalScrolling, horizontalScrolling);
    }

    @Override
    public int getId() {
        return idAttribute.getValue();
    }
}
