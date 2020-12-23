package com.dragonboatrace.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a power up.
 *
 * @author Babar Khan
 */
public class PowerUp extends Entity {

    /**
     * The speed of the power up.
     */
    private final float speed;
    /**
     * The damage the obstacle will deal when colliding with a player.
     */
    private final float damage;

    /**
     * Creates a new power up of a specific type and bounds in which it can be created.
     *
     * @param type   The type of power up.
     * @param startX The starting x value the power up can be created in.
     * @param width  How far from startX the power up can be created.
     */
    public PowerUp(PowerUpType type, float startX, int width) {
        /* Entity creation */
        /* First vector is long as to start it at a random x position within the bounds of the screen */
        /* Form of Entity(Vector2 pos, Vector2 vel, EntityType type, String texture) */
        super(new Vector2(((int) startX + width) / 2.0f + ThreadLocalRandom.current().nextInt(-((int) startX + width) / 2 + EntityType.POWERUP.getWidth() / 2, ((int) startX + width) / 2 + EntityType.POWERUP.getWidth() / 2), Gdx.graphics.getHeight()), new Vector2(), EntityType.POWERUP, type.getTexture());
        this.speed = type.getSpeed();
        this.damage = type.getDamage();
    }

    /**
     * Update the power up's position relative to the time passed since last frame and the velocity of the boat in that lane.
     *
     * @param deltaTime The time since last frame.
     * @param velY      The y-velocity of the boat in the same lane as the power up.
     */
    public void update(float deltaTime, float velY) {
        this.position.add(0, -1 * (velY + this.speed) * deltaTime);
        this.hitbox.move(this.position.x, this.position.y);
    }

    /**
     * Get the power up speed attribute, not the velocity it is moving at currently.
     *
     * @return A float of the power up speed attribute.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the amount of damage the power up will deal at a collision.
     *
     * @return A float of the amount of damage.
     */
    public float getDamage() {
        return this.damage;
    }

    /**
     * The position of the power up.
     *
     * @return A Vector2 of the position of the power up.
     */
    public Vector2 getPos() {
        return this.position;
    }

}
