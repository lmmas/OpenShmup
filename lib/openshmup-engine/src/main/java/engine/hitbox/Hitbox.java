package engine.hitbox;

import engine.types.Vec2D;

import java.util.ArrayList;

public sealed interface Hitbox permits EmptyHitbox, CompositeHitbox, SimpleRectangleHitbox {

    boolean containsPoint(Vec2D position);

    void setPosition(float positionX, float positionY);

    void setSize(float sizeX, float sizeY);

    void setOrientation(float orientationRadians);

    Hitbox copy();

    static Hitbox DEFAULT_EMPTY() {
        return EmptyHitbox.getInstance();
    }

    static boolean intersection(Hitbox hitbox, Hitbox otherHitbox) {
        if (hitbox instanceof EmptyHitbox) {
            return false;
        }
        if (otherHitbox instanceof EmptyHitbox) {
            return false;
        }
        if (hitbox instanceof SimpleRectangleHitbox simpleRectangleHitbox) {
            if (otherHitbox instanceof SimpleRectangleHitbox otherSimpleHitbox) {
                return !(simpleRectangleHitbox.downBound > otherSimpleHitbox.upBound) && !(simpleRectangleHitbox.upBound < otherSimpleHitbox.downBound) && !(simpleRectangleHitbox.rightBound < otherSimpleHitbox.leftBound) && !(simpleRectangleHitbox.leftBound > otherSimpleHitbox.rightBound);
            }
            CompositeHitbox otherCompositeHitbox = (CompositeHitbox) otherHitbox;
            ArrayList<Hitbox> rectangleList = otherCompositeHitbox.getRectangleList();
            for (Hitbox rectangle : rectangleList) {
                if (intersection(hitbox, rectangle)) {
                    return true;
                }
            }
            return false;
        }
        CompositeHitbox compositeHitbox = (CompositeHitbox) hitbox;
        if (otherHitbox instanceof SimpleRectangleHitbox otherSimpleHitbox) {
            for (Hitbox rectangle : compositeHitbox.getRectangleList()) {
                if (intersection(rectangle, otherSimpleHitbox)) {
                    return true;
                }
            }
            return false;
        }
        CompositeHitbox otherCompositeHitbox = (CompositeHitbox) otherHitbox;
        for (Hitbox rectangle : compositeHitbox.getRectangleList()) {
            for (Hitbox otherRectangle : otherCompositeHitbox.getRectangleList()) {
                if (intersection(rectangle, otherRectangle)) {
                    return true;
                }
            }
        }
        return false;
    }
}
