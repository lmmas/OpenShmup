package engine.menu.widget;

import engine.hitbox.Hitbox;
import engine.hitbox.RectangleBox;
import engine.visual.RoundedRectangle;
import engine.visual.Text;
import engine.visual.Visual;
import engine.visual.style.RoundedRectangleStyle;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;
import types.RGBAValue;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

final public class Widgets {

    private Widgets() {}

    public static ActionButton TextButton(Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue rectangleColor, RGBAValue borderColor, String label, TextStyle textStyle, Runnable onClick) {
        RoundedRectangle background = new RoundedRectangle(size, position, roundingRadius, borderWidth, rectangleColor, borderColor);
        return new ActionButton(
            Map.of(background, 0,
                new Text(false, position, label, textStyle, TextAlignment.CENTER), 1), background,
            new RectangleBox(position, size),
            onClick);
    }

    public static ActionButton TextButton(Vec2D size, Vec2D position, RoundedRectangleStyle style, TextStyle textStyle, String label, Runnable onClick) {
        return TextButton(size, position, style.roundingRadius(), style.borderWidth(), style.rectangleColor(), style.borderColor(), label, textStyle, onClick);
    }

    public static void setTextButtonStyle(ActionButton textButton, RoundedRectangleStyle roundedRectangleStyle, TextStyle textStyle) {
        RoundedRectangle roundedRectangle = (RoundedRectangle) textButton.getBackground();
        roundedRectangle.setStyle(roundedRectangleStyle);
        Text text = (Text) textButton.getVisualGroup().getVisualsMap().getLayer(1).getFirst();
        text.setStyle(textStyle);
    }

    public static SelectorButtons StandardSelectorButtons(int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, RoundedRectangleStyle unselectedStyle, RoundedRectangleStyle selectedStyle, TextStyle textStyle, List<String> labels, BiConsumer<SelectorButtons, Integer> onChange, Integer startingValue) {
        assert labels.size() == buttonCount : "Incorrect label count";
        List<Visual> buttonBackgrounds = new ArrayList<>(buttonCount);
        List<Map<Visual, Integer>> buttonVisuals = new ArrayList<>(buttonCount);
        List<Hitbox> hitboxes = new ArrayList<>(buttonCount);
        for (int i = 0; i < buttonCount; i++) {
            Vec2D buttonPosition = startPosition.add(stride.scalar(i));
            RoundedRectangle rectangle = new RoundedRectangle(size, buttonPosition, unselectedStyle.roundingRadius(), unselectedStyle.borderWidth(), unselectedStyle.rectangleColor(), unselectedStyle.borderColor());
            if (startingValue != null && i == startingValue) {
                rectangle.setRectangleBaseColor(selectedStyle.rectangleColor());
            }
            buttonBackgrounds.add(rectangle);
            buttonVisuals.add(Map.of(
                rectangle, 0,
                new Text(false, buttonPosition, labels.get(i), textStyle, TextAlignment.CENTER), 1)
            );
            hitboxes.add(new RectangleBox(buttonPosition, size));
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
        return new SelectorButtons(buttonVisuals, buttonBackgrounds, hitboxes, onChangeWithStyleChange, startingValue);
    }
}