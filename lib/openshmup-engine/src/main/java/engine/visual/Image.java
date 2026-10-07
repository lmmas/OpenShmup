package engine.visual;

import engine.assets.Texture;
import engine.graphics.image.ImageGraphic;
import types.RGBAValue;
import types.Vec2D;

final public class Image extends Visual {

    ImageGraphic imageGraphic;

    public Image(ImageGraphic imageGraphic) {
        super(imageGraphic.getScale(), imageGraphic.getPosition());
        this.imageGraphic = new ImageGraphic(imageGraphic);
        getGraphicalLayers().add(imageGraphic, 0);
    }

    public Image(Texture texture, Vec2D size, Vec2D position) {
        super(size, position);
        this.imageGraphic = new ImageGraphic(texture, false,
            size,
            position,
            Vec2D.ONE,
            Vec2D.ZERO,
            RGBAValue.ONE,
            RGBAValue.ZERO);
        this.getGraphicalLayers().add(imageGraphic, 0);
    }
    @Override
    public void setScale(Vec2D scale) {
        super.setScale(scale);
        this.imageGraphic.setScale(scale);
    }
    @Override
    public void setPosition(Vec2D position) {
        super.setPosition(position);
        this.imageGraphic.setPosition(position);
    }
    @Override
    public Visual copy() {
        return new Image(imageGraphic);
    }
    @Override
    public void updateGraphicsColor() {
        imageGraphic.setColorCoefs(colorCoefs);
        imageGraphic.setAddedColor(addedColor);
    }

}
