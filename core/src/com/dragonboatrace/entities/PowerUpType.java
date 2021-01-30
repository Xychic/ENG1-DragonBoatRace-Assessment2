package com.dragonboatrace.entities;

/**
 * Represents a type of power up.
 *
 * @author Babar Khan
 */
public enum PowerUpType {
    /* ENUM(texture, speed, damage)*/
    SPEED("power_up_speed.png", 50, 0),
    REPAIR("power_up_repair.png", 50, 0),
    SHIELD("power_up_shield.png", 55, 0),
    CLEAR("power_up_clear.png", 50, 0),
    STAMINA("power_up_stamina.png", 50, 0);

    /**
     * The texture of the power up.
     */
    private final String texture;
    /**
     * The base speed at which the power up moves.
     */
    private final float speed;
    /**
     * The damage the power up type deal at a collision.
     */
    private final float damage;

    /**
     * Creates a new type of power up with a given texture, base speed ana damage value.
     *
     * @param texture The path to the power up texture.
     * @param speed   The speed of the power up type.
     * @param damage  The damage of the power up type.
     */
    PowerUpType(String texture, float speed, float damage) {
        this.texture = texture;
        this.speed = speed;
        this.damage = damage;
    }

    /**
     * Get the base speed of the power up type.
     *
     * @return A float representing the speed of the power up type.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the damage of the power up type.
     *
     * @return A float representing the damage dealt by the power up type.
     */
    public float getDamage() {
        return this.damage;
    }

    /**
     * Get the power up types texture
     *
     * @return A string representing the path to the power up texture.
     */
    public String getTexture() {
        return this.texture;
    }
}