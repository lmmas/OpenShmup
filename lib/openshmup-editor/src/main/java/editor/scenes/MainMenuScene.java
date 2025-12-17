package editor.scenes;

import editor.Editor;
import engine.Engine;
import engine.scene.Scene;
import engine.scene.menu.MenuScreen;
import engine.scene.menu.item.RoundedRectangleButton;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.ColorRectangleVisual;
import engine.visual.ScreenFilter;
import engine.visual.TextDisplay;
import engine.visual.style.TextStyle;

import java.io.IOException;
import java.util.List;

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
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50.0f / Engine.getNativeHeight());
        TextDisplay menuTitle = new TextDisplay(backgroundLayer + 1, false, 0.5f, 0.8f, "OpenShmup", menuTitleTextStyle);
        addVisual(menuTitle);

        RGBAValue menuButtonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue menuButtonBorderColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        float roundingRadius = 0.1f;
        float borderWidth = 0.02f;
        RGBAValue menuButtonTextColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle menuButtonLabelStyle = new TextStyle(debugFont, menuButtonTextColor, 17.0f / Engine.getNativeHeight());
        Vec2D buttonSize = new Vec2D(0.3f, 0.15f);
        RoundedRectangleButton button1 = new RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(0.5f, 0.5f), roundingRadius, borderWidth, menuButtonColor, menuButtonBorderColor, "Edit game", menuButtonLabelStyle, null);
        RoundedRectangleButton button2 = new RoundedRectangleButton(backgroundLayer + 1, buttonSize, new Vec2D(0.5f, 0.25f), roundingRadius, borderWidth, menuButtonColor, menuButtonBorderColor, "Quit", menuButtonLabelStyle, terminateProgram);


        mainMenu = new MenuScreen(backgroundLayer, null, List.of(button1, button2));

        Vec2D closeButtonSize = new Vec2D(0.06f, 0.05f);
        RoundedRectangleButton closeButton = new RoundedRectangleButton(backgroundLayer + 4, closeButtonSize, new Vec2D(0.9f, 0.9f), roundingRadius, borderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, null);
        popupMenu = new MenuScreen(backgroundLayer + 3, new ScreenFilter(backgroundLayer + 3, 0.0f, 0.0f, 0.0f, 0.5f), List.of(closeButton));

        button1.setOnclick(() -> {
                try {
                    Editor.loadGames();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                    var game = Editor.getLoadedGames().get(i);
                    popupMenu.menuItems().add(new RoundedRectangleButton(backgroundLayer + 4, buttonSize, new Vec2D(0.5f, 0.8f - (buttonSize.y + 0.02f) * i), roundingRadius, borderWidth, menuButtonColor, menuButtonBorderColor, game.getGameName(), menuButtonLabelStyle, null));
                }
                addMenu(popupMenu);
            }
        );

        closeButton.setOnclick(() -> removeMenu(popupMenu));


        addMenu(mainMenu);
    }
}
