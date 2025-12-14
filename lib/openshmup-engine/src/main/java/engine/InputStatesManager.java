package engine;

import engine.types.GameControl;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

final public class InputStatesManager {
    final private long glfwWindow;
    final private HashMap<Integer, GameControl> controlsMap;
    final private ArrayList<Boolean> controlStatesList;
    private boolean leftClickState = false;
    final private double[] cursorPositionXBuffer;
    final private double[] cursorPositionYBuffer;
    final private Vec2D cursorPosition;

    public InputStatesManager(long glfwWindow) {
        this.glfwWindow = glfwWindow;
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
        this.cursorPositionXBuffer = new double[1];
        this.cursorPositionYBuffer = new double[1];
        this.cursorPosition = new Vec2D(0.0f, 0.0f);
    }

    public void updateInputStates() {
        for (Integer key : controlsMap.keySet()) {
            GameControl control = controlsMap.get(key);
            controlStatesList.set(control.ordinal(), (glfwGetKey(glfwWindow, key) == GLFW_PRESS));
        }

        leftClickState = glfwGetMouseButton(glfwWindow, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;

        glfwGetCursorPos(glfwWindow, cursorPositionXBuffer, cursorPositionYBuffer);
        cursorPosition.x = (float) (cursorPositionXBuffer[0] / Engine.window.getWidth());
        cursorPosition.y = 1.0f - (float) (cursorPositionYBuffer[0] / Engine.window.getHeight());
    }

    public List<Boolean> getGameControlStates() {
        ArrayList<Boolean> controlStatesCopy = new ArrayList<>(controlStatesList.size());
        controlStatesCopy.addAll(controlStatesList);
        return controlStatesCopy;
    }

    public boolean getLeftClickState() {
        return leftClickState;
    }

    public Vec2D getCursorPosition() {
        return new Vec2D(cursorPosition);
    }
}
