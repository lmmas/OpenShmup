package editor;

import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.style.TextStyle;

import static engine.GlobalVars.Paths.debugFont;

public class Style {

    final public static Vec2D buttonSize = new Vec2D(600f, 150f);

    final public static RGBAValue menuButtonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);

    final public static RGBAValue menuButtonBorderColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);

    final public static float menuButtonRoundingRadius = 0.1f;

    final public static float menuButtonBorderWidth = 0.02f;

    final public static RGBAValue menuButtonTextColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);

    final public static TextStyle menuButtonLabelStyle = new TextStyle(debugFont, menuButtonTextColor, 17.0f);

}
