package editor;

import editor.attribute.Attribute;
import editor.editionData.AnimationEditionData;
import editor.editionData.ScrollingImageEditionData;
import engine.Engine;
import engine.menu.MenuItem;
import engine.menu.MenuItems;
import engine.menu.MenuScreen;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;
import engine.visual.SceneVisual;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;

import java.util.List;

import static editor.Style.*;

final public class EditPanels {

    private static MenuScreen BaseEditPanel() {
        MenuScreen panel = new MenuScreen(3);

        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(3, 1500f, 900f, Engine.getNativeWidth() / 2, Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor.r, menuButtonColor.g, menuButtonColor.b, menuButtonColor.a, menuButtonBorderColor.r, menuButtonBorderColor.g, menuButtonColor.b, menuButtonBorderColor.a);
        panel.addVisual(backgroundRectangle);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        MenuItem closeButton = MenuItems.RoundedRectangleButton(4, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, (() -> Engine.getCurrentMenu().removeMenuScreen(panel)));
        panel.addItem(closeButton);

        return panel;
    }

    public static MenuScreen AnimationEditPanel(AnimationEditionData animationData) {
        MenuScreen panel = BaseEditPanel();

        TextDisplay panelTitle = new TextDisplay(4, false, (float) Engine.getNativeWidth() / 2, 950f, "Edit Animation", menuButtonLabelStyle, TextAlignment.CENTER);
        panel.addVisual(panelTitle);

        List<Attribute> attributesList = List.of(animationData.getId(), animationData.getLayer(), animationData.getSize(), animationData.getFramePeriodSeconds(), animationData.getLooping());
        for (int i = 0; i < attributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(4, false, 300f, 700f - 30f * i, attributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }

        TextDisplay spritesheetInfoSectionTitle = new TextDisplay(4, false, 300f, 530f, "Spritesheet info:", menuButtonLabelStyle, TextAlignment.LEFT);
        panel.addVisual(spritesheetInfoSectionTitle);

        AnimationEditionData.AnimationInfoData animationInfoData = animationData.getAnimationInfo();
        List<Attribute> spriteSheetAttributesList = List.of(animationInfoData.getFileName(), animationInfoData.getFrameCount(), animationInfoData.getFrameSize(), animationInfoData.getStartPosition(), animationInfoData.getStride());
        for (int i = 0; i < spriteSheetAttributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(4, false, 330f, 490f - 30f * i, spriteSheetAttributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }


        return panel;
    }

    public static MenuScreen ScrollingImageEditPanel(ScrollingImageEditionData scrollingImageData) {
        MenuScreen panel = BaseEditPanel();

        TextDisplay panelTitle = new TextDisplay(4, false, (float) Engine.getNativeWidth() / 2, 950f, "Edit Scrolling Image", menuButtonLabelStyle, TextAlignment.CENTER);
        panel.addVisual(panelTitle);

        List<Attribute> attributesList = List.of(scrollingImageData.getId(), scrollingImageData.getLayer(), scrollingImageData.getSize(), scrollingImageData.getFileName());

        for (int i = 0; i < attributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(4, false, 300f, 700f - 30f * i, attributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }

        return panel;
    }
}
