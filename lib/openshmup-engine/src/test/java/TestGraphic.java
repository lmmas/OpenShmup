import engine.Engine;
import engine.graphics.GraphicsManager;
import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import types.IVec2D;
import types.RGBAValue;
import types.Vec2D;

import java.io.IOException;

import static engine.Engine.setNativeResolution;
import static engine.Engine.window;

public class TestGraphic {

    public static void main(String[] args) throws IOException {

        Engine.init();
        setNativeResolution(new IVec2D(1920, 1080));
        Engine.initGraphicsManager();
        GraphicsManager graphicsManager = Engine.getGraphicsManager();

        RoundedColorRectangle testRectangle = new RoundedColorRectangle(new Vec2D(300f, 200f), new Vec2D(500f, 600f), 10f, new RGBAValue(0.0f, 0.0f, 1.0f, 1.0f));
        graphicsManager.addDebugGraphic(testRectangle);

        RoundedRectangleBorder testBorder = new RoundedRectangleBorder(new Vec2D(300f, 200f), new Vec2D(500f, 600f), 10f, 2f, RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(testBorder);

        RoundedColorRectangle testRectangle2 = new RoundedColorRectangle(new Vec2D(300f, 200f), new Vec2D(500f, 200f), 10f, new RGBAValue(0.0f, 0.0f, 1.0f, 1.0f));
        graphicsManager.addDebugGraphic(testRectangle2);

        RoundedRectangleBorder testBorder2 = new RoundedRectangleBorder(new Vec2D(300f, 200f), new Vec2D(500f, 200f), 10f, 2f, RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(testBorder2);

        window.show();

        Engine.run();
    }
}
