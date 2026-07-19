package editor;

import edition.GameEditionData;
import engine.Engine;
import engine.GlobalVars;
import engine.menu.Menu;
import engine.menu.MenuScreen;
import engine.menu.widget.ActionButton;
import engine.menu.widget.TextField;
import engine.menu.widget.Widgets;
import engine.scene.Scene;
import engine.scene.visual.*;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import types.RGBAValue;
import types.Vec2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static editor.Style.*;
import static editor.Style.Color.menuBackgroundColor;
import static editor.Style.Text.menuFontPath;
import static engine.GlobalVars.Paths.rootFolderAbsolutePath;
import static engine.menu.MenuActions.terminateProgram;

final public class Menus {

    private Menus() {}

    public static class Screens {

        private Screens() {}

        private static MenuScreen EditPopupScreen() {
            MenuScreen popupMenu = new MenuScreen(3);
            Vec2D closeButtonSize = new Vec2D(150, 50);
            ActionButton closeButton = Widgets.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 930), menuButtonStyle1, "Close", () -> Engine.getCurrentMenu().removeMenuScreen(popupMenu));
            popupMenu.addWidget(closeButton);
            popupMenu.addVisual(new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.5f)));

            Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
            Vec2D listButtonSize = new Vec2D(300f, 75f);
            List<GameEditionData> loadedGames = Editor.getLoadedGames();
            for (int i = 0; i < loadedGames.size(); i++) {
                var game = loadedGames.get(i);
                popupMenu.addWidget(Widgets.RoundedRectangleButton(1, listButtonSize, new Vec2D(resolution.x / 2, 800 - (listButtonSize.y + 10f) * i), menuButtonStyle1, game.getGameName(), () -> openEditionMenu(game)));
            }
            return popupMenu;
        }

        private static void createNewGame(String gameName, List<String> existingFolders){
            assert !Objects.equals(gameName, ""): "empty game name";
            assert !existingFolders.contains(gameName): "game folder already exists";
            GameEditionData newGameData = new GameEditionData(rootFolderAbsolutePath.resolve(GlobalVars.Paths.Partial.customGamesFolder).resolve(gameName));
            newGameData.setToDefaultEmpty();
            openEditionMenu(newGameData);
        }

        private static MenuScreen NewGamePopupScreen(){
            Path gamesFolder = rootFolderAbsolutePath.resolve(GlobalVars.Paths.Partial.customGamesFolder);
            List<String> folderNames;
            try (Stream<Path> paths = Files.list(gamesFolder)) {
                folderNames = paths.filter(Files::isDirectory)
                        .map(path -> path.getFileName().toString())
                        .toList();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
            MenuScreen screen = new MenuScreen(3);
            Vec2D buttonSize = new Vec2D(150, 50);
            screen.addVisual(new ScreenFilter(0, new RGBAValue(0.0f, 0.0f, 0.0f, 0.5f)));
            SceneVisual backgroundRectangle = new BorderedRoundedRectangle(1, new Vec2D(500f, 160f), Engine.getNativeResolution().scalar(0.5f), menuButtonRoundingRadius, menuButtonBorderWidth, RGBAValue.SOLID_WHITE, RGBAValue.SOLID_BLACK);
            screen.addVisual(backgroundRectangle);
            Vec2D fieldLabelPosition = Engine.getNativeResolution().scalar(0.5f).add(-150f, 0f);
            SceneVisual fieldLabel = new TextDisplay(2, false, fieldLabelPosition, "Game Name: ", Text.menuButtonLabelStyle, TextAlignment.LEFT);
            screen.addVisual(fieldLabel);
            Vec2D textFieldPosition = Engine.getNativeResolution().scalar(0.5f).add(120f, 0f);
            TextField gameNameTextField = editor.Widgets.EditorTextField(2, textFieldPosition, 200f, "");
            screen.addWidget(gameNameTextField);
            ActionButton cancelButton = Widgets.RoundedRectangleButton(2, buttonSize, new Vec2D(Engine.getNativeWidth() / 2.0f + 80f  , 450), menuButtonStyle1, "Cancel", () -> Engine.getCurrentMenu().removeMenuScreen(screen));
            screen.addWidget(cancelButton);
            ActionButton okButton = Widgets.RoundedRectangleButton(2, buttonSize, new Vec2D(Engine.getNativeWidth() / 2.0f - 80f, 450), menuButtonStyle1, "OK", () -> createNewGame(gameNameTextField.getStringValue(), folderNames));
            screen.addWidget(okButton);

            return screen;
        }

        private static void openEditionMenu(GameEditionData game){
            Engine.switchCurrentScene(new Scene());
            Engine.switchCurrentMenu(EditionMenu.EditionMenu(game));
        }

    }

    public static Menu MainMenu() {
        Menu mainMenu = new Menu();
        Vec2D resolution = new Vec2D(Engine.getNativeWidth(), Engine.getNativeHeight());
        int backgroundLayer = 0;
        SceneVisual menuBackground = new ColorRectangleVisual(0, resolution, resolution.scalar(0.5f), menuBackgroundColor);

        TextStyle menuTitleTextStyle = new TextStyle(menuFontPath, Color.titleColor, 50);
        SceneVisual menuTitle = new TextDisplay(1, false, new Vec2D(resolution.x / 2, 800), "OpenShmup", menuTitleTextStyle, TextAlignment.CENTER);

        Runnable editGameAction = () -> {
            try {
                Editor.loadGames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            MenuScreen popupScreen = Screens.EditPopupScreen();
            mainMenu.addMenuScreen(popupScreen);
        };
        ActionButton newGameButton = Widgets.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 575), menuButtonStyle1, "Create New Game", () -> mainMenu.addMenuScreen(Screens.NewGamePopupScreen()));
        ActionButton editGameButton = Widgets.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 400), menuButtonStyle1, "Edit game", editGameAction);
        ActionButton quitButton = Widgets.RoundedRectangleButton(1, buttonSize, new Vec2D(resolution.x / 2, 225), menuButtonStyle1, "Quit", terminateProgram);
        MenuScreen titleScreen = new MenuScreen(backgroundLayer);
        titleScreen.addWidget(newGameButton);
        titleScreen.addWidget(editGameButton);
        titleScreen.addWidget(quitButton);
        titleScreen.addVisual(menuBackground);
        titleScreen.addVisual(menuTitle);

        mainMenu.addMenuScreen(titleScreen);
        return mainMenu;
    }

}
