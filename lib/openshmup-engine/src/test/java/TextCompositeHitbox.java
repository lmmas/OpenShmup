import engine.assets.Texture;
import engine.entity.hitbox.CompositeHitbox;

import java.io.FileNotFoundException;

public class TextCompositeHitbox {
    public static void main(String[] args) throws FileNotFoundException {
        CompositeHitbox testHitBox = new CompositeHitbox(new Texture("lib/openshmup-engine/src/test/resources/textures/testHitbox.png"), 0.1f, 0.1f);
    }
}
