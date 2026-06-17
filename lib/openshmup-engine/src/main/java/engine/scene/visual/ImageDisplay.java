package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.image.ImageGraphic;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class ImageDisplay extends SceneVisual {

    ImageGraphic imageGraphic;

    public ImageDisplay(int layer, ImageGraphic imageGraphic) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.imageGraphic = new ImageGraphic(imageGraphic);
        graphicsList.add(imageGraphic);
    }

    public ImageDisplay(int layer, Texture texture, Vec2D size, Vec2D position) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.imageGraphic = new ImageGraphic(texture, false,
            size,
            position,
            Vec2D.ONE,
            Vec2D.ZERO,
            RGBAValue.ONE,
            RGBAValue.ZERO);
        graphicsList.add(imageGraphic);
    }
    @Override
    public SceneVisual copy() {
        return new ImageDisplay(this.sceneLayerIndex, imageGraphic);
    }
    @Override
    public void updateGraphicsColor() {
        imageGraphic.setColorCoefs(colorCoefs);
        imageGraphic.setAddedColor(addedColor);
    }

}
