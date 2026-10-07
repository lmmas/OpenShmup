import engine.Engine;
import engine.assets.Font;
import engine.scene.Scene;
import engine.visual.Text;
import engine.visual.style.TextAlignment;
import types.IVec2D;
import types.RGBAValue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static engine.Engine.window;

public class TestDynamicText {

    public static void main(String[] args) throws IOException {

        Engine.init();
        Engine.setNativeResolution(new IVec2D(1080, 1080));
        Engine.initGraphicsManager();

        Scene testScene = new Scene();
        Engine.switchCurrentScene(testScene);
        try {

            Font myFont = Font.createFromTTF(Paths.get("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf"));
            myFont.getBitmap().loadInGPU();
            String displayedString = "Hello World!";
            RGBAValue color = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
            Text myText = new Text(myFont, true, 24.0f, Engine.getNativeResolution().scalar(0.5f), displayedString, color, TextAlignment.CENTER);
            testScene.addVisual(myText, 0);
            AtomicInteger frameCount = new AtomicInteger();
            Runnable inLoopScript = () -> {
                frameCount.getAndIncrement();
                if (frameCount.get() == 3) {
                    myText.setToRemove(true);
                }
            };
            Engine.setInLoopScript(inLoopScript);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.show();

        Engine.run();
    }
}
