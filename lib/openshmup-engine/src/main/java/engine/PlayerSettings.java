package engine;

final public class PlayerSettings {
    private static WindowMode windowMode;
    private static int windowWidth;

    private static int windowHeight;
    public static int getWindowWidth() {
        return windowWidth;
    }
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
    public static WindowMode getWindowMode() {
        return windowMode;
    }
    public enum WindowMode{
        FULLSCREEN,
        WINDOWED
    }
}

