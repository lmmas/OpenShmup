package engine.menu;

import engine.menu.item.ActionButton;
import engine.menu.item.ActionButtons;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.style.TextStyle;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import static engine.GlobalVars.Paths.debugFont;

public class GameMenus {

    private GameMenus() {}

    public static MenuScreen PauseMenu(int layer) {
        MenuScreen pauseMenu = new MenuScreen(layer);

        RGBAValue buttonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue buttonLabelColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle buttonTextStyle = new TextStyle(debugFont, buttonLabelColor, 25.0f);
        Vec2D buttonSize = new Vec2D(300, 150);
        ActionButton blueButton = ActionButtons.ColorRectangleButton(1, buttonSize, new Vec2D(500f, 500f), buttonColor, "Restart Game", buttonTextStyle, MenuActions.reloadGame);
        pauseMenu.addItem(blueButton);

        pauseMenu.addVisual(new ScreenFilter(0, 0.0f, 0.0f, 0.0f, 0.7f));
        return pauseMenu;
    }
}
