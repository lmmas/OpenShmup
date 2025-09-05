package engine.scene.display;

import engine.assets.Texture;
import engine.graphics.ColorRectangle;
import engine.graphics.Graphic;
import engine.render.RenderInfo;

import java.util.List;

final public class ColorShape extends SceneVisual{
    final private ColorRectangle colorRectangle;

    public ColorShape(ColorRectangle colorRectangle) {
        this.colorRectangle = colorRectangle;
    }

    @Override
    public SceneVisual copy() {
        return new ColorShape(colorRectangle.copy());
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(colorRectangle.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(colorRectangle);
    }

    @Override
    public List<Texture> getTextures() {
        return List.of();
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        colorRectangle.setPosition(positionX, positionY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        colorRectangle.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay(float startingTimeSeconds) {

    }

    @Override
    public void update(float currentTimeSeconds) {

    }
}
