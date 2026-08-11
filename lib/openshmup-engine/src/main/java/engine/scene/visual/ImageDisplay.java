package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.image.ImageGraphic;
import types.RGBAValue;
import types.Vec2D;

final public class ImageDisplay extends SceneVisual {

    ImageGraphic imageGraphic;

    public ImageDisplay(ImageGraphic imageGraphic) {
        super();
        this.imageGraphic = new ImageGraphic(imageGraphic);
        getGraphicalLayers().add(imageGraphic, 0);
    }

    public ImageDisplay(Texture texture, Vec2D size, Vec2D position) {
        super();
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
    public SceneVisual copy() {
        return new ImageDisplay(imageGraphic);
    }
    @Override
    public void updateGraphicsColor() {
        imageGraphic.setColorCoefs(colorCoefs);
        imageGraphic.setAddedColor(addedColor);
    }

}
