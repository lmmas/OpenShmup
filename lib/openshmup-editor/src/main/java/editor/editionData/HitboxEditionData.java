package editor.editionData;

public sealed interface HitboxEditionData extends EditionData permits SimpleRectangleHitboxEditionData, CompositeHitboxEditionData {
}
