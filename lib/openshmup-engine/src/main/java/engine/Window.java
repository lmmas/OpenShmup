package engine;

import engine.types.IVec2D;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

final public class Window {
    final private IVec2D resolution = new IVec2D(1920, 1080);
    private long glfwWindow;

    public Window(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    public int getWidth() {
        return resolution.x;
    }

    public int getHeight() {
        return resolution.y;
    }

    public void setResolution(int width, int height) {
        glfwSetWindowSize(glfwWindow, width, height);
        glViewport(0, 0, width, height);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null : "glfwGetVideoMode failure";
        resolution.x = width;
        resolution.y = height;
        glfwSetWindowPos(glfwWindow, (vidmode.width() - resolution.x) / 2, (vidmode.height() - resolution.y) / 2);
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public void show() {
        glfwShowWindow(glfwWindow);
    }

}
