import engine.Engine;
import engine.assets.Font;
import engine.scene.Scene;
import engine.visual.TextDisplay;
import engine.visual.style.TextAlignment;

import java.io.IOException;
import java.nio.file.Paths;

import static engine.Engine.window;

public class TestTextDisplay {

    public static void main(String[] args) throws IOException {

        Engine myEngine = new Engine();
        Engine.setNativeResolution(540, 1080);

        Scene testScene = new Scene();
        Engine.switchCurrentScene(testScene);
        try {

            Font myFont = Font.createFromTTF(Paths.get("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf"));
            myFont.getBitmap().loadInGPU();
            TextDisplay myTextDisplay = new TextDisplay(2, myFont, false, 24.0f / Engine.getNativeHeight(), 0.5f, 0.5f, "Hello World!\nThis is a test\nall sizes work, any number of lines work, this is heaven!", 1.0f, 1.0f, 1.0f, 1.0f, TextAlignment.CENTER);
            testScene.addVisual(myTextDisplay);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.show();

        myEngine.run();
    }
}
