package engine.menu.widget;

import engine.hitbox.Hitbox;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.RoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.RoundedRectangleStyle;
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

    public static ActionButton TextButton(int layer, Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        return new ActionButton(
            new RoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor),
            List.of(new TextDisplay(layer + 1, false, position, label, textStyle, TextAlignment.CENTER)),
            new SimpleRectangleHitbox(position, size),
            onClick);
    }

    public static ActionButton TextButton(int layer, Vec2D size, Vec2D position, RoundedRectangleStyle style, TextStyle textStyle, String label, Runnable onClick) {
        return TextButton(layer, size, position, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), label, textStyle, onClick);
    }

    public static SelectorButtons StandardSelectorButtons(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, RoundedRectangleStyle unselectedStyle, RoundedRectangleStyle selectedStyle, TextStyle textStyle, List<String> labels, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        assert labels.size() == buttonCount : "Incorrect label count";
        List<SceneVisual> buttonBackgrounds = new ArrayList<>(buttonCount);
        List<List<SceneVisual>> buttonOtherVisuals = new ArrayList<>(buttonCount);
        List<Hitbox> hitboxes = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            Vec2D buttonPosition = startPosition.add(stride.scalar(i));
            RoundedRectangle rectangle = new RoundedRectangle(layer, size, buttonPosition, unselectedStyle.roundingRadius(), unselectedStyle.borderWidth(), unselectedStyle.rectangleColor(), unselectedStyle.borderColor());
            if (startingValue != null && i == startingValue) {
                rectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
            }
            buttonBackgrounds.add(rectangle);
            buttonOtherVisuals.add(List.of(
                new TextDisplay(layer + 1, false, buttonPosition, labels.get(i), textStyle, TextAlignment.CENTER)
            ));
            hitboxes.add(new SimpleRectangleHitbox(buttonPosition, size));
        }
        BiConsumer<SelectorButtons, Integer> onChangeWithStyleChange = (selector, newValue) -> {
            Integer oldValue = selector.getSelectedValue();
            assert !Objects.equals(oldValue, newValue) : "selector old value can't be equal to new value";
            if (oldValue != null) {
                RoundedRectangle unselectedButtonRectangle = (RoundedRectangle) selector.getActionButtons().get(oldValue).getBackground();
                unselectedButtonRectangle.setRectangleBaseColor(unselectedStyle.rectangleColor());
            }

            if (newValue != null) {
                RoundedRectangle selectedButtonRectangle = (RoundedRectangle) selector.getActionButtons().get(newValue).getBackground();
                selectedButtonRectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
                onChange.accept(selector, newValue);
            }
        };
        return new SelectorButtons(buttonBackgrounds, buttonOtherVisuals, hitboxes, onChangeWithStyleChange, startingValue);
    }
}