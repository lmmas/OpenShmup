import engine.Engine;
import engine.assets.Font;
import engine.assets.FontCharInfo;
import engine.assets.Texture;
import engine.graphics.Image2D;
import engine.graphics.StaticImage;
import engine.scene.display.StaticImageDisplay;
import engine.scene.display.TextBox;
import engine.scene.spawnable.SceneDisplaySpawnInfo;
import engine.types.Vec2D;

import java.io.IOException;
import java.util.Optional;

public class TestTextBox {
    public static void main(String[] args) throws IOException {
        String folderName = "testGame";
        Engine myEngine = new Engine(folderName);

        /*StaticImageDisplay testBackground = new StaticImageDisplay(new StaticImage(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/background_white.jpg"), 0,1.0f, 1.0f));
        myEngine.getEditorDataManager().addCustomDisplays(1, testBackground);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(1, 0.5f, 0.5f));
*/
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
        TextBox myTextBox = new TextBox(2, false, 0.5f, 0.5f, 200.0f, "Hello World!\nThis is a test", myFont);
        myEngine.getEditorDataManager().addCustomDisplays(2, myTextBox);
        myEngine.getEditorDataManager().getTimeline(0).addSpawnable(0.0f, new SceneDisplaySpawnInfo(2, 0.5f, 0.5f));

        myEngine.run();
    }
}
