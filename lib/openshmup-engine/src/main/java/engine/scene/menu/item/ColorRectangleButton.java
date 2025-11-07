package engine.scene.menu.item;

import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.scene.menu.MenuItem;
import engine.visual.ColorRectangleVisual;
import engine.visual.TextDisplay;
import engine.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.List;

public class ColorRectangleButton extends MenuItem {
    public ColorRectangleButton(int layer,Vec2D position, Vec2D size, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        super(List.of(
                new ColorRectangleVisual(layer, position, size, color),
                new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle)),
                new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
                onClick);
    }
}
