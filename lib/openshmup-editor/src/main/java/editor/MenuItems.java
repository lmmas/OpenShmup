package editor;

import engine.menu.item.BooleanField;
import engine.menu.item.TextField;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.ImageDisplay;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static editor.Style.menuButtonLabelStyle;
import static engine.Engine.assetManager;

final public class MenuItems {

    private MenuItems() {}

    final private static Path checkboxTexturePath = Paths.get("lib/openshmup-editor/src/main/resources/textures/checkbox.png");

    public static BooleanField Checkbox(int layer, Vec2D position, boolean startingState) {
        Vec2D size = new Vec2D(32.0f, 32.0f);
        float boxRoundingRadius = 3.0f;
        float boxBorderWidth = 1.5f;
        RGBAValue boxColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        RGBAValue boxBorderColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        BorderedRoundedRectangle box = new BorderedRoundedRectangle(layer, size, position, boxRoundingRadius, boxBorderWidth, boxColor, boxBorderColor);
        ImageDisplay checkMark = new ImageDisplay(layer + 1, assetManager.getTexture(checkboxTexturePath), size, position);
        return new BooleanField(size, position, checkMark, List.of(box), startingState);
    }

    public static TextField EditorTextField(int layer, Vec2D position, String startingText) {
        Vec2D size = new Vec2D(150f, 30f);
        float roundingRadius = 5f;
        float borderWidth = 2f;
        RGBAValue rectangleColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        RGBAValue borderColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        BorderedRoundedRectangle borderedRoundedRectangle = new BorderedRoundedRectangle(layer, size, position, roundingRadius, borderWidth, rectangleColor, borderColor);
        return new TextField(layer + 1, size, position, menuButtonLabelStyle, List.of(borderedRoundedRectangle), startingText);
    }
}
