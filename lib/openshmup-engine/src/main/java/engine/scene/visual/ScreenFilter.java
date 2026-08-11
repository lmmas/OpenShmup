package engine.scene.visual;

import engine.Engine;
import engine.graphics.colorRectangle.ColorRectangleGraphic;
import types.RGBAValue;
import types.Vec2D;

final public class ScreenFilter extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ScreenFilter(RGBAValue color) {
        super();
        this.colorRectangleGraphic = new ColorRectangleGraphic(Engine.getNativeResolution().scalar(1.0f), Engine.getNativeResolution().scalar(0.5f), color);
        getGraphicalLayers().add(colorRectangleGraphic, 0);
        this.originalColor = color;
    }

    public ScreenFilter(ScreenFilter screenFilter) {
        super();
        this.colorRectangleGraphic = new ColorRectangleGraphic(screenFilter.colorRectangleGraphic);
        getGraphicalLayers().add(colorRectangleGraphic, 0);
        this.originalColor = new RGBAValue(screenFilter.originalColor);
    }

    @Override
    public SceneVisual copy() {
        return new ScreenFilter(this);
    }

    @Override
    public void setPosition(Vec2D position) {

    }

    @Override
    public void setScale(Vec2D scale) {

    }

    @Override
    public void updateGraphicsColor() {
        RGBAValue newRectangleColor = originalColor.multiply(colorCoefs).add(addedColor);
        colorRectangleGraphic.setColor(newRectangleColor);
    }
}
