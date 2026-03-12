import engine.Engine;
import engine.graphics.GraphicsManager;
import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;

import java.io.IOException;

import static engine.Engine.setNativeResolution;
import static engine.Engine.window;

public class TestGraphic {

    public static void main(String[] args) throws IOException {

        Engine.init();
        setNativeResolution(1920, 1080);
        Engine.initGraphicsManager();
        GraphicsManager graphicsManager = Engine.getGraphicsManager();

        RoundedColorRectangle testRectangle = new RoundedColorRectangle(300f, 200f, 500f, 600f, 10f, 0.0f, 0.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testRectangle);

        RoundedRectangleBorder testBorder = new RoundedRectangleBorder(300f, 200f, 500f, 600f, 10f, 2f, 1.0f, 1.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testBorder);

        RoundedColorRectangle testRectangle2 = new RoundedColorRectangle(300f, 200f, 500f, 200f, 10f, 0.0f, 0.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testRectangle2);

        RoundedRectangleBorder testBorder2 = new RoundedRectangleBorder(300f, 200f, 500f, 200f, 10f, 2f, 1.0f, 1.0f, 1.0f, 1.0f);
        graphicsManager.addDebugGraphic(testBorder2);

        window.show();

        Engine.run();
    }
}
