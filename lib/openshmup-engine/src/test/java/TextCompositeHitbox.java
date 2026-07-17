import engine.assets.Texture;
import engine.hitbox.CompositeHitbox;
import types.Vec2D;

import java.io.IOException;
import java.nio.file.Paths;

public class TextCompositeHitbox {

    public static void main(String[] args) throws IOException {
        CompositeHitbox testHitBox = new CompositeHitbox(Texture.createFromImageFile(Paths.get("lib/openshmup-engine/src/test/resources/textures/testHitbox.png")), new Vec2D(0.1f, 0.1f));
    }
}
