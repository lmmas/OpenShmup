package engine.menu.widget;

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
import java.util.Objects;
import java.util.function.BiConsumer;

final public class Widgets {

    private Widgets() {}

    public static ActionButton ColorRectangleButton(int layer, Vec2D size, Vec2D position, RGBAValue color, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(List.of(
            new ColorRectangleVisual(layer, size, position, color),
            new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position, size),
            onClick);
    }

    public static ActionButton RoundedRectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(
            List.of(
                new BorderedRoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor),
                new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position, size),
            onClick);
    }

    public static ActionButton RoundedRectangleButton(int layer, Vec2D size, Vec2D position, RoundedRectangleButtonStyle style, String label, Runnable onClick) {
        return RoundedRectangleButton(layer, size, position, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), label, style.textStyle(), onClick);
    }

    public static SelectorButtons StandardSelectorButtons(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, RoundedRectangleButtonStyle unselectedStyle, RoundedRectangleButtonStyle selectedStyle, List<String> labels, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        assert labels.size() == buttonCount : "Incorrect label count";
        List<List<SceneVisual>> buttonVisuals = new ArrayList<>(buttonCount);
        List<Hitbox> hitboxes = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            Vec2D buttonPosition = startPosition.add(stride.scalar(i));
            BorderedRoundedRectangle rectangle = new BorderedRoundedRectangle(layer, size, buttonPosition, unselectedStyle.roundingRadius(), unselectedStyle.borderWidth(), unselectedStyle.rectangleColor(), unselectedStyle.borderColor());
            if (startingValue != null && i == startingValue) {
                rectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
            }
            buttonVisuals.add(List.of(
                rectangle,
                new TextDisplay(layer + 1, false, buttonPosition, labels.get(i), unselectedStyle.textStyle(), TextAlignment.CENTER)
            ));
            hitboxes.add(new SimpleRectangleHitbox(buttonPosition, size));
        }
        BiConsumer<SelectorButtons, Integer> onChangeWithStyleChange = (selector, newValue) -> {
            Integer oldValue = selector.getSelectedValue();
            assert !Objects.equals(oldValue, newValue) : "selector old value can't be equal to new value";
            if (oldValue != null) {
                BorderedRoundedRectangle unselectedButtonRectangle = (BorderedRoundedRectangle) selector.getActionButtons().get(oldValue).getVisuals().getFirst();
                unselectedButtonRectangle.setRectangleBaseColor(unselectedStyle.rectangleColor());
                TextDisplay unselectedButtonText = (TextDisplay) selector.getActionButtons().get(oldValue).getVisuals().get(1);
                unselectedButtonText.setTextColor(unselectedStyle.textStyle().textColor());
            }

            if (newValue != null) {
                BorderedRoundedRectangle selectedButtonRectangle = (BorderedRoundedRectangle) selector.getActionButtons().get(newValue).getVisuals().getFirst();
                selectedButtonRectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
                TextDisplay selectedButtonText = (TextDisplay) selector.getActionButtons().get(newValue).getVisuals().get(1);
                selectedButtonText.setTextColor(selectedStyle.textStyle().textColor());
                onChange.accept(selector, newValue);
            }
        };
        return new SelectorButtons(buttonVisuals, hitboxes, onChangeWithStyleChange, startingValue);
    }
}