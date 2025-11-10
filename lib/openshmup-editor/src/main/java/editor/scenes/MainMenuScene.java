package editor.scenes;

import engine.scene.Scene;
import engine.scene.menu.MenuScreen;
import engine.scene.menu.item.ColorRectangleButton;
import engine.visual.ColorRectangleVisual;
import engine.visual.ScreenFilter;
import engine.visual.TextDisplay;
import engine.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.List;

import static engine.Application.window;
import static engine.GlobalVars.Paths.debugFont;
import static engine.scene.menu.MenuActions.terminateProgram;

final public class MainMenuScene extends Scene {
    final private int backgroundLayer = 0;

    final private MenuScreen mainMenu;
    final private MenuScreen popupMenu;

    public MainMenuScene() {
        RGBAValue menuBackgroundColor = new RGBAValue(0.0f, 0.1f, 0.3f, 1.0f);
        ColorRectangleVisual menuBackground = new ColorRectangleVisual(backgroundLayer, 1.0f, 1.0f, 0.5f, 0.5f, menuBackgroundColor.r, menuBackgroundColor.g, menuBackgroundColor.b, menuBackgroundColor.a);
        addVisual(menuBackground);

        RGBAValue menuTitleTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50.0f / window.getHeight());
        TextDisplay menuTitle = new TextDisplay(backgroundLayer + 1, false, 0.5f, 0.8f, "OpenShmup", menuTitleTextStyle);
        addVisual(menuTitle);

        RGBAValue menuButtonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue menuButtonTextColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle menuButtonLabelStyle = new TextStyle(debugFont, menuButtonTextColor,17.0f / window.getHeight());
        Vec2D buttonSize = new Vec2D(0.3f, 0.15f);
        ColorRectangleButton button1 = new ColorRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(0.5f, 0.5f), menuButtonColor, "Open popup menu", menuButtonLabelStyle, null);
        ColorRectangleButton button2 = new ColorRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(0.5f, 0.25f), menuButtonColor, "Quit", menuButtonLabelStyle, terminateProgram);

        mainMenu = new MenuScreen(backgroundLayer, null, List.of(button1, button2));

        ColorRectangleButton button3 = new ColorRectangleButton(backgroundLayer + 3, buttonSize, new Vec2D(0.5f, 0.35f), menuButtonColor, "yes", menuButtonLabelStyle, null);
        popupMenu = new MenuScreen(backgroundLayer + 2, new ScreenFilter(backgroundLayer + 2, 0.0f, 0.0f, 0.0f, 0.5f), List.of(button3));

        button1.setOnclick(() -> addMenu(popupMenu));

        button3.setOnclick(() -> removeMenu(popupMenu));


        addMenu(mainMenu);
    }
}
