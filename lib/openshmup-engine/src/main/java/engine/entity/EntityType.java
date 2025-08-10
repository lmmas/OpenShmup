package engine.entity;

public enum EntityType {
    SHIP,
    PROJECTILE,
    ITEM;

    public static EntityType fromString(String input){
        return switch (input) {
            case "ship" -> SHIP;
            case "projectile" -> PROJECTILE;
            case "item" -> ITEM;
            default -> throw new IllegalArgumentException("illegal string value: \"" + input + "\"");
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case SHIP -> "ship";
            case PROJECTILE -> "projectile";
            case ITEM -> "ITEM";
        };
    }
}
