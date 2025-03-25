package engine.scene.visual;

import engine.graphics.Graphic;
import engine.graphics.MovingImage;
import engine.scene.Scene;

public class ScrollingImage implements SceneVisual {
    private MovingImage image1;
    private float positionX1;
    private float positionY1;
    private MovingImage image2;
    private float positionX2;
    private float positionY2;
    private float sizeX;
    private float sizeY;
    boolean horizontalScrolling;
    private float speed;
    private float lastUpdateTimeSeconds;
    public ScrollingImage(String imagePath, int layer, float sizeX, float sizeY, float speed, boolean horizontalScrolling) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.image1 = new MovingImage(imagePath, layer, sizeX, sizeY);
        this.image2 = new MovingImage(imagePath, layer, sizeX, sizeY);
        this.positionX1 = 0.5f;
        this.positionY1 = 0.5f;
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        image1.setSize(sizeX, sizeY);
        image2.setSize(sizeX, sizeY);
        lastUpdateTimeSeconds = 0.0f;
        setPosition(0.5f, 0.5f);
    }

    public ScrollingImage(MovingImage image1, float positionX1, float positionY1, MovingImage image2, float positionX2, float positionY2, float sizeX, float sizeY, boolean horizontalScrolling, float speed) {
        this.image1 = image1;
        this.positionX1 = positionX1;
        this.positionY1 = positionY1;
        this.image2 = image2;
        this.positionX2 = positionX2;
        this.positionY2 = positionY2;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.horizontalScrolling = horizontalScrolling;
        this.speed = speed;
        this.lastUpdateTimeSeconds = 0.0f;
        setPosition(0.5f, 0.5f);
    }

    @Override
    public SceneVisual copy() {
        return new ScrollingImage(image1.copy(), positionX1, positionY1, image2.copy(), positionX2, positionY2, sizeX, sizeY, horizontalScrolling, speed);
    }

    @Override
    public Graphic<?,?>[] getGraphics() {
        return new MovingImage[]{image1,image2};
    }

    @Override
    public void setScene(Scene scene) {
        this.lastUpdateTimeSeconds = scene.getSceneTimeSeconds();
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        positionX1 = positionX;
        positionY1 = positionY;
        if(horizontalScrolling){
            this.positionX2 = this.positionX1 - Math.signum(speed) * sizeX;
            this.positionY2 = positionY1;
        }
        else{
            this.positionX2 = positionX1;
            this.positionY2 = positionY1 - Math.signum(speed) * sizeY;
        }
        image1.setPosition(positionX1, positionY1);
        image2.setPosition(positionX2, positionY2);
    }

    @Override
    public void update(float currentTimeSeconds){
        float deltaTime = currentTimeSeconds - lastUpdateTimeSeconds;
        if(horizontalScrolling){
            positionX1+= speed * deltaTime;
            positionX2+= speed * deltaTime;
            if(positionX1 > 0.5f + sizeX){
                positionX1 -= 2 * sizeX;
            }
            if(positionX1 < 0.5f - sizeX){
                positionX1 += 2 * sizeX;
            }
            if(positionX2 > 0.5f + sizeX){
                positionX2 -= 2 * sizeX;
            }
            if(positionX2 < 0.5f - sizeX){
                positionX2 += 2 * sizeX;
            }
        }
        else{
            positionY1 += speed * deltaTime;
            positionY2 += speed * deltaTime;
            if(positionY1 > 0.5f + sizeY){
                positionY1 -= 2 * sizeY;
            }
            if(positionY1 < 0.5f - sizeY){
                positionY1 += 2 * sizeY;
            }
            if(positionY2 > 0.5f + sizeY){
                positionY2 -= 2 * sizeY;
            }
            if(positionY2 < 0.5f - sizeY){
                positionY2 += 2 * sizeY;
            }
        }
        image1.setPosition(positionX1, positionY1);
        image2.setPosition(positionX2, positionY2);
        lastUpdateTimeSeconds = currentTimeSeconds;
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }
}
