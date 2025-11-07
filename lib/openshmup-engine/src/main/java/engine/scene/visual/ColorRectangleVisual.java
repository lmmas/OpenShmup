package engine.scene.visual;

import engine.assets.Texture;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.List;

final public class ColorRectangleVisual extends SceneVisual{
    final private ColorRectangle colorRectangle;

    public ColorRectangleVisual(ColorRectangle colorRectangle) {
        this.colorRectangle = colorRectangle;
    }

    public ColorRectangleVisual(int layer, float positionX, float positionY, float sizeX, float sizeY, float r, float g, float b, float a) {
        this(new ColorRectangle(layer, positionX, positionY, sizeX, sizeY, r, g, b, a));
    }

    public ColorRectangleVisual(int layer, Vec2D position, Vec2D size, RGBAValue color){
        this(layer, position.x, position.y, size.x, size.y, color.r, color.g, color.b, color.a);
    }

    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(new ColorRectangle(colorRectangle));
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
