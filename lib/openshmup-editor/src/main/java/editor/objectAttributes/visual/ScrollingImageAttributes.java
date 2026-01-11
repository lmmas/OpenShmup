package editor.objectAttributes.visual;

import editor.attribute.BooleanAttribute;
import editor.attribute.FloatAttribute;
import editor.attribute.StringAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrollingImageAttributes extends VisualAttributes {

    private StringAttribute fileName;

    private FloatAttribute speed;

    private BooleanAttribute horizontalScrolling;

    public ScrollingImageAttributes(int id, int layer, float sizeX, float sizeY, String imageFileName, float speed, boolean horizontalScrolling) {
        super(id, layer, sizeX, sizeY);
        this.fileName = new StringAttribute("Image file name", imageFileName);
        this.speed = new FloatAttribute("Scrolling speed", speed);
        this.horizontalScrolling = new BooleanAttribute("Horizontal scrolling", horizontalScrolling);
    }
}
