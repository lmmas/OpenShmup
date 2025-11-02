package editor.scenes;

import engine.Application;
import engine.GlobalVars;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.graphics.ColorRectangle;
import engine.scene.Scene;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;
import engine.scene.visual.ColorRectangleVisual;
import engine.scene.visual.TextDisplay;

import java.util.List;

import static engine.Application.assetManager;
import static engine.Application.window;
import static engine.scene.menu.MenuActions.quitGame;

public class MainMenuScene extends Scene {
    final private int backgroundLayer = 0;

    final private MenuScreen mainMenu;

    public MainMenuScene() {
        ColorRectangleVisual yellowRectangle = new ColorRectangleVisual(new ColorRectangle(backgroundLayer, 1.0f, 1.0f, 0.7f, 0.9f, 0.0f, 1.0f,assetManager.getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl")));
        yellowRectangle.setPosition(0.5f, 0.5f);
        addVisual(yellowRectangle);
        TextDisplay titleText = new TextDisplay(backgroundLayer + 1, false, 0.5f, 0.5f, 50.0f / window.getHeight(), "OpenShmup", assetManager.getFont(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf"));
        titleText.setPosition(0.5f, 0.8f);
        addVisual(titleText);

        ColorRectangleVisual blueRectangle = new ColorRectangleVisual(new ColorRectangle(backgroundLayer + 1, 0.3f, 0.15f, 0.7f, 0.9f, 1.0f, 1.0f,assetManager.getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl")));
        blueRectangle.setPosition(0.5f, 0.5f);

        TextDisplay buttonText = new TextDisplay(backgroundLayer + 2, false, 0.5f, 0.5f, 17.0f / window.getHeight(), "bouton qui fait rien", assetManager.getFont(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf"));
        buttonText.setPosition(0.5f, 0.5f);
        buttonText.setTextColor(0.0f, 0.0f, 0.0f, 1.0f);
        MenuItem testItem = new MenuItem(List.of(blueRectangle, buttonText), Hitbox.DEFAULT_EMPTY(), () -> {});


        ColorRectangleVisual blueRectangle2 = new ColorRectangleVisual(new ColorRectangle(backgroundLayer + 1, 0.3f, 0.15f, 0.7f, 0.9f, 1.0f, 1.0f,assetManager.getShader(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl")));
        blueRectangle2.setPosition(0.5f, 0.25f);

        TextDisplay buttonText2 = new TextDisplay(backgroundLayer + 2, false, 0.5f, 0.5f, 17.0f / window.getHeight(), "Quitter", assetManager.getFont(GlobalVars.Paths.rootFolderAbsolutePath + "/lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf"));
        buttonText2.setPosition(0.5f, 0.25f);
        buttonText2.setTextColor(0.0f, 0.0f, 0.0f, 1.0f);
        MenuItem testItem2 = new MenuItem(List.of(blueRectangle2, buttonText2), new SimpleRectangleHitbox(0.5f, 0.25f, 0.3f, 0.15f), quitGame);

        mainMenu = new MenuScreen(backgroundLayer, null, List.of(testItem, testItem2));
        addMenu(mainMenu);
        setActiveMenu(mainMenu);
    }
}
