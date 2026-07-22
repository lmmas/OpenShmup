package engine.menu;

import engine.menu.widget.ActionButton;
import engine.menu.widget.Widgets;
import engine.scene.visual.ScreenFilter;
import engine.scene.visual.style.TextStyle;
import types.RGBAValue;
import types.Vec2D;

import static engine.GlobalVars.Paths.debugFont;

final public class GameMenus {

    private GameMenus() {}

    public static MenuScreen PauseMenu(int layer) {
        MenuScreen pauseMenu = new MenuScreen(layer);

        RGBAValue buttonColor = new RGBAValue(0.7f, 0.9f, 1.0f, 1.0f);
        RGBAValue buttonLabelColor = new RGBAValue(0.0f, 0.0f, 0.0f, 1.0f);
        TextStyle buttonTextStyle = new TextStyle(debugFont, buttonLabelColor, 25.0f);
        Vec2D buttonSize = new Vec2D(300, 150);
        ActionButton blueButton = Widgets.TextButton(1, buttonSize, new Vec2D(500f, 500f),5f, 1f, buttonColor, RGBAValue.SOLID_BLACK,"Restart Game", buttonTextStyle, MenuActions.reloadGame);
        pauseMenu.addWidget(blueButton);

        pauseMenu.addVisual(new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.7f)));
        return pauseMenu;
    }
}
