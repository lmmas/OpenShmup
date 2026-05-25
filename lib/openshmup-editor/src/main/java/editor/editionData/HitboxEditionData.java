package editor.editionData;

public sealed interface HitboxEditionData extends EditionData permits SimpleRectangleHitboxEditionData, CompositeHitboxEditionData {

    static String getType(HitboxEditionData data) {
        return switch (data) {
            case SimpleRectangleHitboxEditionData ignored -> "simpleRectangle";
            case CompositeHitboxEditionData ignored -> "composite";
        };
    }
}
