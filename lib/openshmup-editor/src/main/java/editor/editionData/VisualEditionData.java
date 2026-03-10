package editor.editionData;

public sealed interface VisualEditionData extends EditionData permits AnimationEditionData, ScrollingImageEditionData {

    int getId();
}
