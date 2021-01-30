package com.dragonboatrace.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.dragonboatrace.entities.Obstacle;
import com.dragonboatrace.entities.ObstacleType;
import com.dragonboatrace.entities.PowerUp;
import com.dragonboatrace.entities.PowerUpType;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Lane in a {@link Race}.
 *
 * @author Babar Khan, Benji Garment, Joe Wrieden, Jacob Turner
 */
public class Lane {

    /**
     * The hit box of the lane, used to check if a boat is in the lane.
     */
    private final Hitbox area;
    /**
     * A list of the obstacles currently in the lane.
     */
    private final List<Obstacle> obstacles;
    /**
     * A list of the power ups currently in the lane.
     */
    private final List<PowerUp> powerUps;
    /**
     * A list of times to wait before adding a new obstacle to the lane.
     */
    private final List<Float> randomWaitTimes;

    private final Vector2 position;
    private final int width;
    private final int round;

    /**
     * Creates a new lane at a position and with a width and uses the round number to change the number of obstacles.
     * @param pos The position of the lane in the screen.
     * @param width The width of the lane.
     * @param round The current round, used to increase difficulty.
     */
    public Lane(Vector2 pos, int width, int round) {
        this.area = new Hitbox(pos.x, pos.y, width, Gdx.graphics.getHeight() + 200);
        this.position = pos;
        this.width = width;
        this.round = round;

        this.obstacles = new ArrayList<Obstacle>();
        this.powerUps = new ArrayList<PowerUp>();
        this.randomWaitTimes = new ArrayList<Float>();
        populateList(round);
    }

    /**
     * Creates a new lane at a position and with a width and uses the round number to change the number of obstacles.
     * @param data The JsonValue that contains any other data the class requires to fully reconstruct it.
     */
    public Lane(JsonValue data) {
        this.position = new Vector2(data.get("pos").getFloat("x"), data.get("pos").getFloat("y"));
        this.width = data.getInt("width");
        this.round = data.getInt("round");
        this.area = new Hitbox(this.position.x, this.position.y, this.width, Gdx.graphics.getHeight() + 200);


        this.obstacles = new ArrayList<Obstacle>();
        this.powerUps = new ArrayList<PowerUp>();
        this.randomWaitTimes = new ArrayList<Float>();
        
        JsonIterator obstacleIter = data.get("obstacles").iterator();
        while (obstacleIter.hasNext()) {
            JsonValue obstacleJson = obstacleIter.next();
            ObstacleType t = new Json().fromJson(ObstacleType.class, obstacleJson.getString("type"));
            Vector2 objectPos = new Vector2(obstacleJson.get("pos").getFloat("x"), obstacleJson.get("pos").getFloat("y"));
            this.obstacles.add(new Obstacle(t, objectPos));
        }

        JsonIterator powerUpIter = data.get("powerUps").iterator();
        while (powerUpIter.hasNext()) {
            JsonValue powerUpJson = powerUpIter.next();
            PowerUpType type = new Json().fromJson(PowerUpType.class, powerUpJson.getString("type"));
            Vector2 powerUpPos = new Vector2(powerUpJson.get("pos").getFloat("x"), powerUpJson.get("pos").getFloat("y"));
            this.powerUps.add(new PowerUp(type, powerUpPos));
        }

        JsonIterator waitTimeIter = data.get("randomWaitTimes").iterator();
        while (waitTimeIter.hasNext()) {
            JsonValue randomWaitJson = waitTimeIter.next();
            this.randomWaitTimes.add(randomWaitJson.asFloat());
        }
    }

    /**
     * Update the obstacles in the lane, remove any that are no longer on screen and replace them at a random time.
     *
     * @param deltaTime The time since the last frame.
     * @param velY      The y-velocity of the boat in the lane.
     */
    public void update(float deltaTime, float velY) {

        /* Check for obstacle collisions */
        ListIterator<Obstacle> iter1 = obstacles.listIterator();
        while (iter1.hasNext()) {
            Obstacle obstacle = iter1.next();
            obstacle.update(deltaTime, velY);
            if (obstacle.getHitBox().leaves(this.area)) {
                iter1.remove();
                replaceObstacle();
            }
        }

        /* Check for power up collisions */
        ListIterator<PowerUp> iter2 = powerUps.listIterator();
        while (iter2.hasNext()) {
            PowerUp powerup = iter2.next();
            powerup.update(deltaTime, velY);
            if (powerup.getHitBox().leaves(this.area)) {
                iter2.remove();
                replaceObstacle();
            }
        }

        /* Randomly replace obstacles and power ups */
        ListIterator<Float> times = randomWaitTimes.listIterator();
        while (times.hasNext()) {
            float time = times.next() - deltaTime;
            if (time > 0) {
                times.set(time);
            } else {
                /* 80% chance to create an obstacle and a 20% chance to create a power up */
                if (new Random().nextDouble() < 0.8){
                    obstacles.add(randomObstacle());
                }
                else{
                    powerUps.add(randomPowerUp());
                }
                
                times.remove();
            }
        }
    }

