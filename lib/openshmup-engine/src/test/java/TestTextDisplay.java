import engine.Engine;
import engine.assets.Font;
import engine.scene.Scene;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.RGBAValue;

import java.io.IOException;
import java.nio.file.Paths;

import static engine.Engine.window;

public class TestTextDisplay {

    public static void main(String[] args) throws IOException {

        Engine.init();
        Engine.setNativeResolution(1080, 1080);
        Engine.initGraphicsManager();

        Scene testScene = new Scene();
        Engine.switchCurrentScene(testScene);
        try {

            Font myFont = Font.createFromTTF(Paths.get("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf"));
            myFont.getBitmap().loadInGPU();
            String displayedString = "Hello World!\nThis is a test\nall sizes work, any number of lines work, this is heaven!";
            TextDisplay myTextDisplay = new TextDisplay(0, myFont, false, 24.0f, Engine.getNativeResolution().scalar(0.5f), displayedString, new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f), TextAlignment.CENTER);
            testScene.addVisual(myTextDisplay);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.show();

        Engine.run();
    }
}
