package engine;

import engine.scene.GameControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

final public class InputStatesManager {
    final private long window;
    final private HashMap<Integer, GameControl> controlsMap;
    final private ArrayList<Boolean> controlStatesList;
    public InputStatesManager(long window){
        this.window = window;
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
    }

    public void updateControlStates(){
        for(Integer key: controlsMap.keySet()){
            GameControl control = controlsMap.get(key);
            controlStatesList.set(control.ordinal(), (glfwGetKey(window, key) == GLFW_PRESS));
        }
    }

    public List<Boolean> getControlStates(){
        ArrayList<Boolean> controlStatesCopy = new ArrayList<>(controlStatesList.size());
        controlStatesCopy.addAll(controlStatesList);
        return controlStatesCopy;
    }
}
