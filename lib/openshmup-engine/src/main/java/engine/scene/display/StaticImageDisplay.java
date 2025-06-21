package engine.scene.display;

import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.scene.Scene;

import java.util.List;
import java.util.Optional;

public class StaticImageDisplay implements SceneDisplay{
    Image2D image;

    public StaticImageDisplay(Image2D image){
        this.image = image;
    }
    @Override
    public SceneDisplay copy() {
        return new StaticImageDisplay(image.copy());
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(image.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(image.getTexture());
    }

    @Override
    public void setScene(Scene scene) {

    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    @Override
    public void update(float currentTimeSeconds) {

    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }
}
