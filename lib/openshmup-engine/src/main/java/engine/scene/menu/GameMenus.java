package engine.scene.menu;

import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.visual.ScreenFilter;
import engine.visual.style.TextStyle;

import static engine.GlobalVars.Paths.debugFont;

public class GameMenus {

    private GameMenus() {}

    public static MenuScreen PauseMenu(int layer) {
        MenuScreen pauseMenu = new MenuScreen(layer);

        RGBAValue buttonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue buttonLabelColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle buttonTextStyle = new TextStyle(debugFont, buttonLabelColor, 25.0f);
        Vec2D buttonSize = new Vec2D(300, 150);
        MenuItem blueButton = MenuItems.ColorRectangleButton(layer + 1, buttonSize, new Vec2D(500f, 500f), buttonColor, "Restart Game", buttonTextStyle, MenuActions.reloadGame);
        pauseMenu.addItem(blueButton);

        pauseMenu.addVisual(new ScreenFilter(layer, 0.0f, 0.0f, 0.0f, 0.7f));
        return pauseMenu;
    }
}
