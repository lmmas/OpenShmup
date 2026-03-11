package editor;

import editor.editionData.AnimationEditionData;
import editor.editionData.ScrollingImageEditionData;
import editor.editionData.VisualEditionData;
import engine.Engine;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.item.ActionButton;
import engine.menu.item.MenuItems;
import engine.scene.Scene;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.Vec2D;

import java.io.IOException;
import java.util.List;

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
            ActionButton closeButton = MenuItems.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonStyle, "Close", () -> Engine.getCurrentMenu().removeMenuScreen(popupMenu));
            popupMenu.addItem(closeButton);
            popupMenu.addVisual(new ScreenFilter(0, 0.0f, 0.0f, 0.0f, 0.5f));

            Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
            for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                var game = Editor.getLoadedGames().get(i);
                Runnable onclick = () -> {
                    Engine.switchCurrentScene(new Scene());
                    Engine.switchCurrentMenu(Menus.EditGameMenu(game));
                };
                popupMenu.addItem(MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 800 - (buttonSize.y + 20f) * i), menuButtonStyle, game.getGameName(), onclick));
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
        ActionButton button1 = MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 500), menuButtonStyle, "Edit game", editGameAction);
        ActionButton button2 = MenuItems.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 300), menuButtonStyle, "Quit", terminateProgram);
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
        MenuScreen editMenuScreen = new MenuScreen(0);

        SceneVisual menuBackground = new ScreenFilter(0, menuBackgroundColor);
        editMenuScreen.addVisual(menuBackground);

        TextDisplay screenTitle = new TextDisplay(1, false, new Vec2D((float) Engine.getNativeWidth() / 2, 970f), "Edit visuals", Text.menuScreenTitleStyle, TextAlignment.CENTER);
        editMenuScreen.addVisual(screenTitle);

        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        int visualListIndex = 0;
        for (var visualData : visualEditionDataList) {
            Runnable onClick = () -> menu.addMenuScreen(new EditPanel(visualData).getMenuScreen());
            String typeString = "";
            if (visualData instanceof AnimationEditionData) {
                typeString = "Animation";
            }
            if (visualData instanceof ScrollingImageEditionData) {
                typeString = "Scrolling image";
            }
            String menuButtonLabel = visualData.getId() + ": " + typeString;

            ActionButton visualSelectButton = MenuItems.RoundedRectangleButton(2, new Vec2D(300f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonStyle, menuButtonLabel, onClick);
            editMenuScreen.addItem(visualSelectButton);
            visualListIndex++;
        }
        menu.addMenuScreen(editMenuScreen);
        return menu;
    }
}
