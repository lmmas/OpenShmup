package engine.scene.visual.effects;

import engine.types.RGBAValue;

public record ColorEffect(
    RGBAValue colorCoefs,
    RGBAValue addedColor
) {

    public static ColorEffect ColorFilter(RGBAValue filterColor) {
        RGBAValue colorCoefs = new RGBAValue(1.0f - filterColor.a, 1.0f - filterColor.a, 1.0f - filterColor.a, 1.0f);
        RGBAValue addedColor = new RGBAValue(filterColor.r * filterColor.a, filterColor.g * filterColor.a, filterColor.b * filterColor.a, 0.0f);
        return new ColorEffect(colorCoefs, addedColor);
    }

    public static ColorEffect TransparencyEffect(float alpha) {
        RGBAValue colorCoefs = new RGBAValue(1.0f, 1.0f, 1.0f, alpha);
        RGBAValue addedColor = new RGBAValue(0.0f, 0.0f, 0.0f, 0.0f);
        return new ColorEffect(colorCoefs, addedColor);
    }

    public static ColorEffect Invisibility() {
        return ColorEffect.TransparencyEffect(0.0f);
    }
}
