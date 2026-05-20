package engine.scene.visual;

import engine.Engine;
import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class ScreenFilter extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ScreenFilter(int layer, RGBAValue color) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.colorRectangleGraphic = new ColorRectangleGraphic(Engine.getNativeResolution().scalar(1.0f), Engine.getNativeResolution().scalar(0.5f), color);
        graphicsList.add(colorRectangleGraphic);
        this.originalColor = color;
    }

    public ScreenFilter(ScreenFilter screenFilter) {
        super(screenFilter.sceneLayerIndex, new ArrayList<>(1), List.of(0));
        this.colorRectangleGraphic = new ColorRectangleGraphic(screenFilter.colorRectangleGraphic);
        graphicsList.add(colorRectangleGraphic);
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
