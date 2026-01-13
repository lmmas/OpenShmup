package editor.scenes;

import editor.EditorGameDataManager;
import editor.editionData.visual.VisualEditionData;
import engine.Engine;
import engine.scene.Scene;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuItems;
import engine.scene.menu.MenuScreen;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;

import java.util.ArrayList;
import java.util.List;

import static editor.Style.*;

public class EditGameScene extends Scene {

    private final EditorGameDataManager gameData;

    private final ArrayList<MenuItem> visualSelectButtons;

    private final MenuScreen editMenu;

    private final MenuScreen editVisualMenu;

    public EditGameScene(EditorGameDataManager gameData) {
        this.gameData = gameData;
        this.visualSelectButtons = new ArrayList<>();
        this.editMenu = new MenuScreen(0);
        this.editVisualMenu = new MenuScreen(3);
        this.editVisualMenu.addVisual(new BorderedRoundedRectangle(3, 1500f, 900f, Engine.getNativeWidth() / 2, Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor.r, menuButtonColor.g, menuButtonColor.b, menuButtonColor.a, menuButtonBorderColor.r, menuButtonBorderColor.g, menuButtonColor.b, menuButtonBorderColor.a));
        List<VisualEditionData> visualEditionDataList = gameData.getVisualEditionDataList();
        int visualListIndex = 0;
        for (var visualAttributes : visualEditionDataList) {
            editMenu.addItem(MenuItems.RoundedRectangleButton(1, new Vec2D(400f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, Integer.toString(visualAttributes.getId().getValue()), menuButtonLabelStyle, () -> {
                setEditMenuVisual(visualAttributes);
                addMenu(editVisualMenu);
            }));
            visualListIndex++;
        }
    }

    @Override
    public void start() {
        addMenu(editMenu);
        super.start();
    }

    private void setEditMenuVisual(VisualEditionData visualEditionData) {
        TextDisplay idDisplay = new TextDisplay(4, false, 600f, 700f, visualEditionData.getId().toString(), menuButtonLabelStyle, TextAlignment.LEFT);
        TextDisplay layerDisplay = new TextDisplay(4, false, 600f, 670f, visualEditionData.getLayer().toString(), menuButtonLabelStyle, TextAlignment.LEFT);
        TextDisplay sizeDisplay = new TextDisplay(4, false, 600f, 640f, visualEditionData.getSize().toString(), menuButtonLabelStyle, TextAlignment.LEFT);

        editVisualMenu.addVisual(idDisplay);
        editVisualMenu.addVisual(layerDisplay);
        editVisualMenu.addVisual(sizeDisplay);
    }
}
