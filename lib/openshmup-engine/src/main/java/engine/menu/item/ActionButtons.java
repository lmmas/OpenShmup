package engine.menu.item;

import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.List;

public class ActionButtons {

    private ActionButtons() {}

    public static ActionButton ColorRectangleButton(int layer, Vec2D size, Vec2D position, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(List.of(
            new ColorRectangleVisual(layer, size, position, color),
            new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }

    public static ActionButton RoundedRectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(
            List.of(
                new BorderedRoundedRectangle(layer, size.x, size.y, position.x, position.y, roundingRadius, borderWidth, rectangleColor.r, rectangleColor.g, rectangleColor.b, rectangleColor.a, borderColor.r, borderColor.g, borderColor.b, borderColor.a),
                new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }
}