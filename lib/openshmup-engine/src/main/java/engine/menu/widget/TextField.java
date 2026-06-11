package engine.menu.widget;

import engine.Engine;
import engine.InputStatesManager;
import engine.hitbox.Hitbox;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.types.Vec2D;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

final public class TextField implements Widget {

    @Getter final private List<SceneVisual> visuals;

    final private TextDisplay textInputDisplay;

    final private StringBuffer stringBuffer;

    final private HitboxClickDetector hitboxClickDetector;

    private boolean textInputActive;

    public TextField(int textLayer, Vec2D size, Vec2D position, TextStyle style, List<SceneVisual> otherVisuals, String startingText) {
        this.stringBuffer = new StringBuffer(startingText);
        float textStartMargin = 6f;
        Vec2D textPosition = new Vec2D(position.x - (size.x / 2) + textStartMargin, position.y);
        this.textInputDisplay = new TextDisplay(textLayer, true, textPosition, startingText, style, TextAlignment.LEFT);
        this.visuals = new ArrayList<>(otherVisuals);
        this.visuals.add(textInputDisplay);
        Hitbox clickHitbox = new SimpleRectangleHitbox(position, size);
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

    public String getStringValue() {
        return stringBuffer.toString();
    }

    public void setStringValue(String value) {
        stringBuffer.setLength(0);
        stringBuffer.append(value);
        textInputDisplay.setDisplayedString(value);
    }
}
