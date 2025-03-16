package engine.scene;

import engine.graphics.Graphic;
import engine.graphics.MovingImage;

import java.util.ArrayList;

public class ScrollingBackGround implements SceneVisual{
    Scene scene;
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

    public ScrollingBackGround(String imagePath, Scene scene, float sizeX, float sizeY, float speed, boolean horizontalScrolling) {
        this.scene = scene;
        this.image1 = new MovingImage(imagePath, scene, 0);
        this.image2 = new MovingImage(imagePath, scene, 0);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.positionX1 = 0.5f;
        this.positionY1 = 0.5f;
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        if(horizontalScrolling){
            this.positionX2 = this.positionX1 - Math.signum(speed) * sizeX;
            this.positionY2 = positionY1;
        }
        else{
            this.positionX2 = positionX1;
            this.positionY2 = positionY1 - Math.signum(speed) * sizeY;
        }
        image1.setPosition(positionX1, positionY1);
        image1.setSize(sizeX, sizeY);
        image2.setPosition(positionX2, positionY2);
        image2.setSize(sizeX, sizeY);
        this.lastUpdateTimeSeconds = scene.getSceneTime();
        scene.addVisual(this);
    }

    @Override
    public Graphic<?,?>[] getGraphics() {
        return new MovingImage[]{image1,image2};
    }

    @Override
    public void update(){
        float currentime = scene.getSceneTime();
        float deltaTime = currentime - lastUpdateTimeSeconds;
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
        lastUpdateTimeSeconds = currentime;
    }

    @Override
    public void delete() {
        image1.delete();
        image2.delete();
    }
}
