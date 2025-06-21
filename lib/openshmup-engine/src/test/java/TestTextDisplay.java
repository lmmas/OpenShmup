import engine.Engine;
import engine.assets.Font;
import engine.assets.Texture;
import engine.graphics.Image2D;
import engine.scene.display.StaticImageDisplay;
import engine.scene.display.TextDisplay;
import engine.scene.spawnable.SceneDisplaySpawnInfo;

import java.io.IOException;

public class TestTextDisplay {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";
        Engine myEngine = new Engine(folderName);

        StaticImageDisplay testBackground = new StaticImageDisplay(new Image2D(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/background_white.jpg"), 0, false, 0.5f, 0.5f));
        myEngine.getEditorDataManager().addCustomDisplays(1, testBackground);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(1, 0.75f, 0.75f));

        Font myFont = Font.createFromTTF("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");

        TextDisplay myTextDisplay = new TextDisplay(2, false, 0.5f, 0.5f, 24.0f, "Hello World!\nThis is a test\nall sizes work, any number of lines work, this is heaven!", myFont);
        myEngine.getEditorDataManager().addCustomDisplays(2, myTextDisplay);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(2, 0.5f, 0.5f));

        myEngine.run();
    }
}
