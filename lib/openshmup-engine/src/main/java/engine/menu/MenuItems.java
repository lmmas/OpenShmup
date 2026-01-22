package engine.menu;

import engine.Engine;
import engine.InputStatesManager;
import engine.level.entity.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

public class MenuItems {

    private MenuItems() {}

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

    public static MenuItem TextInputField(float sizeX, float sizeY, float positionX, float positionY, TextStyle style, List<SceneVisual> otherVisuals) {
        TextDisplay textInputDisplay = new TextDisplay(2, true, positionX - sizeX / 2, positionY, "", style, TextAlignment.LEFT);
        ArrayList<SceneVisual> visuals = new ArrayList<>(otherVisuals);
        visuals.add(textInputDisplay);
        SimpleRectangleHitbox hitbox = new SimpleRectangleHitbox(positionX, positionY, sizeX, sizeY);
        Runnable onClick = () -> {
            InputStatesManager inputStatesManager = Engine.getInputStatesManager();
            if (inputStatesManager.getTextInputTarget() == textInputDisplay.getStringBuffer()) {
                inputStatesManager.closeTextInput();
            }
            else {
                inputStatesManager.addTextInput(textInputDisplay.getStringBuffer());
            }
        };
        return new MenuItem(visuals, hitbox, onClick);
    }
}