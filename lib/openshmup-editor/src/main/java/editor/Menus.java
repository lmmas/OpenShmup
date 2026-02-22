package editor;

import editor.editionData.AnimationEditionData;
import editor.editionData.EditionData;
import editor.editionData.ScrollingImageEditionData;
import editor.editionData.VisualEditionData;
import engine.Engine;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.item.ActionButton;
import engine.menu.item.ActionButtons;
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
import java.util.List;

import static editor.Style.Color.menuBackgroundColor;
import static editor.Style.*;
import static engine.GlobalVars.Paths.debugFont;
import static engine.menu.MenuActions.terminateProgram;

final public class Menus {

    private Menus() {}

    public static class Screens {

        private Screens() {}

        private static MenuScreen PopupScreen() {
            MenuScreen popupMenu = new MenuScreen(3);
            Vec2D closeButtonSize = new Vec2D(150, 50);
            ActionButton closeButton = ActionButtons.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Close", menuButtonLabelStyle, () -> Engine.getCurrentMenu().removeMenuScreen(popupMenu));
            popupMenu.addItem(closeButton);
            popupMenu.addVisual(new ScreenFilter(0, 0.0f, 0.0f, 0.0f, 0.5f));

            Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
            for (int i = 0; i < Editor.getLoadedGames().size(); i++) {
                var game = Editor.getLoadedGames().get(i);
                Runnable onclick = () -> {
                    Engine.switchCurrentScene(new Scene());
                    Engine.switchCurrentMenu(Menus.EditGameMenu(game));
                };
                popupMenu.addItem(ActionButtons.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 800 - (buttonSize.y + 20f) * i), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, game.getGameName(), menuButtonLabelStyle, onclick));
            }
            return popupMenu;
        }
    }

    public static Menu MainMenu() {
        Menu mainMenu = new Menu();
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        int backgroundLayer = 0;
        SceneVisual menuBackground = new ColorRectangleVisual(0, resolution.x, resolution.y, resolution.x / 2, resolution.y / 2, menuBackgroundColor.r, menuBackgroundColor.g, menuBackgroundColor.b, menuBackgroundColor.a);

        RGBAValue menuTitleTextColor = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        TextStyle menuTitleTextStyle = new TextStyle(debugFont, menuTitleTextColor, 50);
        SceneVisual menuTitle = new TextDisplay(1, false, resolution.x / 2, 800, "OpenShmup", menuTitleTextStyle, TextAlignment.CENTER);

        Runnable editGameAction = () -> {
            try {
                Editor.loadGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MenuScreen popupScreen = Screens.PopupScreen();
            mainMenu.addMenuScreen(popupScreen);
        };
        ActionButton button1 = ActionButtons.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 500), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Edit game", menuButtonLabelStyle, editGameAction);
        ActionButton button2 = ActionButtons.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 300), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, "Quit", menuButtonLabelStyle, terminateProgram);
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

        TextDisplay screenTitle = new TextDisplay(1, false, (float) Engine.getNativeWidth() / 2, 970f, "Edit visuals", menuScreenTitleStyle, TextAlignment.CENTER);
        editMenuScreen.addVisual(screenTitle);

        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        int visualListIndex = 0;
        for (var visualData : visualEditionDataList) {
            Runnable onClick = () -> menu.addMenuScreen(EditPanels.createPanel((EditionData) visualData));
            String typeString = "";
            if (visualData instanceof AnimationEditionData) {
                typeString = "Animation";
            }
            if (visualData instanceof ScrollingImageEditionData) {
                typeString = "Scrolling image";
            }
            String menuButtonLabel = visualData.getId() + ": " + typeString;

            ActionButton visualSelectButton = ActionButtons.RoundedRectangleButton(2, new Vec2D(300f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, menuButtonLabel, menuButtonLabelStyle, onClick);
            editMenuScreen.addItem(visualSelectButton);
            visualListIndex++;
        }
        menu.addMenuScreen(editMenuScreen);
        return menu;
    }
}
