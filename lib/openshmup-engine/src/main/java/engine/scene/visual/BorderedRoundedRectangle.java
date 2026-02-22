package engine.scene.visual;

import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class BorderedRoundedRectangle extends SceneVisual {

    final private RoundedColorRectangle rectangle;

    final private RoundedRectangleBorder border;

    final private RGBAValue rectangleOriginalColor;

    final private RGBAValue borderOriginalColor;

    public BorderedRoundedRectangle(int layer, float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float rectangleColorR, float rectangleColorG, float rectangleColorB, float rectangleColorA, float borderColorR, float borderColorG, float borderColorB, float borderColorA) {
        super(layer, new ArrayList<>(2), List.of(0, 1));
        this.rectangle = new RoundedColorRectangle(sizeX, sizeY, positionX, positionY, roundingRadius, rectangleColorR, rectangleColorG, rectangleColorB, rectangleColorA);
        graphicsList.add(rectangle);
        this.border = new RoundedRectangleBorder(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, borderColorR, borderColorG, borderColorB, borderColorA);
        graphicsList.add(border);
        this.rectangleOriginalColor = new RGBAValue(rectangleColorR, rectangleColorG, rectangleColorB, rectangleColorA);
        this.borderOriginalColor = new RGBAValue(borderColorR, borderColorG, borderColorB, borderColorA);
    }

    public BorderedRoundedRectangle(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor) {
        this(layer, size.x, size.y, position.x, position.y, roundingRadius, borderWidth, rectangleColor.r, rectangleColor.g, rectangleColor.b, rectangleColor.a, borderColor.r, borderColor.g, borderColor.b, borderColor.a);
    }

    @Override
    public SceneVisual copy() {
        return null;
    }

    @Override
    public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        RGBAValue newRectangleColor = rectangleOriginalColor.multiply(colorCoefs).add(addedColor);
        rectangle.setColor(newRectangleColor.r, newRectangleColor.g, newRectangleColor.g, newRectangleColor.a);
        RGBAValue newBorderColor = borderOriginalColor.multiply(colorCoefs).add(addedColor);
        border.setColor(newBorderColor.r, newBorderColor.g, newBorderColor.b, newBorderColor.a);
    }

}
