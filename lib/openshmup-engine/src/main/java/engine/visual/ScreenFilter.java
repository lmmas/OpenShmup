package engine.visual;

import engine.assets.Texture;
import engine.graphics.colorRectangle.ColorRectangle;
import engine.graphics.Graphic;
import engine.graphics.RenderInfo;
import engine.types.RGBAValue;

import java.util.List;

final public class ScreenFilter extends SceneVisual{
    final private ColorRectangle colorRectangle;

    public ScreenFilter(int layer, float r, float g, float b, float a){
        super(layer, List.of(0));
        this.colorRectangle = new ColorRectangle(1.0f, 1.0f, 0.5f, 0.5f, r, g, b, a);
    }

    public ScreenFilter(int layer, RGBAValue color){
        this(layer, color.r, color.g, color.b, color.a);
    }

    public ScreenFilter(ScreenFilter screenFilter){
        super(screenFilter.sceneLayer, List.of(0));
        this.colorRectangle = new ColorRectangle(screenFilter.colorRectangle);
    }

    @Override
    public SceneVisual copy() {
        return new ScreenFilter(this);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(colorRectangle);
    }

    @Override
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void setScale(float scaleX, float scaleY) {

    }
}
