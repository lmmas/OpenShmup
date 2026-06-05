package json.editionData;

import engine.types.Vec2D;
import json.attribute.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public final class ScrollingImageEditionData implements VisualEditionData {

    final private IntegerAttribute idAttribute;

    final private IntegerAttribute layer;

    final private Vec2DAttribute size;

    final private StringAttribute fileName;

    final private FloatAttribute speed;

    final private BooleanAttribute horizontalScrolling;

    private ScrollingImageEditionData() {
        this.idAttribute = new IntegerAttribute(Keys.Visual.ScrollingImage.id);
        this.layer = new IntegerAttribute(Keys.Visual.ScrollingImage.layer);
        this.size = new Vec2DAttribute(Keys.Visual.ScrollingImage.size);
        this.fileName = new StringAttribute(Keys.Visual.ScrollingImage.fileName);
        this.speed = new FloatAttribute(Keys.Visual.ScrollingImage.speed);
        this.horizontalScrolling = new BooleanAttribute(Keys.Visual.ScrollingImage.horizontalScrolling);
    }

    public ScrollingImageEditionData(int id, int layer, Vec2D size, String imageFileName, float speed, boolean horizontalScrolling) {
        this();
        this.idAttribute.setValue(id);
        this.layer.setValue(layer);
        this.size.setValue(size);
        this.fileName.setValue(imageFileName);
        this.speed.setValue(speed);
        this.horizontalScrolling.setValue(horizontalScrolling);
    }
    @Override
    public int getId() {
        return idAttribute.getValue();
    }
    @Override
    public Category getCategory() {
        return Category.VISUAL;
    }
    @Override
    public Type getType() {
        return Types.Visual.scrollingImage;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(idAttribute, layer, size, fileName, speed, horizontalScrolling);
    }

    @Override
    public void setToDefault() {
        this.idAttribute.setValue(0);
        this.layer.setValue(0);
        this.size.setValue(Vec2D.ZERO);
        this.fileName.setValue("");
        this.speed.setValue(100.0f);
        this.horizontalScrolling.setValue(false);
    }

    public static ScrollingImageEditionData DEFAULT() {
        ScrollingImageEditionData data = new ScrollingImageEditionData();
        data.setToDefault();
        return data;
    }
}
