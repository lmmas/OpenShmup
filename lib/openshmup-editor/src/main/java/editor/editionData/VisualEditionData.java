package editor.editionData;

public sealed interface VisualEditionData extends EditionData permits AnimationEditionData, ScrollingImageEditionData {

    int getId();

    static String getType(VisualEditionData data) {
        return switch (data) {
            case AnimationEditionData ignored -> "animation";
            case ScrollingImageEditionData ignored -> "scrollingImage";
        };
    }
}
