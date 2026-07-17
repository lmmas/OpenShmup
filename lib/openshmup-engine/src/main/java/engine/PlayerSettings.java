package engine;

import lombok.Getter;

@Getter
final public class PlayerSettings {

    private WindowMode windowMode;

    private int windowWidth;

    private int windowHeight;

    public void setResolution(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public enum WindowMode {
        FULLSCREEN,
        WINDOWED
    }
}

