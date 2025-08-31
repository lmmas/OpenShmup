package engine.scene.display;

import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderInfo;
import engine.assets.Texture;

import java.util.List;

final public class ImageDisplay implements SceneVisual {
    Image2D image;

    public ImageDisplay(Image2D image){
        this.image = image;
    }
    @Override
    public SceneVisual copy() {
        return new ImageDisplay(image.copy());
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
