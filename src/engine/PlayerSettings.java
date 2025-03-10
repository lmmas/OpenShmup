package engine;

final public class PlayerSettings {
    private static int windowWidth;
    public static int getWindowWidth() {
        return windowWidth;
    }

    private static int windowHeight;
    public static int getWindowHeight() {
        return windowHeight;
    }

    public static float[] getResolution(){
        return new float[] {windowWidth, windowHeight};
    }

    public static void setResolution(int width, int height){
        windowWidth = width;
        windowHeight = height;
    }
    public enum WindowMode{
        FULLSCREEN,
        WINDOWED
    };
    private static WindowMode windowMode;
    public static WindowMode getWindowMode() {
        return windowMode;
    }
}

