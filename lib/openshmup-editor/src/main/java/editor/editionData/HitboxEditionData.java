package editor.editionData;

public sealed interface HitboxEditionData extends EditionData permits RectangleHitboxEditionData, CustomHitboxEditionData {

    static String getType(HitboxEditionData data) {
        return switch (data) {
            case RectangleHitboxEditionData ignored -> "simpleRectangle";
            case CustomHitboxEditionData ignored -> "composite";
        };
    }
}
