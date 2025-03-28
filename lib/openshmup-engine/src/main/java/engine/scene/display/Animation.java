package engine.scene.display;

import engine.graphics.DynamicImage;
import engine.graphics.Graphic;
import engine.render.RenderInfo;
import engine.render.Texture;
import engine.scene.Scene;

import java.util.Optional;

public class Animation implements SceneDisplay {
    final private DynamicImage image;
    final private AnimationInfo info;
    final private boolean looping;
    private float framePeriodSeconds;
    private int frameIndex;
    private float timeOfLastFrame;

    public Animation(int layer, AnimationInfo info, float framePeriodSeconds, boolean looping, float sizeX, float sizeY) {
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.image = new DynamicImage(info.filepath(), layer, sizeX, sizeY);
        this.image.setTextureSize(info.frameSizeX(), info.frameSizeY());
        updateTexturePosition();
        this.timeOfLastFrame = 0.0f;
    }

    public Animation(DynamicImage image, AnimationInfo info, boolean looping, float framePeriodSeconds) {
        //this constructore is only used for deep copying
        this.image = image;
        this.info = info;
        this.looping = looping;
        this.framePeriodSeconds = framePeriodSeconds;
        this.frameIndex = 0;
        this.timeOfLastFrame = 0.0f;
    }
    private void updateTexturePosition(){
        float texturePositionX = frameIndex * info.strideX() + info.startPosX();
        float texturePositionY = frameIndex * info.strideY() + info.startPosY();
        image.setTexturePosition(texturePositionX, texturePositionY);
    }

    @Override
    public Animation copy() {
        return new Animation(image.copy(), info, looping, framePeriodSeconds);
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(image.getRenderInfo());
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    public void setSize(float sizeX, float sizeY) {
        image.setSize(sizeX, sizeY);
    }


    public DynamicImage getImage() {
        return image;
    }

    @Override
    public Graphic<?, ?>[] getGraphics() {
        return new Graphic[]{image};
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(image.getTexture());
    }

    @Override
    public void setScene(Scene scene) {
        this.timeOfLastFrame = scene.getSceneTimeSeconds();
    }

    @Override
    public void update(float currentTimeSeconds) {
        if(currentTimeSeconds >= timeOfLastFrame + framePeriodSeconds){
            frameIndex++;
            if(frameIndex >= info.frameCount()){
                if(looping){
                    frameIndex = 0;
                }
                else{
                    return;
                }
            }
            updateTexturePosition();
            timeOfLastFrame += framePeriodSeconds;
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        return !looping && frameIndex == info.frameCount() -1;
    }
}
