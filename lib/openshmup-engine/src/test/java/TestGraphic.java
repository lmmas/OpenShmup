import engine.Engine;
import engine.graphics.GraphicsManager;
import engine.graphics.colorRectangle.ColorRectangleGraphic;
import engine.graphics.colorRoundedRectangle.RoundedColorRectangle;
import engine.graphics.roundedRectangleBorder.RoundedRectangleBorder;
import engine.level.entity.extraComponent.HitboxDebugRectangle;
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

        Vec2D graphicSize = new Vec2D(300f, 200f);
        float roundingRadius = 50f;
        float borderWidth = 5f;

        ColorRectangleGraphic colorRectangle1 = new ColorRectangleGraphic(graphicSize, new Vec2D(200f, 850f), RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(colorRectangle1);

        ColorRectangleGraphic colorRectangle4 = new ColorRectangleGraphic(graphicSize, new Vec2D(600f, 850f), RGBAValue.SOLID_WHITE, Engine.assetManager.getShader(HitboxDebugRectangle.hitboxDebugShader));
        graphicsManager.addDebugGraphic(colorRectangle4);

        ColorRectangleGraphic colorRectangle2 = new ColorRectangleGraphic(graphicSize, new Vec2D(975f, 875f), new RGBAValue(1.0f, 0.0f, 0.0f, 1.0f));
        graphicsManager.addDebugGraphic(colorRectangle2);

        ColorRectangleGraphic colorRectangle3 = new ColorRectangleGraphic(graphicSize, new Vec2D(1025f, 825f), new RGBAValue(0.0f, 1.0f, 0.0f, 0.5f));
        graphicsManager.addDebugGraphic(colorRectangle3);

        RoundedColorRectangle testRectangle2 = new RoundedColorRectangle(graphicSize, new Vec2D(200f, 600f), roundingRadius, RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(testRectangle2);

        RoundedRectangleBorder testBorder = new RoundedRectangleBorder(graphicSize, new Vec2D(600f, 600f), roundingRadius, borderWidth, RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(testBorder);

        RoundedColorRectangle testRectangle3 = new RoundedColorRectangle(graphicSize, new Vec2D(1000f, 600f), roundingRadius, new RGBAValue(1.0f, 0.0f, 0.0f, 1.0f));
        graphicsManager.addDebugGraphic(testRectangle3);

        RoundedRectangleBorder testBorder2 = new RoundedRectangleBorder(graphicSize, new Vec2D(1000f, 600f), roundingRadius, borderWidth, RGBAValue.SOLID_WHITE);
        graphicsManager.addDebugGraphic(testBorder2);

        window.show();

        Engine.run();
    }
}
