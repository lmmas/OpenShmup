package editor.scenes;

import engine.Engine;
import engine.gameData.GameDataManager;
import engine.scene.Scene;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;
import engine.scene.menu.item.RoundedRectangleButton;
import engine.types.Vec2D;
import engine.visual.SceneVisual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static editor.Style.*;

public class EditGameScene extends Scene {
    private GameDataManager gameData;
    private ArrayList<MenuItem> visualSelectButtons;
    private MenuScreen editMenu;

    public EditGameScene(GameDataManager gameData) {
        this.gameData = gameData;
        this.visualSelectButtons = new ArrayList<>();
        this.editMenu = new MenuScreen(0);

        HashMap<Integer, SceneVisual> visuals = gameData.getVisuals();
        int visualListIndex = 0;
        for (Map.Entry<Integer, SceneVisual> entry : visuals.entrySet()) {
            editMenu.menuItems().add(new RoundedRectangleButton(1, new Vec2D(400f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, entry.getKey().toString(), menuButtonLabelStyle, null));
            visualListIndex++;
        }
    }

    @Override
    public void start() {
        addMenu(editMenu);
        super.start();
    }
}
