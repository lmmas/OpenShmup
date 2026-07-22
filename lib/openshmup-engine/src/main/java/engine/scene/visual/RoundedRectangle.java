package engine.scene.visual;

import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import lombok.Getter;
import types.RGBAValue;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class RoundedRectangle extends SceneVisual {

    final private RoundedColorRectangle rectangle;

    final private RoundedRectangleBorder border;
    @Getter
    private RGBAValue rectangleBaseColor;
    @Getter
    private RGBAValue borderBaseColor;

    public RoundedRectangle(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor) {
        super(layer, new ArrayList<>(2), List.of(0, 1));
        this.rectangle = new RoundedColorRectangle(size, position, roundingRadius, rectangleColor);
        graphicsList.add(rectangle);
        this.border = new RoundedRectangleBorder(size, position, roundingRadius, borderWidth, borderColor);
        graphicsList.add(border);
        this.rectangleBaseColor = rectangleColor;
        this.borderBaseColor = borderColor;
    }

    @Override
    public SceneVisual copy() {
        return null;
    }

    @Override
    public void updateGraphicsColor() {
        RGBAValue newRectangleColor = rectangleBaseColor.multiply(colorCoefs).add(addedColor);
        rectangle.setColor(newRectangleColor);
        RGBAValue newBorderColor = borderBaseColor.multiply(colorCoefs).add(addedColor);
        border.setColor(newBorderColor);
    }

    public void setRectangleBaseColor(RGBAValue newColor) {
        this.rectangleBaseColor = newColor;
        updateGraphicsColor();
    }

    public void setBorderBaseColor(RGBAValue newColor) {
        this.borderBaseColor = newColor;
        updateGraphicsColor();
    }
}
