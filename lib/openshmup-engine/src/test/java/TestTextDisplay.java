import engine.Engine;
import engine.assets.Font;
import engine.assets.Texture;
import engine.graphics.StaticImage;
import engine.scene.display.StaticImageDisplay;
import engine.scene.display.TextDisplay;
import engine.scene.spawnable.SceneDisplaySpawnInfo;

import java.io.IOException;

public class TestTextDisplay {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";
        Engine myEngine = new Engine(folderName);

        StaticImageDisplay testBackground = new StaticImageDisplay(new StaticImage(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/background_white.jpg"), 0,0.5f, 0.5f));
        myEngine.getEditorDataManager().addCustomDisplays(1, testBackground);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(1, 0.75f, 0.75f));

        Font myFont = Font.createFromTTF("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");
        /*StaticImageDisplay fontBitmap = new StaticImageDisplay(new StaticImage(myFont.getBitmap(), 1,0.35f, 0.35f));
        myEngine.getEditorDataManager().addCustomDisplays(3, fontBitmap);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(3, 0.2f, 0.75f));
*/
        /*StaticImage aCharacterImage = new StaticImage(myFont.getBitmap(), 5, 0.2f, 0.2f);
        StaticImageDisplay aCharacterDisplay = new StaticImageDisplay(aCharacterImage);
        Optional<FontCharInfo> fontCharInfoOptional = myFont.getCharInfo("a".codePointAt(0));
        FontCharInfo newCharInfo = fontCharInfoOptional.orElseThrow();
        Vec2D textureSize = newCharInfo.bitmapTextureSize();
        aCharacterImage.setTextureSize(textureSize.x, textureSize.y);
        Vec2D texturePosition = newCharInfo.bitmapTexturePosition();
        aCharacterImage.setTexturePosition(texturePosition.x, texturePosition.y);
        myEngine.getEditorDataManager().addCustomDisplays(4, aCharacterDisplay);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(4, 0.8f, 0.8f));
*/
        TextDisplay myTextDisplay = new TextDisplay(2, false, 0.5f, 0.5f, 100.0f, "Hello World!\nThis is a test", myFont);
        myEngine.getEditorDataManager().addCustomDisplays(2, myTextDisplay);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(2, 0.5f, 0.5f));

        myEngine.run();
    }
}
