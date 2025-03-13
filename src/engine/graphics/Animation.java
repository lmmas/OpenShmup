package engine.graphics;

public class Animation{
    final private MovingImage image;
    final private AnimationInfo info;
    private float framePeriodSeconds;
    private int frameIndex = 0;
    private float timeOfLastFrame = 0.0f;

    public Animation(MovingImage image, AnimationInfo info, float framePeriodSeconds) {
        this.image = image;
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
    }


}
