package editor.scenes;

import editor.Editor;
import engine.Engine;
import engine.menu.*;
import engine.scene.Scene;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.ColorRectangleVisual;
import engine.visual.ScreenFilter;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;

import java.io.IOException;

import static editor.Style.*;
import static engine.GlobalVars.Paths.debugFont;
import static engine.menu.MenuActions.terminateProgram;

final public class MainMenuScene extends Scene {

    static final private int backgroundLayer = 0;

    final private MenuScreen mainMenu;

    final private ColorRectangleVisual menuBackground;

    final private TextDisplay menuTitle;

    public MainMenuScene() {
        MenuManager menuManager = new MenuManager(this);
        this.setMenuManager(menuManager);
        RGBAValue menuBackgroundColor = new RGBAValue(0.0f, 0.1f, 0.3f, 1.0f);
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        menuBackground = new ColorRectangleVisual(backgroundLayer, resolution.x, resolution.y, resolution.x / 2, resolution.y / 2, menuBackgroundColor.r, menuBackgroundColor.g, menuBackgroundColor.b, menuBackgroundColor.a);

        RGBAValue menuTitleTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50);
        menuTitle = new TextDisplay(backgroundLayer + 1, false, resolution.x / 2, 800, "OpenShmup", menuTitleTextStyle, TextAlignment.CENTER);

        MenuAction editGameAction = (scene) -> {
            try {
                Editor.loadGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MenuScreen popupMenu = PopupMenu();
            menuManager.addMenu(popupMenu);
        };
        MenuItem button1 = MenuItems.RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(resolution.x / 2, 500), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Edit game", menuButtonLabelStyle, editGameAction);
        MenuItem button2 = MenuItems.RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(resolution.x / 2, 300), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Quit", menuButtonLabelStyle, terminateProgram);


        mainMenu = new MenuScreen(backgroundLayer);
        mainMenu.addItem(button1);
        mainMenu.addItem(button2);
    }

    @Override
    public void start() {
        addVisual(menuBackground);
        addVisual(menuTitle);
        getMenuManager().addMenu(mainMenu);
        super.start();
    }

    private static MenuScreen PopupMenu() {

        MenuScreen popupMenu = new MenuScreen(backgroundLayer + 3);
        Vec2D closeButtonSize = new Vec2D(150, 50);
        MenuItem closeButton = MenuItems.RoundedRectangleButton(backgroundLayer + 4, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, (scene) -> Engine.menuManager.removeMenu(popupMenu));
        popupMenu.addItem(closeButton);
        popupMenu.addVisual(new ScreenFilter(backgroundLayer + 3, 0.0f, 0.0f, 0.0f, 0.5f));

        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
            var game = Editor.getLoadedGames().get(i);
            popupMenu.addItem(MenuItems.RoundedRectangleButton(backgroundLayer + 4, buttonSize, new Vec2D(resolution.x / 2, 800 - (buttonSize.y + 20f) * i), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, game.getGameName(), menuButtonLabelStyle, (scene1) -> Engine.switchCurrentScene(new EditGameScene(game))));
        }
        return popupMenu;
    }
}
