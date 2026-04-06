package editor;

import editor.editionData.*;
import engine.Engine;
import engine.menu.ItemGroup;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.item.ActionButton;
import engine.menu.item.MenuItem;
import engine.menu.item.MenuItems;
import engine.menu.item.SelectorButtons;
import engine.scene.Scene;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.Reference;
import engine.types.Vec2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static editor.Style.*;
import static editor.Style.Color.menuBackgroundColor;
import static engine.GlobalVars.Paths.debugFont;
import static engine.menu.MenuActions.terminateProgram;

final public class Menus {

    private Menus() {}

    public static class Screens {

        private Screens() {}

        private static MenuScreen PopupScreen() {
            MenuScreen popupMenu = new MenuScreen(3);
            Vec2D closeButtonSize = new Vec2D(150, 50);
            ActionButton closeButton = MenuItems.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonStyle1, "Close", () -> Engine.getCurrentMenu().removeMenuScreen(popupMenu));
            popupMenu.addItem(closeButton);
            popupMenu.addVisual(new ScreenFilter(0, 0.0f, 0.0f, 0.0f, 0.5f));

            Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
            for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                var game = Editor.getLoadedGames().get(i);
                Runnable onclick = () -> {
                    Engine.switchCurrentScene(new Scene());
                    Engine.switchCurrentMenu(Menus.EditGameMenu(game));
                };
                popupMenu.addItem(MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 800 - (buttonSize.y + 20f) * i), menuButtonStyle1, game.getGameName(), onclick));
            }
            return popupMenu;
        }
    }

    public static Menu MainMenu() {
        Menu mainMenu = new Menu();
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        int backgroundLayer = 0;
        SceneVisual menuBackground = new ColorRectangleVisual(0, resolution, resolution.scalar(0.5f), menuBackgroundColor);

        TextStyle menuTitleTextStyle = new TextStyle(debugFont, Color.titleColor, 50);
        SceneVisual menuTitle = new TextDisplay(1, false, new Vec2D(resolution.x / 2, 800), "OpenShmup", menuTitleTextStyle, TextAlignment.CENTER);

        Runnable editGameAction = () -> {
            try {
                Editor.loadGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MenuScreen popupScreen = Screens.PopupScreen();
            mainMenu.addMenuScreen(popupScreen);
        };
        ActionButton button1 = MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 500), menuButtonStyle1, "Edit game", editGameAction);
        ActionButton button2 = MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 300), menuButtonStyle1, "Quit", terminateProgram);
        MenuScreen titleScreen = new MenuScreen(backgroundLayer);
        titleScreen.addItem(button1);
        titleScreen.addItem(button2);
        titleScreen.addVisual(menuBackground);
        titleScreen.addVisual(menuTitle);

        mainMenu.addMenuScreen(titleScreen);
        return mainMenu;
    }

    public static Menu EditGameMenu(EditorGameDataManager gameData) {
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
        ActionButton returnToMainMenuButton = MenuItems.RoundedRectangleButton(1, new Vec2D(300, 50), new Vec2D(1750, 75), menuButtonStyle1, "Return to main menu", returnToMainMenu);
        mainScreen.addItem(returnToMainMenuButton);

        Vec2D listButtonSize = new Vec2D(300f, 50f);
        float listButtonSpacing = 15f;

        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        ArrayList<MenuItem> visualListItems = new ArrayList<>(visualEditionDataList.size());
        for (int i = 0; i < visualEditionDataList.size(); i++) {
            var visualData = visualEditionDataList.get(i);
            Runnable onClick = () -> menu.addMenuScreen(new EditPanel(visualData).getMenuScreen());
            String typeString = "";
            if (visualData instanceof AnimationEditionData) {
                typeString = "Animation";
            }
            if (visualData instanceof ScrollingImageEditionData) {
                typeString = "Scrolling image";
            }
            String menuButtonLabel = visualData.getId() + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = MenuItems.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            visualListItems.add(visualSelectButton);
        }
        ItemGroup visualList = new ItemGroup(visualListItems, List.of());

        List<TrajectoryEditionData> trajectoryEditionDataList = gameData.getTrajectoryEditionDataList();
        ArrayList<MenuItem> trajectoryListItems = new ArrayList<>(trajectoryEditionDataList.size());
        for (int i = 0; i < trajectoryEditionDataList.size(); i++) {
            var trajectoryData = trajectoryEditionDataList.get(i);
            Runnable onClick = () -> menu.addMenuScreen(new EditPanel(trajectoryData).getMenuScreen());
            String typeString = "";
            if (trajectoryData instanceof PlayerControlledTrajectoryEditionData) {
                typeString = "Player Controlled";
            }
            if (trajectoryData instanceof FixedTrajectoryEditionData) {
                typeString = "Fixed";
            }
            String menuButtonLabel = trajectoryData.getId() + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = MenuItems.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            trajectoryListItems.add(visualSelectButton);
        }
        ItemGroup trajectoryList = new ItemGroup(trajectoryListItems, List.of());

        List<EntityEditionData> entityEditionDataList = new ArrayList<>(gameData.getEntityEditionDataMap().values());
        List<MenuItem> entityListItems = new ArrayList<>(entityEditionDataList.size());
        for (int i = 0; i < entityEditionDataList.size(); i++) {
            var entityData = entityEditionDataList.get(i);
            Runnable onClick = () -> menu.addMenuScreen(new EditPanel(entityData).getMenuScreen());
            String typeString = "";
            if (entityData instanceof ShipEditionData) {
                typeString = "Ship";
            }
            if (entityData instanceof ProjectileEditionData) {
                typeString = "Projectile";
            }
            String menuButtonLabel = entityData.getId() + ": " + typeString;

            Vec2D listButtonPosition = new Vec2D((float) Engine.getNativeWidth() / 2, 850f - (i * (listButtonSize.y + listButtonSpacing)));
            ActionButton visualSelectButton = MenuItems.RoundedRectangleButton(2, listButtonSize, listButtonPosition, menuButtonStyle1, menuButtonLabel, onClick);
            entityListItems.add(visualSelectButton);
        }
        ItemGroup entityList = new ItemGroup(entityListItems, List.of());

        ItemGroup timelineSpawnList = new ItemGroup(List.of(), List.of());

        List<String> labels = List.of("Visuals", "Trajectories", "Entities", "Timeline");
        Vec2D selectorButtonStride = new Vec2D(250f, 0f);
        Vec2D selectorButtonSize = new Vec2D(200, 50f);
        Vec2D selectorStartPosition = new Vec2D(Engine.getNativeWidth() * 0.5f - 1.5f * selectorButtonStride.x, 950);
        final Reference<ItemGroup> currentList = new Reference<>(null);
        Consumer<Integer> onChange = (Integer selectedValue) -> {
            if (currentList.get() != null) {
                menu.removeItemGroupFromCurrentScreen(currentList.get());
            }
            ItemGroup newCurrentList = switch (selectedValue) {
                case 0 -> visualList;
                case 1 -> trajectoryList;
                case 2 -> entityList;
                case 3 -> timelineSpawnList;
                default -> throw new IllegalArgumentException("incorrect selected value");
            };
            menu.addItemGroupToCurrentScreen(newCurrentList);
            currentList.set(newCurrentList);
        };
        SelectorButtons objectSelector = MenuItems.StandardSelectorButtons(1, 4, selectorButtonSize, selectorStartPosition, selectorButtonStride, menuButtonStyle1, labels, onChange);
        mainScreen.addItem(objectSelector);
        currentList.set(visualList);
        mainScreen.addItemGroup(currentList.get());
        menu.addMenuScreen(mainScreen);

        return menu;
    }
}
