package engine.hitbox;

import types.Vec2D;

import java.util.ArrayList;

public sealed interface Hitbox permits CompositeHitbox, RectangleBox {

    boolean containsPoint(Vec2D position);

    void setPosition(Vec2D position);

    void setSize(Vec2D size);

    void setOrientation(float orientationRadians);

    Hitbox copy();

    static boolean intersection(Hitbox hitbox, Hitbox otherHitbox) {
        if (hitbox == null || otherHitbox == null) {
            return false;
        }
        if (hitbox instanceof RectangleBox rectangleBox) {
            if (otherHitbox instanceof RectangleBox otherSimpleHitbox) {
                return !(rectangleBox.getDownBound() > otherSimpleHitbox.getUpBound()) && !(rectangleBox.getUpBound() < otherSimpleHitbox.getDownBound()) && !(rectangleBox.getRightBound() < otherSimpleHitbox.getLeftBound()) && !(rectangleBox.getLeftBound() > otherSimpleHitbox.getRightBound());
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
        if (otherHitbox instanceof RectangleBox otherSimpleHitbox) {
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
