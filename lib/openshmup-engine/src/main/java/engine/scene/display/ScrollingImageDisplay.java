package engine.scene.display;

import engine.graphics.Graphic;
import engine.graphics.Image2D;
import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.scene.Scene;
import engine.types.Vec2D;

import java.util.List;
import java.util.Optional;

final public class ScrollingImageDisplay implements SceneDisplay {
    private final Image2D image1;
    final private Vec2D position1;
    private final Image2D image2;
    final private Vec2D position2;
    private float sizeX;
    private float sizeY;
    boolean horizontalScrolling;
    private float speed;
    private float lastUpdateTimeSeconds;
    public ScrollingImageDisplay(Texture texture, int layer, float sizeX, float sizeY, float speed, boolean horizontalScrolling) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.image1 = new Image2D(texture, layer, true, sizeX, sizeY);
        this.image2 = new Image2D(texture, layer, true, sizeX, sizeY);
        this.position1 = new Vec2D(0.5f, 0.5f);
        this.position2 = new Vec2D(0.0f, 0.0f);
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        image1.setSize(sizeX, sizeY);
        image2.setSize(sizeX, sizeY);
        lastUpdateTimeSeconds = 0.0f;
        setPosition(0.5f, 0.5f);
    }

    public ScrollingImageDisplay(ScrollingImageDisplay scrollingImageDisplay){
        this.image1 = scrollingImageDisplay.image1.copy();
        this.position1 = new Vec2D(scrollingImageDisplay.position1);
        this.position2 = new Vec2D(scrollingImageDisplay.position2);
        this.image2 = scrollingImageDisplay.image2.copy();
        this.sizeX = scrollingImageDisplay.sizeX;
        this.sizeY = scrollingImageDisplay.sizeY;
        this.horizontalScrolling = scrollingImageDisplay.horizontalScrolling;
        this.speed = scrollingImageDisplay.speed;
        this.lastUpdateTimeSeconds = scrollingImageDisplay.lastUpdateTimeSeconds;
    }
    @Override
    public SceneDisplay copy() {
        return new ScrollingImageDisplay(this);
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(image1.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image1, image2);
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(image1.getTexture());
    }

    @Override
    public void setScene(Scene scene) {
        this.lastUpdateTimeSeconds = scene.getSceneTimeSeconds();
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

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }
}
