package engine.scene.menu.item;

import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.scene.menu.MenuItem;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;

import java.util.List;

public class ColorRectangleButton extends MenuItem {
    public ColorRectangleButton(int layer, float sizeX, float sizeY, float positionX, float positionY, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        super(List.of(
                new ColorRectangleVisual(layer, sizeX, sizeY, positionX, positionY, color.r, color.g, color.b, color.a),
                new TextDisplay(layer + 1, false, positionX, positionY, label, textStyle)),
                new SimpleRectangleHitbox(positionX, positionY, sizeX, sizeY),
                onClick);
    }
}
