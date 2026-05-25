package editor;

import editor.editionData.*;
import editor.fieldNode.EditionDataTypeSelect;
import editor.fieldNode.FieldNode;
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
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static editor.Menus.MainMenu;
import static editor.Style.Color.menuBackgroundColor;
import static editor.Style.*;
import static editor.Widgets.EditorSelector;

final public class EditionMenu {

    @Getter final private Menu menu;

    final private EditorGameDataManager gameData;

    private MenuScreen editPanel;

    private FieldNode editPanelRoot;

    public EditionMenu(EditorGameDataManager gameData) {
        this.gameData = gameData;
        this.menu = new Menu();
        this.editPanel = null;
        this.editPanelRoot = null;
        this.buildMainScreen();
    }

    private void buildMainScreen() {
        MenuScreen mainScreen = new MenuScreen(0);

        SceneVisual menuBackground = new ScreenFilter(0, menuBackgroundColor);
        mainScreen.addVisual(menuBackground);

        TextDisplay screenTitle = new TextDisplay(1, false, new Vec2D((float) Engine.getNativeWidth() / 2, 1020f), "Edit Game", Style.Text.menuScreenTitleStyle, TextAlignment.CENTER);
        mainScreen.addVisual(screenTitle);
        Runnable returnToMainMenu = () -> {
            Engine.switchCurrentScene(new Scene());
            Engine.switchCurrentMenu(MainMenu());
        };
        ActionButton returnToMainMenuButton = Widgets.RoundedRectangleButton(1, new Vec2D(300, 50), new Vec2D(1750, 75), menuButtonStyle1, "Return to main menu", returnToMainMenu);
        mainScreen.addWidget(returnToMainMenuButton);

        Vec2D listButtonSize = new Vec2D(300f, 50f);
        float listButtonSpacing = 15f;

        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        ArrayList<Widget> visualListItems = new ArrayList<>(visualEditionDataList.size());
        for (int i = 0; i < visualEditionDataList.size(); i++) {
            var visualData = visualEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> this.openEditPanel(visualEditionDataList, finalI);
            String typeString = "";
            if (visualData instanceof AnimationEditionData) {
                typeString = "Animation";
            }
            if (visualData instanceof ScrollingImageEditionData) {
                typeString = "Scrolling image";
            }
            String menuButtonLabel = visualData.getId() + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = Widgets.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            visualListItems.add(visualSelectButton);
        }
        MenuElementGroup visualList = new MenuElementGroup(visualListItems, List.of());

        List<TrajectoryEditionData> trajectoryEditionDataList = gameData.getTrajectoryEditionDataList();
        ArrayList<Widget> trajectoryListItems = new ArrayList<>(trajectoryEditionDataList.size());
        for (int i = 0; i < trajectoryEditionDataList.size(); i++) {
            var trajectoryData = trajectoryEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> this.openEditPanel(trajectoryEditionDataList, finalI);
            String typeString = "";
            if (trajectoryData instanceof PlayerControlledTrajectoryEditionData) {
                typeString = "Player Controlled";
            }
            if (trajectoryData instanceof FixedTrajectoryEditionData) {
                typeString = "Fixed";
            }
            String menuButtonLabel = trajectoryData.getId() + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = Widgets.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            trajectoryListItems.add(visualSelectButton);
        }
        MenuElementGroup trajectoryList = new MenuElementGroup(trajectoryListItems, List.of());

        List<EntityEditionData> entityEditionDataList = gameData.getEntityEditionDataList();
        List<Widget> entityListItems = new ArrayList<>(entityEditionDataList.size());
        for (int i = 0; i < entityEditionDataList.size(); i++) {
            var entityData = entityEditionDataList.get(i);
            int finalI = i;
            Runnable onClick = () -> this.openEditPanel(entityEditionDataList, finalI);
            String typeString = "";
            if (entityData instanceof ShipEditionData) {
                typeString = "Ship";
            }
            if (entityData instanceof ProjectileEditionData) {
                typeString = "Projectile";
            }
            String menuButtonLabel = entityData.getId() + ": " + typeString;

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
    }

    private <D extends EditionData> void openEditPanel(List<D> dataList, int index) {
        D editionData = dataList.get(index);
        this.editPanel = new MenuScreen(4);
        SceneVisual backgroundColor = new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.5f));
        this.editPanel.addVisual(backgroundColor);
        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(1, new Vec2D(1500f, 900f), Engine.getNativeResolution().scalar(0.5f), menuButtonRoundingRadius, menuButtonBorderWidth, RGBAValue.SOLID_WHITE, RGBAValue.SOLID_BLACK);
        this.editPanel.addVisual(backgroundRectangle);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        Vec2D closeButtonPosition = new Vec2D(1625, 125);
        ActionButton closeButton = Widgets.RoundedRectangleButton(3, closeButtonSize, closeButtonPosition, menuButtonStyle2, "Close", (() -> this.menu.removeMenuScreen(editPanel)));
        this.editPanel.addWidget(closeButton);

        String panelTitleString = "Test";
        TextDisplay panelTitle = new TextDisplay(1, false, new Vec2D((float) Engine.getNativeWidth() / 2, 950f), panelTitleString, Text.menuButtonLabelStyle, TextAlignment.CENTER);
        this.editPanel.addVisual(panelTitle);

        Vec2D startPosition = new Vec2D(300f, 800f);
        EditionDataTypeSelect<D> editionDataTypeSelect = new EditionDataTypeSelect<>(editionData, startPosition);
        this.editPanelRoot = editionDataTypeSelect;

        editPanelRoot.setActive(true);
        MenuElementGroup allActiveItems = editPanelRoot.getAllActiveElements();
        this.editPanel.addElementGroup(allActiveItems);
        editPanelRoot.setMenu(menu);

        Vec2D applyButtonSize = new Vec2D(150, 50);
        Vec2D applyButtonPosition = new Vec2D(1465, 125);
        Runnable applyChanges = () -> {
            this.editPanelRoot.applyChanges();
            D selectedEditionData = editionDataTypeSelect.getSelectedEditionData();
            dataList.set(index, selectedEditionData);
        };
        ActionButton applyButton = Widgets.RoundedRectangleButton(3, applyButtonSize, applyButtonPosition, menuButtonStyle2, "Apply", applyChanges);
        this.editPanel.addWidget(applyButton);

        this.menu.addMenuScreen(this.editPanel);
    }
}
