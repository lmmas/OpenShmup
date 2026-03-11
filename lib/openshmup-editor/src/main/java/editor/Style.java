package editor;

import engine.scene.visual.style.RoundedRectangleButtonStyle;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import static engine.GlobalVars.Paths.debugFont;

final public class Style {

    private Style() {}

    public static class Color {

        private Color() {}

        final public static RGBAValue menuButtonColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

        final public static RGBAValue menuButtonBorderColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);

        final public static RGBAValue menuButtonTextColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);

        final public static RGBAValue titleColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);

        final public static RGBAValue menuBackgroundColor = new RGBAValue(0.8f, 0.95f, 1.0f, 1.0f);
    }

    public static class Text {

        private Text() {}

        final public static TextStyle menuButtonLabelStyle = new TextStyle(debugFont, Color.menuButtonTextColor, 17.0f);

        final public static TextStyle menuScreenTitleStyle = new TextStyle(debugFont, Color.titleColor, 20);
    }

    final public static Vec2D buttonSize = new Vec2D(600f, 150f);

    final public static float menuButtonRoundingRadius = 10f;

    final public static float menuButtonBorderWidth = 4f;

    final public static RoundedRectangleButtonStyle menuButtonStyle = new RoundedRectangleButtonStyle(menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor, Text.menuButtonLabelStyle);

}
