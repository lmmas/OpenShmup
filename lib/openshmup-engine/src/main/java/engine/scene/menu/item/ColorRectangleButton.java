package engine.scene.menu.item;

import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.scene.menu.MenuItem;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.ColorRectangleVisual;
import engine.visual.TextDisplay;
import engine.visual.style.TextStyle;

import java.util.List;

public class ColorRectangleButton extends MenuItem {

    public ColorRectangleButton(int layer, Vec2D size, Vec2D position, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        super(List.of(
                new ColorRectangleVisual(layer, size, position, color),
                new TextDisplay(layer + 1, false, position.x, position.y, label, textStyle)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }
}
