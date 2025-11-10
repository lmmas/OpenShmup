package engine.visual;

import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.graphics.RenderInfo;
import engine.assets.Texture;
import engine.types.Vec2D;

import java.util.List;

final public class ImageDisplay extends SceneVisual {
    Image image;

    public ImageDisplay(Image image){
        this.image = new Image(image);
    }

    public ImageDisplay(int layer, Texture texture, float sizeX, float sizeY, float positionX, float positionY){
        this.image = new Image(texture, layer, false, sizeX, sizeY, positionX, positionY, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public ImageDisplay(int layer, Texture texture, Vec2D size, Vec2D position){
        this(layer, texture, size.x, size.y, position.x, position.y);
    }

    @Override
    public SceneVisual copy() {
        return new ImageDisplay(image);
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(image.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }

    @Override
    public List<Texture> getTextures() {
        return List.of(image.getTexture());
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        image.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay(float startingTimeSeconds) {

    }

    @Override
    public void update(float currentTimeSeconds) {

    }
}
