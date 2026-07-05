package editor;

import editor.fieldNode.EditionDataFieldNode;
import engine.Engine;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.MenuScreen;
import engine.menu.widget.ActionButton;
import engine.menu.widget.SelectorButtons;
import engine.menu.widget.Widget;
import engine.menu.widget.Widgets;
import engine.scene.Scene;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.RGBAValue;
import engine.types.Reference;
import engine.types.Vec2D;
import json.GameEditionData;
import json.editionData.EditionData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static editor.Menus.MainMenu;
import static editor.Style.Color.menuBackgroundColor;
import static editor.Style.*;
import static editor.Widgets.EditorSelector;

final public class EditionMenu {

    private EditionMenu() {}

    public static Menu EditionMenu(GameEditionData gameData) {
        Menu menu = new Menu();
        MenuScreen mainScreen = new MenuScreen(0);

        SceneVisual menuBackground = new ScreenFilter(0, menuBackgroundColor);
        mainScreen.addVisual(menuBackground);

        TextDisplay screenTitle = new TextDisplay(1, false, new Vec2D((float) Engine.getNativeWidth() / 2, 1020f), "Edit Game", Text.menuScreenTitleStyle, TextAlignment.CENTER);
        mainScreen.addVisual(screenTitle);
        Runnable returnToMainMenu = () -> {
            Engine.switchCurrentScene(new Scene());
            Engine.switchCurrentMenu(MainMenu());
        };
        ActionButton returnToMainMenuButton = Widgets.RoundedRectangleButton(1, new Vec2D(300, 50), new Vec2D(1750, 75), menuButtonStyle1, "Return to main menu", returnToMainMenu);
        mainScreen.addWidget(returnToMainMenuButton);

        Vec2D listButtonSize = new Vec2D(300f, 50f);
        float listButtonSpacing = 15f;

        List<EditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        ArrayList<Widget> visualListItems = new ArrayList<>(visualEditionDataList.size());
        Vec2D fieldsStartPosition = new Vec2D(120f, 830f);
        for (int i = 0; i < visualEditionDataList.size(); i++) {
            var visualData = visualEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> {
                EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(visualEditionDataList.get(finalI), fieldsStartPosition);
                Runnable onApply = () -> applyChangesToList(node, visualEditionDataList, finalI);
                openEditPanel(menu, 4, node, onApply, () -> {});
            };
            String typeString = "";
            if (visualData.getType() == EditionData.Types.Visual.animation) {
                typeString = "Animation";
            }
            if (visualData.getType() == EditionData.Types.Visual.scrollingImage) {
                typeString = "Scrolling image";
            }
            String menuButtonLabel = EditionData.getVisualId(visualData) + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = Widgets.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            visualListItems.add(visualSelectButton);
        }
        MenuElementGroup visualList = new MenuElementGroup(visualListItems, List.of());

        List<EditionData> trajectoryEditionDataList = gameData.getTrajectoryEditionDataList();
        ArrayList<Widget> trajectoryListItems = new ArrayList<>(trajectoryEditionDataList.size());
        for (int i = 0; i < trajectoryEditionDataList.size(); i++) {
            var trajectoryData = trajectoryEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> {
                EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(trajectoryEditionDataList.get(finalI), fieldsStartPosition);
                Runnable onApply = () -> applyChangesToList(node, trajectoryEditionDataList, finalI);
                openEditPanel(menu, 4, node, onApply, () -> {});
            };
            String typeString = "";
            if (trajectoryData.getType() == EditionData.Types.Trajectory.player) {
                typeString = "Player Controlled";
            }
            if (trajectoryData.getType() == EditionData.Types.Trajectory.fixed) {
                typeString = "Fixed";
            }
            String menuButtonLabel = EditionData.getTrajectoryId(trajectoryData) + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = Widgets.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            trajectoryListItems.add(visualSelectButton);
        }
        MenuElementGroup trajectoryList = new MenuElementGroup(trajectoryListItems, List.of());

        List<EditionData> entityEditionDataList = gameData.getEntityEditionDataList();
        List<Widget> entityListItems = new ArrayList<>(entityEditionDataList.size());
        for (int i = 0; i < entityEditionDataList.size(); i++) {
            var entityData = entityEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> {
                EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(entityEditionDataList.get(finalI), fieldsStartPosition);
                Runnable onApply = () -> applyChangesToList(node, entityEditionDataList, finalI);
                openEditPanel(menu, 4, node, onApply, () -> {});
            };
            String typeString = "";
            if (entityData.getType() == EditionData.Types.Entity.ship) {
                typeString = "Ship";
            }
            if (entityData.getType() == EditionData.Types.Entity.projectile) {
                typeString = "Projectile";
            }
            String menuButtonLabel = EditionData.getEntityId(entityData) + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = Widgets.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            entityListItems.add(visualSelectButton);
        }
        MenuElementGroup entityList = new MenuElementGroup(entityListItems, List.of());

        MenuElementGroup timelineSpawnList = new MenuElementGroup();

        List<String> labels = List.of("Visuals", "Trajectories", "Entities", "Timeline");
        Vec2D selectorButtonStride = new Vec2D(250f, 0f);
        Vec2D selectorButtonSize = new Vec2D(200, 50f);
        Vec2D selectorStartPosition = new Vec2D(Engine.getNativeWidth() * 0.5f - 1.5f * selectorButtonStride.x, 950);
        final Reference<MenuElementGroup> currentList = new Reference<>(null);
        BiConsumer<SelectorButtons, Integer> onChange = (buttons, newValue) -> {

            if (currentList.get() != null) {
                menu.removeFromCurrentScreen(currentList.get());
            }
            MenuElementGroup newCurrentList = switch (newValue) {
                case 0 -> visualList;
                case 1 -> trajectoryList;
                case 2 -> entityList;
                case 3 -> timelineSpawnList;
                default -> throw new IllegalArgumentException("incorrect selected value");
            };
            menu.addToCurrentScreen(newCurrentList);
            currentList.set(newCurrentList);
        };
        SelectorButtons categorySelector = EditorSelector(1, 4, selectorButtonSize, selectorStartPosition, selectorButtonStride, labels, onChange, 0);
        mainScreen.addWidget(categorySelector);
        currentList.set(visualList);
        mainScreen.addElementGroup(currentList.get());
        menu.addMenuScreen(mainScreen);

        return menu;
    }

