package engine;

import engine.types.IVec2D;
import lombok.Getter;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

final public class Window {

    private IVec2D resolution = new IVec2D(1920, 1080);
    @Getter
    private final long glfwWindow;

    public Window(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    public int getWidth() {
        return resolution.x;
    }

    public int getHeight() {
        return resolution.y;
    }

    public void setResolution(IVec2D newResolution) {
        glfwSetWindowSize(glfwWindow, newResolution.x, newResolution.y);
        glViewport(0, 0, newResolution.x, newResolution.y);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null : "glfwGetVideoMode failure";
        glfwSetWindowPos(glfwWindow, (vidmode.width() - newResolution.x) / 2, (vidmode.height() - newResolution.y) / 2 - 25);
        this.resolution = newResolution;
    }

    public void show() {
        glfwShowWindow(glfwWindow);
    }

}
