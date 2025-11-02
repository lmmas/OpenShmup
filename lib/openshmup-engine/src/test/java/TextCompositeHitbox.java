import engine.assets.Texture;
import engine.entity.hitbox.CompositeHitbox;

import java.io.IOException;

public class TextCompositeHitbox {
    public static void main(String[] args) throws IOException {
        CompositeHitbox testHitBox = new CompositeHitbox(Texture.createFromImageFile("lib/openshmup-engine/src/test/resources/textures/testHitbox.png"), 0.1f, 0.1f);
    }
}
