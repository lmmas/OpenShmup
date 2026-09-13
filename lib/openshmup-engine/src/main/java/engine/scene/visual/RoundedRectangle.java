package engine.scene.visual;

import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import lombok.Getter;
import types.RGBAValue;
import types.Vec2D;

final public class RoundedRectangle extends SceneVisual {

    final private RoundedColorRectangle rectangle;

    final private RoundedRectangleBorder border;
    @Getter
    private RGBAValue rectangleBaseColor;
    @Getter
    private RGBAValue borderBaseColor;

    public RoundedRectangle(Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor) {
        super(size, position);
        this.rectangle = new RoundedColorRectangle(size, position, roundingRadius, rectangleColor);
        getGraphicalLayers().add(rectangle, 0);
        this.border = new RoundedRectangleBorder(size, position, roundingRadius, borderWidth, borderColor);
        getGraphicalLayers().add(border, 1);
        this.rectangleBaseColor = rectangleColor;
        this.borderBaseColor = borderColor;
    }
    @Override
    public void setScale(Vec2D scale) {
        super.setScale(scale);
        this.rectangle.setScale(scale);
    }
    @Override
    public void setPosition(Vec2D position) {
        super.setPosition(position);
        this.rectangle.setPosition(position);
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
