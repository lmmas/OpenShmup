import engine.Engine;
import engine.graphics.GraphicsManager;
import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;

import java.io.IOException;

import static engine.Engine.setNativeResolution;
import static engine.Engine.window;

public class TestGraphic {

    public static void main(String[] args) throws IOException {

        Engine myEngine = new Engine();

        setNativeResolution(1920, 1080);

        GraphicsManager graphicsManager = Engine.getGraphicsManager();

        RoundedColorRectangle testRectangle = new RoundedColorRectangle(0.3f, 0.2f, 0.5f, 0.6f, 0.2f, 0.0f, 0.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testRectangle);

        RoundedRectangleBorder testBorder = new RoundedRectangleBorder(0.3f, 0.2f, 0.5f, 0.6f, 0.2f, 0.02f, 1.0f, 1.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testBorder);

        RoundedColorRectangle testRectangle2 = new RoundedColorRectangle(0.3f, 0.2f, 0.5f, 0.2f, 0.2f, 0.0f, 0.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testRectangle2);

        RoundedRectangleBorder testBorder2 = new RoundedRectangleBorder(0.3f, 0.2f, 0.5f, 0.2f, 0.2f, 0.02f, 1.0f, 1.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testBorder2);

        window.show();

        myEngine.run();
    }
}
