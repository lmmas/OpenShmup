package json.factories.hitbox;

import engine.assets.Texture;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.Hitbox;
import engine.types.Vec2D;
import json.SafeJsonNode;

import java.nio.file.Path;

import static engine.Engine.assetManager;

public class CompositeHitboxFactory implements HitboxFactory {

    @Override
    public Hitbox fromJson(SafeJsonNode hitboxNode) {
        String textureFileName = hitboxNode.checkAndGetString("fileName");
        Path hitboxFilePath = hitboxNode.getFilePath();
        Texture texture = assetManager.getTexture(hitboxFilePath.getParent().resolve("textures").resolve(textureFileName).toString());
        Vec2D size = hitboxNode.checkAndGetVec2D("size");
        return new CompositeHitbox(texture, size.x, size.y);
    }
}
