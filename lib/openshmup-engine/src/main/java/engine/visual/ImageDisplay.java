package engine.visual;

import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.graphics.RenderInfo;
import engine.assets.Texture;
import engine.types.Vec2D;

import java.util.List;

final public class ImageDisplay extends SceneVisual {
    Image image;

    public ImageDisplay(int layer, Image image){
        super(layer, List.of(0));
        this.image = new Image(image);
    }

    public ImageDisplay(int layer, Texture texture, float sizeX, float sizeY, float positionX, float positionY){
        super(layer, List.of(0));
        this.image = new Image(texture, false,
                sizeX, sizeY,
                positionX, positionY,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f);
    }

    public ImageDisplay(int layer, Texture texture, Vec2D size, Vec2D position){
        this(layer, texture, size.x, size.y, position.x, position.y);
    }

    @Override
    public SceneVisual copy() {
        return new ImageDisplay(this.sceneLayer, image);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }
}
