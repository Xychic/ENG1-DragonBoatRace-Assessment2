package main.com.dragonboatrace.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents an Obstacle.
 *
 * @author Benji Garment, Joe Wrieden
 */
public class Obstacle extends Entity {

    /**
     * The speed of the obstacle.
     */
    private final float speed;
    /**
     * The damage the obstacle will deal when colliding with a player.
     */
    private final float damage;

    private final ObstacleType obstacleType;

    // >>>> Added in assessment 2 <<<<
    /**
     * Creates a new Obstacle of a specific type and bounds in which it can be created.
     *
     * @param type   The type of obstacle.
     * @param startX The starting x value the obstacle can be created in.
     * @param width  How far from startX the obstacle can be created.
     * @param loadTextures If the enity should load its textures.
     */
    public Obstacle(ObstacleType type, float startX, int width, boolean loadTextures) {
        /* Entity creation */
        /* First vector is long as to start it at a random x position within the bounds of the screen */
        /* Form of Entity(Vector2 pos, Vector2 vel, EntityType type, String texture) */
        super(new Vector2(((int) startX + width) / 2.0f + ThreadLocalRandom.current().nextInt(-((int) startX + width) / 2 + EntityType.OBSTACLE.getWidth() / 2, ((int) startX + width) / 2 + EntityType.OBSTACLE.getWidth() / 2), Gdx.graphics.getHeight()), new Vector2(), EntityType.OBSTACLE, type.getTexture(), loadTextures);
        this.speed = type.getSpeed();
        this.damage = type.getDamage();
        this.obstacleType = type;
    }

    public Obstacle(ObstacleType type, float startX, int width) {
        this(type, startX, width, true);
    }


    // >>>> Added in assessment 2 <<<<
    public Obstacle(ObstacleType type, Vector2 pos, boolean loadTextures) {
        super(pos, new Vector2(), EntityType.OBSTACLE, type.getTexture(), loadTextures);
        this.speed = type.getSpeed();
        this.damage = type.getDamage();
        this.obstacleType = type;
    }

    public Obstacle(ObstacleType type, Vector2 pos) {
        this(type, pos, true);
    }

    /**
     * Update the obstacle's position relative to the time passed since last frame and the velocity of the boat in that lane.
     *
     * @param deltaTime The time since last frame.
     * @param velY      The y-velocity of the boat in the same lane as the obstacle.
     */
    public void update(float deltaTime, float velY) {
        this.position.add(0, -1 * (velY + this.speed) * deltaTime);
        this.hitbox.move(this.position.x, this.position.y);
    }

    /**
     * Get the obstacles speed attribute, not the velocity it is moving at currently.
     *
     * @return A float of the obstacles speed attribute.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the amount of damage the obstacle will deal at a collision.
     *
     * @return A float of the amount of damage.
     */
    public float getDamage() {
        return this.damage;
    }

    /**
     * The position of the obstacle.
     *
     * @return A Vector2 of the position of the obstacle.
     */
    public Vector2 getPos() {
        return this.position;
    }

    // >>>> Added in assessment 2 <<<<
    public String toJson(){
        String s = String.format("{type:%s, pos:{x:%f, y:%f}}", 
            this.obstacleType, 
            this.position.x,
            this.position.y
        );
        return s;
    }
}
