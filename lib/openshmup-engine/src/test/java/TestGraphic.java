import engine.Engine;
import engine.graphics.colorRoundedRectangle.ColorRoundedRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;

import java.io.IOException;

import static engine.Application.graphicsManager;

public class TestGraphic {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";


        Runnable testInit = () ->{

            ColorRoundedRectangle testRectangle = new ColorRoundedRectangle(0.3f, 0.2f, 0.5f, 0.6f, 0.1f, 0.0f, 0.0f, 1.0f, 1.0f);
            graphicsManager.addDebugGraphic(testRectangle);

            RoundedRectangleBorder testBorder = new RoundedRectangleBorder(0.3f, 0.2f, 0.5f, 0.6f, 0.1f, 0.02f, 1.0f, 1.0f, 1.0f, 1.0f);
            graphicsManager.addDebugGraphic(testBorder);

            ColorRoundedRectangle testRectangle2 = new ColorRoundedRectangle(0.3f, 0.2f, 0.5f, 0.2f, 0.1f, 0.0f, 0.0f, 1.0f, 1.0f);
            graphicsManager.addDebugGraphic(testRectangle2);

            RoundedRectangleBorder testBorder2 = new RoundedRectangleBorder(0.3f, 0.2f, 0.5f, 0.2f, 0.1f, 0.02f, 1.0f, 1.0f, 1.0f, 1.0f);
            graphicsManager.addDebugGraphic(testBorder2);
        };

        Engine myEngine = new Engine(folderName, testInit, () -> {});
    }
}
