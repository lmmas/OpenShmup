package engine.scene.menu;

import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;
import engine.visual.ColorRectangleVisual;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;

import java.util.List;

public class MenuItems {

    public static MenuItem ColorRectangleButton(int layer, Vec2D size, Vec2D position, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        return new MenuItem(List.of(
            new ColorRectangleVisual(layer, size, position, color),
            new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }

    public static MenuItem RoundedRectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new MenuItem(
            List.of(
                new BorderedRoundedRectangle(layer, size.x, size.y, position.x, position.y, roundingRadius, borderWidth, rectangleColor.r, rectangleColor.g, rectangleColor.b, rectangleColor.a, borderColor.r, borderColor.g, borderColor.b, borderColor.a),
                new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }
}