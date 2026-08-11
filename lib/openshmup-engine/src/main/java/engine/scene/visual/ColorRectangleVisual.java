package engine.scene.visual;

import engine.graphics.colorRectangle.ColorRectangleGraphic;
import types.RGBAValue;
import types.Vec2D;

final public class ColorRectangleVisual extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ColorRectangleVisual(Vec2D size, Vec2D position, RGBAValue color) {
        super();
        this.colorRectangleGraphic = new ColorRectangleGraphic(size, position, color);
        getGraphicalLayers().add(colorRectangleGraphic, 0);
        this.originalColor = color;
    }

    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(colorRectangleGraphic.getScale(), colorRectangleGraphic.getPosition(), colorRectangleGraphic.getColor());
    }

    @Override
    public void updateGraphicsColor() {
        RGBAValue newRectangleColor = originalColor.multiply(colorCoefs).add(addedColor);
        colorRectangleGraphic.setColor(newRectangleColor);
    }

}
