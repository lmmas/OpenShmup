package engine.menu.widget;

import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.RectangleButtonStyle;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import types.RGBAValue;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

final public class Widgets {

    private Widgets() {}

    public static ActionButton RectangleButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(
            new BorderedRoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor),
            List.of(new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position, size),
            onClick);
    }

    public static ActionButton RectangleButton(int layer, Vec2D size, Vec2D position, RectangleButtonStyle style, String label, Runnable onClick) {
        return RectangleButton(layer, size, position, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), label, style.textStyle(), onClick);
    }

    public static SelectorButtons StandardSelectorButtons(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, RectangleButtonStyle unselectedStyle, RectangleButtonStyle selectedStyle, List<String> labels, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        assert labels.size() == buttonCount : "Incorrect label count";
        List<SceneVisual> buttonBackgrounds = new ArrayList<>(buttonCount);
        List<List<SceneVisual>> buttonOtherVisuals = new ArrayList<>(buttonCount);
        List<Hitbox> hitboxes = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            Vec2D buttonPosition = startPosition.add(stride.scalar(i));
            BorderedRoundedRectangle rectangle = new BorderedRoundedRectangle(layer, size, buttonPosition, unselectedStyle.roundingRadius(), unselectedStyle.borderWidth(), unselectedStyle.rectangleColor(), unselectedStyle.borderColor());
            if (startingValue != null && i == startingValue) {
                rectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
            }
            buttonBackgrounds.add(rectangle);
            buttonOtherVisuals.add(List.of(
                new TextDisplay(layer + 1, false, buttonPosition, labels.get(i), unselectedStyle.textStyle(), TextAlignment.CENTER)
            ));
            hitboxes.add(new SimpleRectangleHitbox(buttonPosition, size));
        }
        BiConsumer<SelectorButtons, Integer> onChangeWithStyleChange = (selector, newValue) -> {
            Integer oldValue = selector.getSelectedValue();
            assert !Objects.equals(oldValue, newValue) : "selector old value can't be equal to new value";
            if (oldValue != null) {
                BorderedRoundedRectangle unselectedButtonRectangle = (BorderedRoundedRectangle) selector.getActionButtons().get(oldValue).getBackground();
                unselectedButtonRectangle.setRectangleBaseColor(unselectedStyle.rectangleColor());
                TextDisplay unselectedButtonText = (TextDisplay) selector.getActionButtons().get(oldValue).getOtherVisuals().getFirst();
                unselectedButtonText.setTextColor(unselectedStyle.textStyle().textColor());
            }

            if (newValue != null) {
                BorderedRoundedRectangle selectedButtonRectangle = (BorderedRoundedRectangle) selector.getActionButtons().get(newValue).getBackground();
                selectedButtonRectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
                TextDisplay selectedButtonText = (TextDisplay) selector.getActionButtons().get(newValue).getOtherVisuals().getFirst();
                selectedButtonText.setTextColor(selectedStyle.textStyle().textColor());
                onChange.accept(selector, newValue);
            }
        };
        return new SelectorButtons(buttonBackgrounds, buttonOtherVisuals, hitboxes, onChangeWithStyleChange, startingValue);
    }
}