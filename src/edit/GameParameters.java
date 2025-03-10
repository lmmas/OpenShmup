package edit;

import engine.PlayerSettings;

final public class GameParameters {
    private static int editionWidth;
    public static int getEditionWidth() {
        return editionWidth;
    }
    public static void setEditionWidth(int editionWidth) {
        GameParameters.editionWidth = editionWidth;
    }

    private static int editionHeight;
    public static int getEditionHeight() {
        return editionHeight;
    }
    public static void setEditionHeight(int editionHeight) {
        GameParameters.editionHeight = editionHeight;
    }

    public static void useDefaultParameters(){
        PlayerSettings.setResolution(800, 600);
        editionWidth = 800;
        editionHeight = 600;
    }
}
