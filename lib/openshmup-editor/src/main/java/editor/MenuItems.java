package editor;

import engine.menu.item.BooleanField;
import engine.menu.item.SelectorButtons;
import engine.menu.item.TextField;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.ImageDisplay;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;

import static editor.Style.Text.menuButtonLabelStyle;
import static editor.Style.menuButtonStyle1;
import static editor.Style.menuButtonStyle3;
import static engine.Engine.assetManager;

final public class MenuItems {

    private MenuItems() {}

    final private static Path checkboxTexturePath = Paths.get("lib/openshmup-editor/src/main/resources/textures/checkbox.png");

    public static BooleanField Checkbox(int layer, Vec2D position, boolean startingState) {
        Vec2D size = new Vec2D(32.0f, 32.0f);
        float boxRoundingRadius = 3.0f;
        float boxBorderWidth = 1.5f;
        RGBAValue boxColor = RGBAValue.SOLID_WHITE;
        RGBAValue boxBorderColor = RGBAValue.SOLID_BLACK;
        BorderedRoundedRectangle box = new BorderedRoundedRectangle(layer, size, position, boxRoundingRadius, boxBorderWidth, boxColor, boxBorderColor);
        ImageDisplay checkMark = new ImageDisplay(layer + 1, assetManager.getTexture(checkboxTexturePath), size, position);
        return new BooleanField(size, position, checkMark, List.of(box), startingState);
    }

    public static TextField EditorTextField(int layer, Vec2D position, float fieldWidthPixels, String startingText) {
        Vec2D size = new Vec2D(fieldWidthPixels, 30f);
        float roundingRadius = 5f;
        float borderWidth = 2f;
        RGBAValue rectangleColor = RGBAValue.SOLID_WHITE;
        RGBAValue borderColor = RGBAValue.SOLID_BLACK;
        BorderedRoundedRectangle borderedRoundedRectangle = new BorderedRoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor);
        return new TextField(layer + 1, size, position, menuButtonLabelStyle, List.of(borderedRoundedRectangle), startingText);
    }

    public static SelectorButtons EditorSelector(int layer, int buttonCount, Vec2D size, Vec2D startPosition, Vec2D stride, List<String> labels, BiConsumer<SelectorButtons, Integer> onChange) {
        return engine.menu.item.MenuItems.StandardSelectorButtons(layer, buttonCount, size, startPosition, stride, menuButtonStyle1, menuButtonStyle3, labels, onChange);
    }


}
