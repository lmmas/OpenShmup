package engine.menu.item;

import engine.Engine;
import engine.InputStatesManager;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TextField implements MenuItem {

    @Getter final private List<SceneVisual> visuals;

    final private TextDisplay textInputDisplay;

    final private StringBuffer stringBuffer;

    final private HitboxClickDetector hitboxClickDetector;

    private boolean textInputActive;

    public TextField(float sizeX, float sizeY, float positionX, float positionY, String startingText, TextStyle style, List<SceneVisual> otherVisuals) {
        this.stringBuffer = new StringBuffer(startingText);
        this.textInputDisplay = new TextDisplay(2, true, positionX - sizeX / 2, positionY, startingText, style, TextAlignment.LEFT);
        this.visuals = new ArrayList<>(otherVisuals);
        this.visuals.add(textInputDisplay);
        Hitbox clickHitbox = new SimpleRectangleHitbox(positionX, positionY, sizeX, sizeY);
        this.hitboxClickDetector = new HitboxClickDetector(clickHitbox);
        this.textInputActive = false;
    }


    @Override
    public void handleInputs() {
        InputStatesManager inputStatesManager = Engine.getInputStatesManager();
        boolean leftClickState = inputStatesManager.getLeftClickState();

        if (textInputActive) {
            textInputDisplay.setDisplayedString(stringBuffer.toString());
            if (leftClickState) {
                inputStatesManager.closeTextInput();
                textInputActive = false;
            }
        }

        else {
            if (hitboxClickDetector.result(leftClickState, inputStatesManager.getCursorPosition())) {
                inputStatesManager.addTextInput(stringBuffer);
                textInputActive = true;
            }
        }
    }
}
