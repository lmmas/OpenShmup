package engine.scene.display;

import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.types.Vec2D;

import java.util.List;

final public class ScrollingImage implements SceneVisual {
    private final Image2D image1;
    final private Vec2D position1;
    private final Image2D image2;
    final private Vec2D position2;
    private float sizeX;
    private float sizeY;
    boolean horizontalScrolling;
    private float speed;
    private float lastUpdateTimeSeconds;

    public ScrollingImage(Texture texture, int layer, float sizeX, float sizeY, float speed, boolean horizontalScrolling) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.image1 = new Image2D(texture, layer, true, sizeX, sizeY);
        this.image2 = new Image2D(texture, layer, true, sizeX, sizeY);
        this.position1 = new Vec2D(0.5f, 0.5f);
        this.position2 = new Vec2D(0.0f, 0.0f);
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        image1.setScale(sizeX, sizeY);
        image2.setScale(sizeX, sizeY);
        lastUpdateTimeSeconds = 0.0f;
        setPosition(0.5f, 0.5f);
    }

    public ScrollingImage(ScrollingImage scrollingImage){
        this.image1 = scrollingImage.image1.copy();
        this.position1 = new Vec2D(scrollingImage.position1);
        this.position2 = new Vec2D(scrollingImage.position2);
        this.image2 = scrollingImage.image2.copy();
        this.sizeX = scrollingImage.sizeX;
        this.sizeY = scrollingImage.sizeY;
        this.horizontalScrolling = scrollingImage.horizontalScrolling;
        this.speed = scrollingImage.speed;
        this.lastUpdateTimeSeconds = scrollingImage.lastUpdateTimeSeconds;
    }
    @Override
    public SceneVisual copy() {
        return new ScrollingImage(this);
    }

    @Override
    public List<RenderInfo> getRenderInfos() {
        return List.of(image1.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image1, image2);
    }

    @Override
    public List<Texture> getTextures() {
        return List.of(image1.getTexture());
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        position1.x = positionX;
        position1.y = positionY;
        if(horizontalScrolling){
            this.position2.x = this.position1.x - Math.signum(speed) * sizeX;
            this.position2.y = position1.y;
        }
        else{
            this.position2.x = position1.x;
            this.position2.y = position1.y - Math.signum(speed) * sizeY;
        }
        image1.setPosition(position1.x, position1.y);
        image2.setPosition(position2.x, position2.y);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        image1.setScale(scaleX, scaleY);
        image2.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay(float startingTimeSeconds) {
        this.lastUpdateTimeSeconds = startingTimeSeconds;
    }

    @Override
    public void update(float currentTimeSeconds){
        float deltaTime = currentTimeSeconds - lastUpdateTimeSeconds;
        if(horizontalScrolling){
            position1.x += speed * deltaTime;
            position2.x+= speed * deltaTime;
            if(position1.x > 0.5f + sizeX){
                position1.x -= 2 * sizeX;
            }
            if(position1.x < 0.5f - sizeX){
                position1.x += 2 * sizeX;
            }
            if(position2.x > 0.5f + sizeX){
                position2.x -= 2 * sizeX;
            }
            if(position2.x < 0.5f - sizeX){
                position2.x += 2 * sizeX;
            }
        }
        else{
            position1.y += speed * deltaTime;
            position2.y += speed * deltaTime;
            if(position1.y > 0.5f + sizeY){
                position1.y -= 2 * sizeY;
            }
            if(position1.y < 0.5f - sizeY){
                position1.y += 2 * sizeY;
            }
            if(position2.y > 0.5f + sizeY){
                position2.y -= 2 * sizeY;
            }
            if(position2.y < 0.5f - sizeY){
                position2.y += 2 * sizeY;
            }
        }
        image1.setPosition(position1.x, position1.y);
        image2.setPosition(position2.x, position2.y);
        lastUpdateTimeSeconds = currentTimeSeconds;
    }
}
