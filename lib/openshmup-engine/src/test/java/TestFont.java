import engine.assets.Font;

import java.io.IOException;

public class TestFont {

    public static void main(String[] args) throws IOException {
        Font testFont = Font.createFromTTF("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");
    }
}
