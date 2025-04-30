import engine.Engine;
import engine.assets.TrueTypeFont;

import java.io.IOException;

public class TestFont {
    public static void main(String[] args) throws IOException {
        TrueTypeFont testFont = new TrueTypeFont("lib/openshmup-engine/src/test/resources/fonts/testFont.ttf");
    }
}
