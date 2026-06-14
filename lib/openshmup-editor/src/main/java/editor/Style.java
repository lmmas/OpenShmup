package editor;

import engine.scene.visual.style.RoundedRectangleButtonStyle;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.nio.file.Path;

import static engine.types.RGBAValue.SOLID_BLACK;
import static engine.types.RGBAValue.SOLID_WHITE;
import static java.nio.file.Paths.get;

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

    final public static RoundedRectangleButtonStyle menuButtonStyle1 = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor, Text.menuButtonLabelStyle);

    final public static RoundedRectangleButtonStyle menuButtonStyle2 = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.blue1, Color.menuButtonBorderColor, Text.menuButtonLabelStyle);

    final public static RoundedRectangleButtonStyle menuButtonStyle3 = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.grey1, Color.menuButtonBorderColor, Text.menuButtonLabelStyle);

    final public static RoundedRectangleButtonStyle editionSelectorSelected = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor, Text.menuTextStyle);

    final public static RoundedRectangleButtonStyle editionSelectorUnselected = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.blue1, Color.menuButtonBorderColor, Text.menuTextStyle);

}
