package editor;

import editor.attribute.Attribute;
import editor.editionData.*;
import engine.Engine;
import engine.menu.MenuItem;
import engine.menu.MenuItems;
import engine.menu.MenuScreen;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;

import java.util.List;

import static editor.Style.*;

final public class EditPanels {

    private static MenuScreen BaseEditPanel(String editPanelTitle) {
        MenuScreen panel = new MenuScreen(3);

        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(0, 1500f, 900f, Engine.getNativeWidth() / 2, Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor.r, menuButtonColor.g, menuButtonColor.b, menuButtonColor.a, menuButtonBorderColor.r, menuButtonBorderColor.g, menuButtonColor.b, menuButtonBorderColor.a);
        panel.addVisual(backgroundRectangle);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        MenuItem closeButton = MenuItems.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, (() -> Engine.getCurrentMenu().removeMenuScreen(panel)));
        panel.addItem(closeButton);

        TextDisplay panelTitle = new TextDisplay(1, false, (float) Engine.getNativeWidth() / 2, 950f, editPanelTitle, menuButtonLabelStyle, TextAlignment.CENTER);
        panel.addVisual(panelTitle);

        return panel;
    }

    public static MenuScreen AnimationEditPanel(AnimationEditionData animationData) {
        MenuScreen panel = BaseEditPanel("Edit Animation");

        List<Attribute> attributesList = List.of(animationData.getIdAttribute(), animationData.getLayer(), animationData.getSize(), animationData.getFramePeriodSeconds(), animationData.getLooping());
        for (int i = 0; i < attributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(1, false, 300f, 700f - 30f * i, attributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }

        TextDisplay spritesheetInfoSectionTitle = new TextDisplay(1, false, 300f, 530f, "Spritesheet info:", menuButtonLabelStyle, TextAlignment.LEFT);
        panel.addVisual(spritesheetInfoSectionTitle);

        AnimationEditionData.SpritesheetInfoData spritesheetInfoData = animationData.getSpritesheetInfo();
        List<Attribute> spriteSheetAttributesList = List.of(spritesheetInfoData.getFileName(), spritesheetInfoData.getFrameCount(), spritesheetInfoData.getFrameSize(), spritesheetInfoData.getStartPosition(), spritesheetInfoData.getStride());
        for (int i = 0; i < spriteSheetAttributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(1, false, 330f, 490f - 30f * i, spriteSheetAttributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }


        return panel;
    }

    public static MenuScreen ScrollingImageEditPanel(ScrollingImageEditionData scrollingImageData) {
        MenuScreen panel = BaseEditPanel("Edit Scrolling Image");

        List<Attribute> attributesList = List.of(scrollingImageData.getIdAttribute(), scrollingImageData.getLayer(), scrollingImageData.getSize(), scrollingImageData.getFileName());

        for (int i = 0; i < attributesList.size(); i++) {
            TextDisplay attributeText = new TextDisplay(1, false, 300f, 700f - 30f * i, attributesList.get(i).toString(), menuButtonLabelStyle, TextAlignment.LEFT);
            panel.addVisual(attributeText);
        }

        return panel;
    }

    public static MenuScreen SimpleRectangleHitboxEditPanel(SimpleRectangleHitboxEditionData simpleRectangleHitboxData) {
        MenuScreen panel = BaseEditPanel("Edit Hitbox");
        return panel;
    }

    public static MenuScreen CompositeHitboxEditPanel(CompositeHitboxEditionData compositeHitboxData) {
        MenuScreen panel = BaseEditPanel("Edit Hitbox");
        return panel;
    }

    public static MenuScreen FixedTrajectoryEditPanel(FixedTrajectoryEditionData fixedTrajectoryData) {
        MenuScreen panel = BaseEditPanel("Edit Trajectory");
        return panel;
    }

    public static MenuScreen PlayerControlledTrajectoryEditPanel(PlayerControlledTrajectoryEditionData playerControlledTrajectoryData) {
        MenuScreen panel = BaseEditPanel("Edit Trajectory");
        return panel;
    }

    public static MenuScreen ShotEditPanel(ShotEditionData shotData) {
        MenuScreen panel = BaseEditPanel("Edit Shot");
        return panel;
    }

    public static MenuScreen ShipEditPanel(ShipEditionData shipData) {
        MenuScreen panel = BaseEditPanel("Edit Ship");
        return panel;
    }

    public static MenuScreen ProjectileEditPanel(ProjectileEditionData projectileData) {
        MenuScreen panel = BaseEditPanel("Edit Projectile");
        return panel;
    }

    public static MenuScreen DisplaySpawnInfoEditPanel(DisplaySpawnInfoEditionData displaySpawnInfoData) {
        MenuScreen panel = BaseEditPanel("Edit Display Spawn Info");
        return panel;
    }

    public static MenuScreen EntitySpawnInfoEditPanel(EntitySpawnInfoEditionData entitySpawnInfoData) {
        MenuScreen panel = BaseEditPanel("Edit Entity Spawn Info");
        return panel;
    }

    public static MenuScreen createPanel(EditionData editionData) {
        return switch (editionData) {
            case AnimationEditionData animationData -> AnimationEditPanel(animationData);
            case ScrollingImageEditionData scrollingImageData -> ScrollingImageEditPanel(scrollingImageData);
            case SimpleRectangleHitboxEditionData simpleRectangleHitboxData ->
                SimpleRectangleHitboxEditPanel(simpleRectangleHitboxData);
            case CompositeHitboxEditionData compositeHitboxData -> CompositeHitboxEditPanel(compositeHitboxData);
            case FixedTrajectoryEditionData fixedTrajectoryData -> FixedTrajectoryEditPanel(fixedTrajectoryData);
            case PlayerControlledTrajectoryEditionData playerControlledTrajectoryData ->
                PlayerControlledTrajectoryEditPanel(playerControlledTrajectoryData);
            case ShotEditionData shotData -> ShotEditPanel(shotData);
            case ShipEditionData shipData -> ShipEditPanel(shipData);
            case ProjectileEditionData projectileData -> ProjectileEditPanel(projectileData);
            case DisplaySpawnInfoEditionData displaySpawnInfoData -> DisplaySpawnInfoEditPanel(displaySpawnInfoData);
            case EntitySpawnInfoEditionData entitySpawnInfoEditionData ->
                EntitySpawnInfoEditPanel(entitySpawnInfoEditionData);
        };
    }
}
