package engine.scene.menu.item;

import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.scene.menu.MenuItem;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;

import java.util.List;

public class RoundedRectangleButton extends MenuItem {

    public RoundedRectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        super(
            List.of(
                new BorderedRoundedRectangle(layer, size.x, size.y, position.x, position.y, roundingRadius, borderWidth, rectangleColor.r, rectangleColor.g, rectangleColor.b, rectangleColor.a, borderColor.r, borderColor.g, borderColor.b, borderColor.a),
                new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }
}
