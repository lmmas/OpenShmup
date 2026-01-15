package json.converters.hitbox;

import engine.assets.Texture;
import engine.gameData.GamePaths;
import engine.level.entity.hitbox.CompositeHitbox;
import engine.level.entity.hitbox.Hitbox;
import engine.types.Vec2D;
import json.SafeJsonNode;

import static engine.Engine.assetManager;

public class CompositeHitboxFactory implements HitboxFactory {

    @Override
    public Hitbox fromJson(SafeJsonNode hitboxNode, GamePaths paths) {
        String textureFileName = hitboxNode.checkAndGetString("fileName");
        Texture texture = assetManager.getTexture(paths.gameTextureFolder.resolve(textureFileName));
        Vec2D size = hitboxNode.checkAndGetVec2D("size");
        return new CompositeHitbox(texture, size.x, size.y);
    }
}
