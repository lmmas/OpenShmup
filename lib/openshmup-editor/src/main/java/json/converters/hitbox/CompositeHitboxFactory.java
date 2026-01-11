package json.converters.hitbox;

import engine.assets.Texture;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.Hitbox;
import engine.gameData.GamePaths;
import engine.types.Vec2D;
import json.SafeJsonNode;

import static engine.Engine.assetManager;

public class CompositeHitboxFactory implements HitboxFactory {

    @Override
    public Hitbox fromJson(SafeJsonNode hitboxNode, GamePaths paths) {
        String textureFileName = hitboxNode.checkAndGetString("fileName");
        Texture texture = assetManager.getTexture(paths.gameTextureFolder + textureFileName);
        Vec2D size = hitboxNode.checkAndGetVec2D("size");
        return new CompositeHitbox(texture, size.x, size.y);
    }
}
