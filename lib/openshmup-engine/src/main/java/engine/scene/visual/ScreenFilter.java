package engine.scene.visual;

import engine.Engine;
import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.types.RGBAValue;

import java.util.ArrayList;
import java.util.List;

final public class ScreenFilter extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ScreenFilter(int layer, float r, float g, float b, float a) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.colorRectangleGraphic = new ColorRectangleGraphic(Engine.getNativeWidth(), Engine.getNativeHeight(), (float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2, r, g, b, a);
        graphicsList.add(colorRectangleGraphic);
        this.originalColor = new RGBAValue(r, g, b, a);
    }

    public ScreenFilter(int layer, RGBAValue color) {
        this(layer, color.r, color.g, color.b, color.a);
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
    public void setPosition(float positionX, float positionY) {

    }

    @Override
    public void setScale(float scaleX, float scaleY) {

    }

    @Override
    public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        RGBAValue newRectangleColor = originalColor.multiply(colorCoefs).add(addedColor);
        colorRectangleGraphic.setColor(newRectangleColor.r, newRectangleColor.g, newRectangleColor.g, newRectangleColor.a);
    }
}
