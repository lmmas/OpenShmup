import engine.Engine;
import engine.assets.Font;
import engine.assets.Texture;
import engine.graphics.StaticImage;
import engine.scene.display.StaticImageDisplay;
import engine.scene.display.TextBox;
import engine.scene.spawnable.SceneDisplaySpawnInfo;

import java.io.IOException;

public class TestTextBox {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";
        Engine myEngine = new Engine(folderName);

        /*StaticImageDisplay testBackground = new StaticImageDisplay(new StaticImage(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/background_white.jpg"), 0,1.0f, 1.0f));
        myEngine.getEditorDataManager().addCustomDisplays(1, testBackground);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(1, 0.5f, 0.5f));
*/
        Font myFont = Font.createFromTTF("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");
        StaticImageDisplay fontBitmap = new StaticImageDisplay(new StaticImage(myFont.getBitmap(), 1,0.5f, 0.5f));
        myEngine.getEditorDataManager().addCustomDisplays(3, fontBitmap);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(3, 0.5f, 0.5f));

        TextBox myTextBox = new TextBox(2, false, 0.5f, 1.0f, 50.0f, "Hello World!", myFont);
        myEngine.getEditorDataManager().addCustomDisplays(2, myTextBox);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(1.0f, new SceneDisplaySpawnInfo(2, 0.5f, 0.5f));
        myEngine.run();
    }
}
