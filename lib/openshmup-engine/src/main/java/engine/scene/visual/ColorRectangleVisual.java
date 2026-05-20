package engine.scene.visual;

import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class ColorRectangleVisual extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ColorRectangleVisual(int layer, Vec2D size, Vec2D position, RGBAValue color) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.colorRectangleGraphic = new ColorRectangleGraphic(size, position, color);
        graphicsList.add(colorRectangleGraphic);
        this.originalColor = color;
    }

    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(this.sceneLayerIndex, colorRectangleGraphic.getScale(), colorRectangleGraphic.getPosition(), colorRectangleGraphic.getColor());
    }

    @Override
    public void updateGraphicsColor() {
        RGBAValue newRectangleColor = originalColor.multiply(colorCoefs).add(addedColor);
        colorRectangleGraphic.setColor(newRectangleColor);
    }

}
