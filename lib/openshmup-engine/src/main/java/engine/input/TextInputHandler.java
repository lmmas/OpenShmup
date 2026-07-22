package engine.input;

import engine.assets.Font;
import lombok.Getter;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

final public class TextInputHandler {

    final private GLFWCharCallbackI textInputCallback;

    final private GLFWKeyCallbackI keyInputCallback;

    final private long glfwWindow;
    @Getter
    final private ArrayList<Integer> rawInputBuffer;
    @Getter
    final private StringBuffer targetBuffer;

    private int backspaceCount;

    public TextInputHandler(long glfwWindow, StringBuffer targetBuffer) {
        this.glfwWindow = glfwWindow;
        this.targetBuffer = targetBuffer;
        this.rawInputBuffer = new ArrayList<>();
        this.textInputCallback = (window, codepoint) -> rawInputBuffer.add(codepoint);
        this.keyInputCallback = (win, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_BACKSPACE -> backspaceCount += 1;
                }
            }
        };
        glfwSetCharCallback(this.glfwWindow, textInputCallback);
        glfwSetKeyCallback(this.glfwWindow, keyInputCallback);
        this.backspaceCount = 0;
    }

    public void update() {
        List<Integer> filteredBuffer = rawInputBuffer.stream().filter(Font::codepointIsSupported).toList();
        int validCharCount = filteredBuffer.size();
        filteredBuffer = filteredBuffer.subList(0, Integer.max(0, (validCharCount - backspaceCount)));
        filteredBuffer.forEach(targetBuffer::appendCodePoint);
        if(backspaceCount > validCharCount){
            backspaceCount = backspaceCount - validCharCount;
        }
        else{
            backspaceCount = 0;
        }
        for (int i = 0; i < backspaceCount; i++) {
            if (!targetBuffer.isEmpty()) {
                targetBuffer.setLength(targetBuffer.length() - 1);
            }
        }
        backspaceCount = 0;
        rawInputBuffer.clear();
    }
}
