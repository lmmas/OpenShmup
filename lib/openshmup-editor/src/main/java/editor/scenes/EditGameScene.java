package editor.scenes;

import editor.EditorGameDataManager;
import editor.editionData.EditionData;
import editor.editionData.VisualEditionData;
import engine.Engine;
import engine.menu.MenuItems;
import engine.menu.MenuManager;
import engine.menu.MenuScreen;
import engine.scene.Scene;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;

import java.util.List;

import static editor.Style.*;

public class EditGameScene extends Scene {

    private final MenuScreen editMenu;

    private final MenuScreen editVisualMenu;

    public EditGameScene(EditorGameDataManager gameData) {
        MenuManager menuManager = new MenuManager(this);
        this.setMenuManager(menuManager);
        this.editMenu = new MenuScreen(0);
        this.editVisualMenu = new MenuScreen(3);
        this.editVisualMenu.addVisual(new BorderedRoundedRectangle(3, 1500f, 900f, Engine.getNativeWidth() / 2, Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor.r, menuButtonColor.g, menuButtonColor.b, menuButtonColor.a, menuButtonBorderColor.r, menuButtonBorderColor.g, menuButtonColor.b, menuButtonBorderColor.a));
        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        int visualListIndex = 0;
        for (var visualAttributes : visualEditionDataList) {
            editMenu.addItem(MenuItems.RoundedRectangleButton(1, new Vec2D(400f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, Integer.toString(visualAttributes.getId().getValue()), menuButtonLabelStyle, (scene) -> {
                menuManager.addMenu(EditionData.createPanel((EditionData) visualAttributes));
            }));
            visualListIndex++;
        }
    }

    @Override
    public void start() {
        this.getMenuManager().addMenu(editMenu);
        super.start();
    }
}