    /**
     * Render the obstacles and power ups in the lane.
     *
     * @param batch The SpriteBatch to be added to.
     */
    public void render(SpriteBatch batch) {
        for (Obstacle obstacle : obstacles) {
            obstacle.render(batch);
        }
        for(PowerUp powerUp : powerUps) {
            powerUp.render(batch);
        }
    }

    /**
     * Get the list of all obstacles in the lane.
     *
     * @return An {@link ArrayList} of type {@link Obstacle} with all the obstacles in the lane.
     */
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    /**
     * Get the list of all power ups in the lane.
     *
     * @return An {@link ArrayList} of type {@link PowerUp} with all the power ups in the lane.
     */
    public List<PowerUp> getPowerUps() {
        return this.powerUps;
    }

    /**
     * Get the lanes hit box
     *
     * @return A {@link Hitbox} representing the lanes area.
     */
    public Hitbox getHitbox() {
        return this.area;
    }

    /**
     * Remove an {@link Obstacle} from the list of obstacles, and randomly replace it.
     *
     * @param toRemove The obstacle to remove from the lane.
     */
    public void removeObstacle(Obstacle toRemove) {
        obstacles.remove(toRemove);
        replaceObstacle();
    }

    /**
     * Remove an {@link PowerUp} from the list of power ups, and randomly replace it.
     *
     * @param toRemove The obstacle to remove from the lane.
     */
    public void removePowerUp(PowerUp toRemove) {
        powerUps.remove(toRemove);
        replaceObstacle();
    }

    /**
     * Create a random time at which to add an {@link Obstacle} or {@link PowerUp} to the lane.
     */
    public void replaceObstacle() {
        randomWaitTimes.add(1.0f + 2 * ThreadLocalRandom.current().nextFloat());
    }

    /**
     * Create a new random {@link Obstacle}
     *
     * @return a new {@link Obstacle} in the lanes area.
     */
    private Obstacle randomObstacle() {
        int rand = ThreadLocalRandom.current().nextInt(0, ObstacleType.values().length);
        return new Obstacle(ObstacleType.values()[rand], this.area.getX(), this.area.getWidth());
    }

    /**
     * Create a new random {@link PowerUp}
     *
     * @return a new {@link PowerUp} in the lanes area.
     */
    private PowerUp randomPowerUp() {
        int rand = ThreadLocalRandom.current().nextInt(0, PowerUpType.values().length);
        return new PowerUp(PowerUpType.values()[rand], this.area.getX(), this.area.getWidth());
    }

    /**
     * Fill the list with obstacles and power ups that will start at random times.
     * @param round The current round increases the number of obstacles.
     */
    private void populateList(int round) {
        for (int i = 0; i < ((5 - Config.PLAYER_COUNT + round) + (5 * Config.GAME_DIFFICULTY)); i++) {
            replaceObstacle();
        }
    }

    public void dispose() {
        for (Obstacle obst : obstacles) {
            obst.dispose();
        }
        for (PowerUp powerUp : powerUps){
            powerUp.dispose();
        }
    }

    /**
     * Creates a JSON string needed to fully reconstruct the class.
     * 
     * @return JSON String contain all values needed to reconstruct the class.
     */
    public String toJson(){
        String[] obstacleJson = new String[this.obstacles.size()];
        String[] powerUpJson = new String[this.powerUps.size()];
        String[] randomWaitJson = new String[this.randomWaitTimes.size()];
        for (int i=0;i<this.obstacles.size();i++){
            obstacleJson[i] = this.obstacles.get(i).toJson();
        }
        for (int i=0;i<this.powerUps.size();i++){
            powerUpJson[i] = this.powerUps.get(i).toJson();
        }
        for (int i=0;i<this.randomWaitTimes.size();i++){
            randomWaitJson[i] = randomWaitTimes.get(i).toString();
        }

        return String.format("{pos:{x:%f, y:%f}, width:%s, round:%s, obstacles:[%s], powerUps:[%s], randomWaitTimes:[%s]}", 
            this.position.x,
            this.position.y,
            this.width,
            this.round,
            String.join(",", obstacleJson),
            String.join(",", powerUpJson),
            String.join(",", randomWaitJson)
        );
    }
}