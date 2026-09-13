package engine.scene.visual;

import engine.graphics.colorRectangle.ColorRectangleGraphic;
import types.RGBAValue;
import types.Vec2D;

final public class ColorRectangleVisual extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ColorRectangleVisual(Vec2D size, Vec2D position, RGBAValue color) {
        super(size, position);
        this.colorRectangleGraphic = new ColorRectangleGraphic(size, position, color);
        getGraphicalLayers().add(colorRectangleGraphic, 0);
        this.originalColor = color;
    }

    @Override
    public void setScale(Vec2D scale) {
        super.setScale(scale);
        colorRectangleGraphic.setScale(scale);
    }
    @Override
    public void setPosition(Vec2D position) {
        super.setPosition(position);
        colorRectangleGraphic.setPosition(position);
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
