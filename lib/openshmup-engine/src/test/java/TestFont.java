import engine.assets.Font;

import java.io.IOException;
import java.nio.file.Paths;

public class TestFont {

    public static void main(String[] args) throws IOException {
        Font testFont = Font.createFromTTF(Paths.get("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf"));
    }
}