    public static MenuScreen openEditPanel(Menu menu, int layer, EditionDataFieldNode node, Runnable onApply, Runnable onClose) {
        EditionData editionData = node.getEditionData();
        assert editionData.getCategory() == EditionData.Category.VISUAL || editionData.getCategory() == EditionData.Category.TRAJECTORY || editionData.getCategory() == EditionData.Category.ENTITY || editionData.getCategory() == EditionData.Category.SPAWN_INFO || editionData.getType() == EditionData.Types.shot : "Incorrect editionData type: " + editionData.getType().name();
        MenuScreen editPanel = new MenuScreen(layer);
        SceneVisual backgroundColor = new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.5f));
        editPanel.addVisual(backgroundColor);
        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(1, new Vec2D(1800f, 950f), Engine.getNativeResolution().scalar(0.5f), menuButtonRoundingRadius, menuButtonBorderWidth, RGBAValue.SOLID_WHITE, RGBAValue.SOLID_BLACK);
        editPanel.addVisual(backgroundRectangle);

        String panelTitleString = switch (editionData.getCategory()) {
            case VISUAL -> "Edit Visual";
            case TRAJECTORY -> "Edit Trajectory";
            case ENTITY -> "Edit Entity";
            case SPAWN_INFO -> "Edit Timeline entry";
            case NONE -> switch (editionData.getType()) {
                case EditionData.Types.shot -> "Edit Shot";
                default -> "";
            };
            default -> "";
        };
        TextDisplay panelTitle = new TextDisplay(2, false, new Vec2D((float) Engine.getNativeWidth() / 2, 950f), panelTitleString, Text.menuButtonLabelStyle, TextAlignment.CENTER);
        editPanel.addVisual(panelTitle);

        node.setMenu(menu);
        node.setActive(true);

        Vec2D buttonSize = new Vec2D(150, 50);
        Vec2D applyButtonPosition = new Vec2D(1465, 125);
        ActionButton applyButton = Widgets.RoundedRectangleButton(3, buttonSize, applyButtonPosition, menuButtonStyle2, "Apply", onApply);

        Vec2D closeButtonPosition = new Vec2D(1625, 125);
        Runnable closeButtonAction = () -> {
            menu.removeMenuScreen(editPanel);
            onClose.run();
        };
        ActionButton closeButton = Widgets.RoundedRectangleButton(3, buttonSize, closeButtonPosition, menuButtonStyle2, "Close", closeButtonAction);
        editPanel.addWidget(closeButton);
        editPanel.addWidget(applyButton);
        menu.addMenuScreen(editPanel);

        return editPanel;
    }

    private static void applyChangesToList(EditionDataFieldNode node, List<EditionData> dataList, int index) {
        node.applyChanges();
        EditionData selectedEditionData = node.getEditionData();
        dataList.set(index, selectedEditionData);
    }
}
