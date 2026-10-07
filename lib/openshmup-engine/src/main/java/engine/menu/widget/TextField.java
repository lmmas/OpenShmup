package engine.menu.widget;

import engine.Engine;
import engine.hitbox.HitboxClickDetector;
import engine.hitbox.SimpleRectangleHitbox;
import engine.input.InputStatesManager;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.scene.visual.style.TextStyle;
import engine.scene.visual.style.VisualGroup;
import layer.LayerMap;
import types.Vec2D;

import java.util.Map;

final public class TextField implements Widget {

    final private VisualGroup visualGroup;

    final private TextDisplay textInputDisplay;

    final private StringBuffer stringBuffer;

    final private HitboxClickDetector hitboxClickDetector;

    private boolean textInputActive;

    public TextField(int textLayer, Vec2D size, Vec2D position, TextStyle style, Map<SceneVisual, Integer> otherVisuals, String startingText) {
        this.stringBuffer = new StringBuffer(startingText);
        float textStartMargin = 6f;
        Vec2D textPosition = new Vec2D(position.x - (size.x / 2) + textStartMargin, position.y);
        this.textInputDisplay = new TextDisplay(true, textPosition, startingText, style, TextAlignment.LEFT);
        LayerMap<SceneVisual> visuals = new LayerMap<>(otherVisuals);
        visuals.add(textInputDisplay, textLayer);
        this.visualGroup = new VisualGroup(visuals);
        this.hitboxClickDetector = new HitboxClickDetector(new SimpleRectangleHitbox(position, size));
        this.textInputActive = false;
    }

    @Override
    public VisualGroup getVisualGroup() {
        return visualGroup;
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
