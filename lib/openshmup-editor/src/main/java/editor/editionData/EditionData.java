package editor.editionData;

import editor.EditPanels;
import engine.scene.menu.MenuScreen;

public sealed interface EditionData permits AnimationEditionData, ScrollingImageEditionData {

    static MenuScreen createPanel(EditionData editionData) {
        return switch (editionData) {
            case AnimationEditionData animationData -> EditPanels.AnimationEditPanel(animationData);
            case ScrollingImageEditionData scrollingImageData -> EditPanels.ScrollingImageEditPanel(scrollingImageData);
        };
    }
}
