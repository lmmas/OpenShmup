package engine.menu.item;

import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.RoundedRectangleButtonStyle;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final public class MenuItems {

    private MenuItems() {}

    public static ActionButton ColorRectangleButton(int layer, Vec2D size, Vec2D position, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(List.of(
            new ColorRectangleVisual(layer, size, position, color),
            new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }

    public static ActionButton RoundedRectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(
            List.of(
                new BorderedRoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor),
                new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position.x, position.y, size.x, size.y),
            onClick);
    }

    public static ActionButton RoundedRectangleButton(int layer, Vec2D size, Vec2D position, RoundedRectangleButtonStyle style, String label, Runnable onClick) {
        return RoundedRectangleButton(layer, size, position, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), label, style.textStyle(), onClick);
    }

    public static SelectorButtons StandardSelectorButtons(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, TextStyle textStyle, List<String> labels, Consumer<Integer> onChange) {
        List<List<SceneVisual>> buttonVisuals = new ArrayList<>(buttonCount);
        List<Hitbox> hitboxes = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            Vec2D buttonPosition = startPosition.add(stride.scalar(i));
            buttonVisuals.add(List.of(
                new BorderedRoundedRectangle(layer, size, buttonPosition, roundingRadius, borderWidth, rectangleColor, borderColor),
                new TextDisplay(layer + 1, false, buttonPosition, labels.get(i), textStyle, TextAlignment.CENTER)
            ));
            hitboxes.add(new SimpleRectangleHitbox(buttonPosition.x, buttonPosition.y, size.x, size.y));
        }
        return new SelectorButtons(buttonVisuals, hitboxes, onChange);
    }

    public static SelectorButtons StandardSelectorButtons(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, RoundedRectangleButtonStyle style, List<String> labels, Consumer<Integer> onChange) {
        return StandardSelectorButtons(layer, buttonCount, size, startPosition, stride, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), style.textStyle(), labels, onChange);
    }
}