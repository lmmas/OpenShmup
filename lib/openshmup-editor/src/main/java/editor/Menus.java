package editor;

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
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.io.IOException;

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
            popupMenu.addVisual(new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.5f)));

            Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
            for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                var game = Editor.getLoadedGames().get(i);
                Runnable onclick = () -> {
                    Engine.switchCurrentScene(new Scene());
                    Engine.switchCurrentMenu(new EditionMenu(game).getMenu());
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

}
