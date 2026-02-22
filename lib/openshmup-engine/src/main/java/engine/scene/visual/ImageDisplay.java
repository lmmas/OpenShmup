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

    public ImageDisplay(int layer, Texture texture, float sizeX, float sizeY, float positionX, float positionY) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.imageGraphic = new ImageGraphic(texture, false,
            sizeX, sizeY,
            positionX, positionY,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        graphicsList.add(imageGraphic);
    }

    public ImageDisplay(int layer, Texture texture, Vec2D size, Vec2D position) {
        this(layer, texture, size.x, size.y, position.x, position.y);
    }

    @Override
    public SceneVisual copy() {
        return new ImageDisplay(this.sceneLayerIndex, imageGraphic);
    }

    @Override public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        imageGraphic.setColorCoefs(colorCoefs.r, colorCoefs.g, colorCoefs.b, colorCoefs.a);
        imageGraphic.setAddedColor(addedColor.r, addedColor.g, addedColor.b, addedColor.a);
    }

}
