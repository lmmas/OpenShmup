package engine;

import engine.scene.GameControl;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    final private long window;
    private HashMap<Integer, GameControl> inputList;
    public InputHandler(long window){
        this.window = window;
        this.inputList = new HashMap<>();
        inputList.put(GLFW_KEY_LEFT, GameControl.MOVE_LEFT);
        inputList.put(GLFW_KEY_RIGHT, GameControl.MOVE_RIGHT);
        inputList.put(GLFW_KEY_UP, GameControl.MOVE_UP);
        inputList.put(GLFW_KEY_DOWN, GameControl.MOVE_DOWN);
        inputList.put(GLFW_KEY_W, GameControl.FIRE);
        inputList.put(GLFW_KEY_X, GameControl.BOMB);
        inputList.put(GLFW_KEY_P, GameControl.PAUSE);
        inputList.put(GLFW_KEY_S, GameControl.SLOWDOWN);
    }

    public void updateControls(boolean[] controlStates){
        assert controlStates.length == GameControl.values().length: "invalid array length";
        for(Integer key: inputList.keySet()){
            GameControl control = inputList.get(key);
            controlStates[control.ordinal()] = glfwGetKey(window, key) == GLFW_PRESS;
        }
    }
}
