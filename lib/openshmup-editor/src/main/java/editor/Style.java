package editor;

import engine.scene.visual.style.RoundedRectangleStyle;
import engine.scene.visual.style.TextStyle;
import types.RGBAValue;
import types.Vec2D;

import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static types.RGBAValue.SOLID_BLACK;
import static types.RGBAValue.SOLID_WHITE;

final public class Style {

    private Style() {}

    public static class Color {

        private Color() {}

        final public static RGBAValue blue1 = new RGBAValue(0.8f, 0.95f, 1.0f, 1.0f);

        final public static RGBAValue grey1 = new RGBAValue(0.9f, 0.9f, 0.9f, 1.0f);

        final public static RGBAValue menuButtonColor = SOLID_WHITE;

        final public static RGBAValue menuButtonBorderColor = SOLID_BLACK;

        final public static RGBAValue titleColor = SOLID_BLACK;

        final public static RGBAValue menuBackgroundColor = blue1;
    }

    public static class Text {

        private Text() {}

        final public static Path menuFontPath = get("lib/openshmup-engine/src/main/resources/fonts/Inter_28pt-Regular.ttf");

        final public static TextStyle menuButtonLabelStyle = new TextStyle(menuFontPath, SOLID_BLACK, 20f);

        final public static TextStyle menuTextStyle = new TextStyle(menuFontPath, SOLID_BLACK, 16f);

        final public static TextStyle menuScreenTitleStyle = new TextStyle(menuFontPath, SOLID_BLACK, 24f);
    }

    final public static Vec2D buttonSize = new Vec2D(600f, 150f);

    final public static float menuButtonRoundingRadius = 10f;

    final public static float menuButtonBorderWidth = 2f;

    final public static RoundedRectangleStyle menuButtonStyle1 = new RoundedRectangleStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor);

    final public static RoundedRectangleStyle menuButtonStyle2 = new RoundedRectangleStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.blue1, Color.menuButtonBorderColor);

    final public static RoundedRectangleStyle menuButtonStyle3 = new RoundedRectangleStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.grey1, Color.menuButtonBorderColor);

    final public static RoundedRectangleStyle editionSelectorSelected = new RoundedRectangleStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor);

    final public static RoundedRectangleStyle editionSelectorUnselected = new RoundedRectangleStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.blue1, Color.menuButtonBorderColor);
}
