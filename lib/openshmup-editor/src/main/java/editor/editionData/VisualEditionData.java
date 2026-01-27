package editor.editionData;

public sealed interface VisualEditionData permits AnimationEditionData, ScrollingImageEditionData {

    int getId();
}
