package engine;

final public class PlayerSettings {

    private WindowMode windowMode;

    private int windowWidth;

    private int windowHeight;

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setResolution(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public WindowMode getWindowMode() {
        return windowMode;
    }

    public enum WindowMode {
        FULLSCREEN,
        WINDOWED
    }
}

