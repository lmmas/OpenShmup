package engine.scene.visual;

import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class ColorRectangleVisual extends SceneVisual {

    final private ColorRectangleGraphic colorRectangleGraphic;

    final private RGBAValue originalColor;

    public ColorRectangleVisual(int layer, float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.colorRectangleGraphic = new ColorRectangleGraphic(sizeX, sizeY, positionX, positionY, r, g, b, a);
        graphicsList.add(colorRectangleGraphic);
        this.originalColor = new RGBAValue(r, g, b, a);
    }

    public ColorRectangleVisual(int layer, Vec2D size, Vec2D position, RGBAValue color) {
        this(layer, size.x, size.y, position.x, position.y, color.r, color.g, color.b, color.a);
    }

    @Override
    public SceneVisual copy() {
        return new ColorRectangleVisual(this.sceneLayerIndex, colorRectangleGraphic.getScale(), colorRectangleGraphic.getPosition(), colorRectangleGraphic.getColor());
    }

    @Override
    public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        RGBAValue newRectangleColor = originalColor.multiply(colorCoefs).add(addedColor);
        colorRectangleGraphic.setColor(newRectangleColor.r, newRectangleColor.g, newRectangleColor.g, newRectangleColor.a);
    }

}
