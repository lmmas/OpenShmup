package engine.menu.widget;

import engine.Engine;
import engine.hitbox.HitboxClickListener;
import engine.hitbox.RectangleBox;
import engine.input.InputStatesManager;
import engine.visual.Text;
import engine.visual.Visual;
import engine.visual.VisualGroup;
import engine.visual.style.TextAlignment;
import engine.visual.style.TextStyle;
import layer.LayerMap;
import types.Vec2D;

import java.util.Map;

final public class TextField implements Widget {

    final private VisualGroup visualGroup;

    final private Text textInputDisplay;

    final private StringBuffer stringBuffer;

    final private HitboxClickListener hitboxClickListener;

    private boolean textInputActive;

    public TextField(int textLayer, Vec2D size, Vec2D position, TextStyle style, Map<Visual, Integer> otherVisuals, String startingText) {
        this.stringBuffer = new StringBuffer(startingText);
        float textStartMargin = 6f;
        Vec2D textPosition = new Vec2D(position.x - (size.x / 2) + textStartMargin, position.y);
        this.textInputDisplay = new Text(true, textPosition, startingText, style, TextAlignment.LEFT);
        LayerMap<Visual> visuals = new LayerMap<>(otherVisuals);
        visuals.add(textInputDisplay, textLayer);
        this.visualGroup = new VisualGroup(visuals);
        this.hitboxClickListener = new HitboxClickListener(new RectangleBox(position, size));
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
            if (hitboxClickListener.result(leftClickState, inputStatesManager.getCursorPosition())) {
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
