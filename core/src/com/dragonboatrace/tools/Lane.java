package com.dragonboatrace.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.entities.Obstacle;
import com.dragonboatrace.entities.ObstacleType;
import com.dragonboatrace.entities.PowerUp;
import com.dragonboatrace.entities.PowerUpType;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Lane in a {@link Race}.
 *
 * @author Babar Khan, Benji Garment, Joe Wrieden
 */
public class Lane {

    /**
     * The hit box of the lane, used to check if a boat is in the lane.
     */
    private final Hitbox area;
    /**
     * A list of the obstacles currently in the lane.
     */
    private final ArrayList<Obstacle> obstacles;
    /**
     * A list of the power ups currently in the lane.
     */
    private final ArrayList<PowerUp> powerUps;
    /**
     * A list of times to wait before adding a new obstacle to the lane.
     */
    private final ArrayList<Float> randomWaitTimes;

    /**
     * Creates a new lane at a position and with a width and uses the round number to change the number of obstacles.
     * @param pos The position of the lane in the screen.
     * @param width The width of the lane.
     * @param round The current round, used to increase difficulty.
     */
    public Lane(Vector2 pos, int width, int round) {
        this.area = new Hitbox(pos.x, pos.y, width, Gdx.graphics.getHeight() + 200);
        this.obstacles = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.randomWaitTimes = new ArrayList<>();
        populateList(round);
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
    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }

    /**
     * Get the list of all power ups in the lane.
     *
     * @return An {@link ArrayList} of type {@link PowerUp} with all the power ups in the lane.
     */
    public ArrayList<PowerUp> getPowerUps() {
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
        for (int i = 0; i < (11 - Settings.PLAYER_COUNT + round - 1); i++) {
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

}