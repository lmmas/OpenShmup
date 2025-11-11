import engine.Engine;
import engine.assets.Font;
import engine.assets.Texture;
import engine.graphics.colorRoundedRectangle.ColorRoundedRectangle;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.visual.ImageDisplay;
import engine.visual.TextDisplay;

import java.io.IOException;

import static engine.Application.graphicsManager;
import static engine.Engine.gameDataManager;

public class TestGraphic {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";


        Runnable testInit = () ->{
            ColorRoundedRectangle testRectangle = new ColorRoundedRectangle(0.7f, 0.5f, 0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f);
            graphicsManager.insertNewLayer(0);
            graphicsManager.addDebugGraphic(testRectangle);
        };

        Engine myEngine = new Engine(folderName, testInit, () -> {});
    }
}
