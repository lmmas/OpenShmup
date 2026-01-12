package editor.editionData.visual;

import editor.attribute.BooleanAttribute;
import editor.attribute.FloatAttribute;
import editor.attribute.StringAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrollingImageEditionData extends VisualEditionData {

    private StringAttribute fileName;

    private FloatAttribute speed;

    private BooleanAttribute horizontalScrolling;

    public ScrollingImageEditionData(int id, int layer, float sizeX, float sizeY, String imageFileName, float speed, boolean horizontalScrolling) {
        super(id, layer, sizeX, sizeY);
        this.fileName = new StringAttribute("Image file name", imageFileName);
        this.speed = new FloatAttribute("Scrolling speed", speed);
        this.horizontalScrolling = new BooleanAttribute("Horizontal scrolling", horizontalScrolling);
    }
}
