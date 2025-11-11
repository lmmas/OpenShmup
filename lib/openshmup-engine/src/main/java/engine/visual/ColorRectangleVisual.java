package engine.visual;

import engine.assets.Texture;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.List;

final public class ColorRectangleVisual extends SceneVisual{
    final private ColorRectangle colorRectangle;

    public ColorRectangleVisual(int layer, ColorRectangle colorRectangle) {
        super(layer);
        this.colorRectangle = colorRectangle;
    }

    public ColorRectangleVisual(int layer, float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
        this(layer, new ColorRectangle(sizeX, sizeY, positionX, positionY, r, g, b, a));
    }

    public ColorRectangleVisual(int layer, Vec2D size, Vec2D position, RGBAValue color){
        this(layer, size.x, size.y, position.x, position.y, color.r, color.g, color.b, color.a);
    }

    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(this.sceneLayer, new ColorRectangle(colorRectangle));
    }

    @Override
    public List<Integer> getGraphicalSubLayers() {
        return List.of(0);
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
