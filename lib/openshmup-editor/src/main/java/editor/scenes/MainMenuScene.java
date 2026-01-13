package editor.scenes;

import editor.Editor;
import engine.Engine;
import engine.scene.Scene;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuItems;
import engine.scene.menu.MenuScreen;
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
import static engine.scene.menu.MenuActions.terminateProgram;

final public class MainMenuScene extends Scene {

    final private int backgroundLayer = 0;

    final private MenuScreen mainMenu;

    final private MenuScreen popupMenu;

    final private ColorRectangleVisual menuBackground;

    final private TextDisplay menuTitle;

    public MainMenuScene() {
        RGBAValue menuBackgroundColor = new RGBAValue(0.0f, 0.1f, 0.3f, 1.0f);
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        menuBackground = new ColorRectangleVisual(backgroundLayer, resolution.x, resolution.y, resolution.x / 2, resolution.y / 2, menuBackgroundColor.r, menuBackgroundColor.g, menuBackgroundColor.b, menuBackgroundColor.a);

        RGBAValue menuTitleTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50);
        menuTitle = new TextDisplay(backgroundLayer + 1, false, resolution.x / 2, 800, "OpenShmup", menuTitleTextStyle, TextAlignment.CENTER);

        MenuItem button1 = MenuItems.RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(resolution.x / 2, 500), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Edit game", menuButtonLabelStyle, null);
        MenuItem button2 = MenuItems.RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(resolution.x / 2, 300), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Quit", menuButtonLabelStyle, terminateProgram);


        mainMenu = new MenuScreen(backgroundLayer);
        mainMenu.addItem(button1);
        mainMenu.addItem(button2);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        MenuItem closeButton = MenuItems.RoundedRectangleButton(backgroundLayer + 4, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, null);
        popupMenu = new MenuScreen(backgroundLayer + 3);
        popupMenu.addItem(closeButton);
        popupMenu.addVisual(new ScreenFilter(backgroundLayer + 3, 0.0f, 0.0f, 0.0f, 0.5f));

        button1.setOnclick(() -> {
                try {
                    Editor.loadGames();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                    var game = Editor.getLoadedGames().get(i);
                    popupMenu.addItem(MenuItems.RoundedRectangleButton(backgroundLayer + 4, buttonSize, new Vec2D(resolution.x / 2, 800 - (buttonSize.y + 20f) * i), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, game.getGameName(), menuButtonLabelStyle, () -> Engine.switchCurrentScene(new EditGameScene(game))));
                }
                addMenu(popupMenu);
            }
        );

        closeButton.setOnclick(() -> removeMenu(popupMenu));
    }

    @Override
    public void start() {
        addVisual(menuBackground);
        addVisual(menuTitle);
        addMenu(mainMenu);
        super.start();
    }
}
