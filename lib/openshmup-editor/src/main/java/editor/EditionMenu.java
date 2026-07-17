package editor;

import edition.EditionData;
import edition.GameEditionData;
import editor.fieldNode.EditionDataFieldNode;
import editor.fieldNode.FieldNode;
import editor.fieldNode.ListFields;
import engine.Engine;
import engine.GlobalVars;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.widget.ActionButton;
import engine.menu.widget.SelectorButtons;
import engine.menu.widget.Widgets;
import engine.scene.Scene;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import json.JsonDataWriter;
import types.RGBAValue;
import types.Reference;
import types.Vec2D;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
        ActionButton saveButton = Widgets.RoundedRectangleButton(1, new Vec2D(300, 50), new Vec2D(1750, 150), menuButtonStyle1, "Save", () -> saveGame(gameData));
        mainScreen.addWidget(saveButton);
        ActionButton launchGameButton = Widgets.RoundedRectangleButton(1, new Vec2D(300, 50), new Vec2D(1750, 220), menuButtonStyle1, "Launch game", () -> launchGame(gameData));
        mainScreen.addWidget(launchGameButton);

        ArrayList<EditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        Vec2D listStartPosition = new Vec2D(Engine.getNativeWidth() / 2.0f, 850f);
        ListFields visualListFields = new ListFields(EditionData.Category.VISUAL, visualEditionDataList, listStartPosition, true);
        visualListFields.setMenu(menu);

        ArrayList<EditionData> trajectoryEditionDataList = gameData.getTrajectoryEditionDataList();

        ListFields trajectoryListFields = new ListFields(EditionData.Category.TRAJECTORY, trajectoryEditionDataList, listStartPosition, true);
        trajectoryListFields.setMenu(menu);

        ArrayList<EditionData> entityEditionDataList = gameData.getEntityEditionDataList();
        ListFields entityListFields = new ListFields(EditionData.Category.ENTITY, entityEditionDataList, listStartPosition, true);
        entityListFields.setMenu(menu);

        ArrayList<EditionData> timelineEditionDataList = gameData.getTimelineDataList();
        ListFields timelineSpawnInfoList = new ListFields(EditionData.Category.SPAWN_INFO, timelineEditionDataList, listStartPosition, true);
        timelineSpawnInfoList.setMenu(menu);

        List<String> labels = List.of("Visuals", "Trajectories", "Entities", "Timeline");
        Vec2D selectorButtonStride = new Vec2D(250f, 0f);
        Vec2D selectorButtonSize = new Vec2D(200, 50f);
        Vec2D selectorStartPosition = new Vec2D(Engine.getNativeWidth() * 0.5f - 1.5f * selectorButtonStride.x, 950);
        final Reference<FieldNode> currentNode = new Reference<>(null);
        BiConsumer<SelectorButtons, Integer> onChange = (buttons, newValue) -> {

            if (currentNode.get() != null) {
                currentNode.get().setActive(false);
            }
            FieldNode node = switch (newValue) {
                case 0 -> visualListFields;
                case 1 -> trajectoryListFields;
                case 2 -> entityListFields;
                case 3 -> timelineSpawnInfoList;
                default -> {
                    assert false : "incorrect value";
                    yield null;
                }
            };
            node.setActive(true);
            currentNode.set(node);
        };
        SelectorButtons categorySelector = EditorSelector(1, 4, selectorButtonSize, selectorStartPosition, selectorButtonStride, labels, onChange, 0);
        mainScreen.addWidget(categorySelector);
        currentNode.set(visualListFields);
        menu.addMenuScreen(mainScreen);
        visualListFields.setActive(true);

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
        node.setActive(true);

        return editPanel;
    }

    private static void saveGame(GameEditionData gameData) {
        JsonDataWriter writer = new JsonDataWriter();
        writer.saveToJson(gameData);
        Path gameJAR = GlobalVars.Paths.rootFolderAbsolutePath.resolve("lib/openshmup-gameExecutable/target/openshmup-gameExecutable-1.0-SNAPSHOT.jar");
        Path gameFolder = gameData.paths.gameFolder;
        Path targetPath = gameFolder.resolve(gameFolder.getFileName() + ".jar");
        try {
            Files.copy(gameJAR, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy to " + gameFolder + ": " + e.getMessage());
        }
    }

    private static void launchGame(GameEditionData gameData) {
        Path enginePath = GlobalVars.Paths.rootFolderAbsolutePath.resolve("lib/openshmup-engine/target/openshmup-engine-1.0-SNAPSHOT.jar");

        ProcessBuilder pb = new ProcessBuilder(
            "java", "-jar", enginePath.toAbsolutePath().toString());
        try {
            Process process = pb.start();
            try (ObjectOutputStream out = new ObjectOutputStream(process.getOutputStream())) {
                out.writeObject(gameData);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
