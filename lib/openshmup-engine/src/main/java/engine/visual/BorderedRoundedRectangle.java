package engine.visual;

import engine.graphics.Graphic;
import engine.graphics.colorRoundedRectangle.ColorRoundedRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;

import java.util.List;

final public class BorderedRoundedRectangle extends SceneVisual {
    final private ColorRoundedRectangle rectangle;
    final private RoundedRectangleBorder border;

    public BorderedRoundedRectangle(int layer, float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float rectangleColorR, float rectangleColorG, float rectangleColorB, float rectangleColorA, float borderColorR, float borderColorG, float borderColorB, float borderColorA) {
        super(layer, List.of(0, 1));
        this.rectangle = new ColorRoundedRectangle(sizeX, sizeY, positionX, positionY, roundingRadius, rectangleColorR, rectangleColorG, rectangleColorB, rectangleColorA);
        this.border = new RoundedRectangleBorder(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, borderColorR, borderColorG, borderColorB, borderColorA);
    }

    @Override
    public SceneVisual copy() {
        return null;
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(rectangle, border);
    }
}
