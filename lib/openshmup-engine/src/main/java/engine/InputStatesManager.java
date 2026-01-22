package engine;

import engine.types.GameControl;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static engine.Engine.window;
import static org.lwjgl.glfw.GLFW.*;

final public class InputStatesManager implements EngineSystem {

    final private long glfwWindow;

    final private HashMap<Integer, GameControl> controlsMap;

    final private ArrayList<Boolean> controlStatesList;

    final private ArrayList<Boolean> previousControlStatesList;

    final private double[] cursorPositionXBuffer;

    final private double[] cursorPositionYBuffer;

    final private Vec2D cursorPosition;

    private TextInputHandler textInputHandler;

    public InputStatesManager() {
        this.glfwWindow = window.getGlfwWindow();
        this.controlsMap = new HashMap<>();
        controlsMap.put(GLFW_KEY_LEFT, GameControl.MOVE_LEFT);
        controlsMap.put(GLFW_KEY_RIGHT, GameControl.MOVE_RIGHT);
        controlsMap.put(GLFW_KEY_UP, GameControl.MOVE_UP);
        controlsMap.put(GLFW_KEY_DOWN, GameControl.MOVE_DOWN);
        controlsMap.put(GLFW_KEY_Z, GameControl.FIRE);
        controlsMap.put(GLFW_KEY_X, GameControl.BOMB);
        controlsMap.put(GLFW_KEY_P, GameControl.PAUSE);
        controlsMap.put(GLFW_KEY_S, GameControl.SLOWDOWN);
        controlsMap.put(GLFW_KEY_T, GameControl.SLOWDOWN);
        controlsMap.put(GLFW_KEY_F3, GameControl.TOGGLE_DEBUG);
        this.controlStatesList = new ArrayList<>(Collections.nCopies(controlsMap.size(), Boolean.FALSE));
        this.previousControlStatesList = new ArrayList<>(Collections.nCopies(controlsMap.size(), Boolean.FALSE));
        this.cursorPositionXBuffer = new double[1];
        this.cursorPositionYBuffer = new double[1];
        this.cursorPosition = new Vec2D(0.0f, 0.0f);
        this.textInputHandler = null;
    }

    @Override
    public void update() {
        for (int i = 0; i < previousControlStatesList.size(); i++) {
            previousControlStatesList.set(i, controlStatesList.get(i));
        }

        boolean leftClickState = glfwGetMouseButton(glfwWindow, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
        controlStatesList.set(0, leftClickState);
        for (Integer key : controlsMap.keySet()) {
            GameControl control = controlsMap.get(key);
            controlStatesList.set(control.ordinal(), (glfwGetKey(glfwWindow, key) == GLFW_PRESS));
        }

        glfwGetCursorPos(glfwWindow, cursorPositionXBuffer, cursorPositionYBuffer);
        cursorPosition.x = (float) (cursorPositionXBuffer[0] / window.getWidth() * Engine.getNativeWidth());
        cursorPosition.y = (1.0f - (float) (cursorPositionYBuffer[0] / window.getHeight())) * Engine.getNativeHeight();

        if (textInputHandler != null) {
            textInputHandler.update();
        }
    }

    public List<Boolean> getGameControlStates() {
        ArrayList<Boolean> controlStatesCopy = new ArrayList<>(controlStatesList.size());
        controlStatesCopy.addAll(controlStatesList);
        return controlStatesCopy;
    }

    public boolean getLeftClickState() {
        return controlStatesList.getFirst();
    }

    public Vec2D getCursorPosition() {
        return new Vec2D(cursorPosition);
    }

    public void addTextInput(StringBuffer targetBuffer) {
        this.textInputHandler = new TextInputHandler(glfwWindow, targetBuffer);
    }

    public void closeTextInput() {
        glfwSetCharCallback(glfwWindow, null);
        this.textInputHandler = null;
    }

    public StringBuffer getTextInputTarget() {
        if (textInputHandler != null) {
            return textInputHandler.getTargetBuffer();
        }
        else {
            return null;
        }
    }
}
