import engine.Engine;
import engine.assets.Font;
import engine.assets.Texture;
import engine.graphics.Image2D;
import engine.scene.display.StaticImageDisplay;
import engine.scene.display.TextDisplay;
import engine.scene.spawnable.SceneDisplaySpawnInfo;

import java.io.IOException;

import static engine.Engine.editorDataManager;

public class TestTextDisplay {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";

        Runnable testInit = () ->{
            try{
                StaticImageDisplay testBackground = new StaticImageDisplay(new Image2D(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/background_white.jpg"), 0, false, 0.5f, 0.5f));
                editorDataManager.addCustomDisplays(1, testBackground);
                editorDataManager.getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(1, 0.25f, 0.25f));

                Font myFont = Font.createFromTTF("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");

                TextDisplay myTextDisplay = new TextDisplay(2, false, 0.5f, 0.5f, 24.0f, "Hello World!\nThis is a test\nall sizes work, any number of lines work, this is heaven!", myFont);
                editorDataManager.addCustomDisplays(2, myTextDisplay);
                editorDataManager.getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(2, 0.5f, 0.5f));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Engine myEngine = new Engine(folderName, testInit, () -> {});
    }
}
