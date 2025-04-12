package engine.entity;

public enum EntityType {
    SHIP,
    PROJECTILE,
    ITEM;

    public static EntityType fromString(String input){
        if(input.equals("ship")){
            return SHIP;
        }
        if(input.equals("projectile")){
            return PROJECTILE;
        }
        if(input.equals("item")){
            return ITEM;
        }
        throw new IllegalArgumentException("illegal string value: \"" + input + "\"");
    }

    @Override
    public String toString() {
        if(this.equals(SHIP)){
            return "ship";
        }
        if(this.equals(PROJECTILE)){
            return "projectile";
        }
        if(this.equals(ITEM)){
            return "item";
        }
        return "null";
    }
}
