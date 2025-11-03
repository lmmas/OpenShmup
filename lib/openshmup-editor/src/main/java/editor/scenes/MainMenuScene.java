package editor.scenes;

import engine.scene.Scene;
import engine.scene.menu.MenuScreen;
import engine.scene.menu.item.ColorRectangleButton;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;

import java.util.List;

import static engine.Application.window;
import static engine.GlobalVars.Paths.debugFont;
import static engine.scene.menu.MenuActions.terminateProgram;

public class MainMenuScene extends Scene {
    final private int backgroundLayer = 0;

    final private MenuScreen mainMenu;

    public MainMenuScene() {
        RGBAValue menuBackgroundColor = new RGBAValue(0.7f, 0.9f, 0.0f, 1.0f);
        ColorRectangleVisual menuBackground = new ColorRectangleVisual(backgroundLayer, 1.0f, 1.0f, 0.5f, 0.5f, menuBackgroundColor.r, menuBackgroundColor.g, menuBackgroundColor.b, menuBackgroundColor.a);
        addVisual(menuBackground);

        RGBAValue menuTitleTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50.0f / window.getHeight());
        TextDisplay menuTitle = new TextDisplay(backgroundLayer + 1, false, 0.5f, 0.8f, "OpenShmup", menuTitleTextStyle);
        addVisual(menuTitle);

        RGBAValue menuButtonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue menuButtonTextColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle menuButtonLabelStyle = new TextStyle(debugFont, menuButtonTextColor,17.0f / window.getHeight());
        ColorRectangleButton button1 = new ColorRectangleButton(backgroundLayer + 1, 0.3f, 0.15f, 0.5f, 0.5f, menuButtonColor, "bouton qui fait rien", menuButtonLabelStyle, () -> {});
        ColorRectangleButton button2 = new ColorRectangleButton(backgroundLayer + 1, 0.3f, 0.15f, 0.5f, 0.25f, menuButtonColor, "Quitter", menuButtonLabelStyle, terminateProgram);

        mainMenu = new MenuScreen(backgroundLayer, null, List.of(button1, button2));


        addMenu(mainMenu);
        setActiveMenu(mainMenu);
    }
}
