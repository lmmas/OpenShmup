package editor.scenes;

import editor.attribute.EditorGameDataManager;
import editor.objectAttributes.visual.VisualAttributes;
import engine.Engine;
import engine.scene.Scene;
import engine.scene.menu.MenuItem;
import engine.scene.menu.MenuScreen;
import engine.scene.menu.item.RoundedRectangleButton;
import engine.types.Vec2D;
import engine.visual.BorderedRoundedRectangle;
import engine.visual.TextDisplay;

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
        this.editVisualMenu = new MenuScreen(3, List.of(), List.of(new BorderedRoundedRectangle(3, 1500f, 900f, Engine.getNativeWidth() / 2, Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor.r, menuButtonColor.g, menuButtonColor.b, menuButtonColor.a, menuButtonBorderColor.r, menuButtonBorderColor.g, menuButtonColor.b, menuButtonBorderColor.a)));

        List<VisualAttributes> visualAttributesList = gameData.getVisualAttributesList();
        int visualListIndex = 0;
        for (var visualAttributes : visualAttributesList) {
            editMenu.menuItems().add(new RoundedRectangleButton(1, new Vec2D(400f, 50f), new Vec2D((float) Engine.getNativeWidth() / 2, 900f - (visualListIndex * (50f + 15f))), menuButtonRoundingRadius, menuButtonBorderWidth, menuButtonColor, menuButtonBorderColor, Integer.toString(visualAttributes.getId().getValue()), menuButtonLabelStyle, () -> {
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

    private void setEditMenuVisual(VisualAttributes visualAttributes) {
        TextDisplay idDisplay = new TextDisplay(4, false, 600f, 700f, visualAttributes.getId().toString(), menuButtonLabelStyle);
        TextDisplay layerDisplay = new TextDisplay(4, false, 600f, 670f, visualAttributes.getLayer().toString(), menuButtonLabelStyle);
        TextDisplay sizeDisplay = new TextDisplay(4, false, 600f, 640f, visualAttributes.getSize().toString(), menuButtonLabelStyle);

        editVisualMenu.otherVisuals().add(idDisplay);
        editVisualMenu.otherVisuals().add(layerDisplay);
        editVisualMenu.otherVisuals().add(sizeDisplay);
    }
}
