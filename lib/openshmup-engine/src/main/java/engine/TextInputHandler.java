package engine;

import lombok.Getter;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

final public class TextInputHandler {

    final private GLFWCharCallbackI textInputCallback;

    final private GLFWKeyCallbackI keyInputCallback;

    final private long glfwWindow;
    @Getter final private ArrayList<Integer> textInputBuffer;
    @Getter final private StringBuffer targetBuffer;

    private int backspaceCount;
    @Getter
    private boolean targetGotModified;

    public TextInputHandler(long glfwWindow, StringBuffer targetBuffer) {
        this.glfwWindow = glfwWindow;
        this.targetBuffer = targetBuffer;
        this.textInputBuffer = new ArrayList<>();
        this.textInputCallback = (window, codepoint) -> textInputBuffer.add(codepoint);
        this.keyInputCallback = (win, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_BACKSPACE -> {
                        if (!textInputBuffer.isEmpty()) {
                            textInputBuffer.removeLast();
                        }
                        else {
                            backspaceCount += 1;
                        }
                    }
                }
            }
        };
        glfwSetCharCallback(glfwWindow, textInputCallback);
        glfwSetKeyCallback(glfwWindow, keyInputCallback);
        this.backspaceCount = 0;
        this.targetGotModified = false;
    }

    public void update() {
        if (!textInputBuffer.isEmpty()) {
            this.targetGotModified = true;
        }
        textInputBuffer.forEach(targetBuffer::appendCodePoint);
        textInputBuffer.clear();
        for (int i = 0; i < backspaceCount; i++) {
            if (!targetBuffer.isEmpty()) {
                targetBuffer.setLength(targetBuffer.length() - 1);
                this.targetGotModified = true;
            }
        }
        backspaceCount = 0;
    }
}
