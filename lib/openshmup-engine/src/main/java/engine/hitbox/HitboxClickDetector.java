package engine.hitbox;

import engine.types.Vec2D;

final public class HitboxClickDetector {

    final private Hitbox hitbox;

    private boolean leftClickPressedOnHitbox;


    public HitboxClickDetector(Hitbox hitbox) {
        this.hitbox = hitbox;
        this.leftClickPressedOnHitbox = false;
    }

    public boolean result(boolean leftClickState, Vec2D cursorPosition) {
        if (leftClickState && !leftClickPressedOnHitbox && hitbox.containsPoint(cursorPosition)) {
            leftClickPressedOnHitbox = true;
            return false;
        }

        if (!leftClickState && leftClickPressedOnHitbox) {
            this.leftClickPressedOnHitbox = false;
            return hitbox.containsPoint(cursorPosition);
        }
        return false;
    }
}
