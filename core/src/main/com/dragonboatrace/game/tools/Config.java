package main.com.dragonboatrace.game.tools;

import java.io.File;
import java.net.URISyntaxException;

import main.com.dragonboatrace.game.DragonBoatRace;

/**
 * Static class used to hold Config
 *
 * @author Benji Garment, Joe Wrieden, Jacob Turner
 */
public class Config {

    /**
     * The width of the window.
     */
    public static int WIDTH = 1920;
    /**
     * The height of the window.
     */
    public static int HEIGHT = 1080;
    /**
     * If the game is fullscreen or not.
     */
    public static boolean FULLSCREEN = true;
    /**
     * The global scalar at which to scale entities.
     */
    public static int SCALAR = 1;
    /**
     * The number of boats in the game, including the player itself.
     */
    public static int PLAYER_COUNT = 8;
    /**
     * Reduces the scalar that stamina gives when accelerating, see {@link main.com.dragonboatrace.game.entities.boats.Boat#velocityPercentage()}
     */
    public static int STAMINA_SPEED_DIVISION = 2;
    /**
     * The velocity penalty given when a collision occurs
     */
    public static int OBSTACLE_COLLISION_PENALTY = -20;
    /**
     * The the boat must wait before moving again after a collision.
     */
    public static float OBSTACLE_COLLISION_TIME = 0.5f;

    /** 
     * The difficulty of the game, changes the number of obstacles
    */
    public static int GAME_DIFFICULTY = 1;

    // >>>> Added in assessment 2 <<<<
    /**
     * The location of the save files
     */
    public static String SAVE_FILE_LOCATION = getSaveLocation();

    // >>>> Added in assessment 2 <<<<
    /**
     * 
     * @return A string to the save file location.
     */
    private static String getSaveLocation(){
        try {
            return new File(DragonBoatRace.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().resolve("..").normalize().toString();
        } catch (URISyntaxException exception) {
            return null;
        }
    }

    /**
     * Set the resolution of the screen.
     *
     * @param width  The width of the screen.
     * @param height The height of the screen.
     */
    public static void setResolution(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        SCALAR = 1920 / WIDTH + ((1920 % WIDTH == 0) ? 0 : 1);
    }

    /**
     * Set if the window should be fullscreen or not.
     *
     * @param FULLSCREEN A boolean if the window should be fullscreen.
     */
    public static void setFULLSCREEN(boolean FULLSCREEN) {
        Config.FULLSCREEN = FULLSCREEN;
    }

    /**
     * Set the number of players in the game.
     *
     * @param playerCount The number of players to change to.
     */
    public static void setPlayerCount(int playerCount) {
        PLAYER_COUNT = playerCount;
    }

    /**
     * Update the Stamina Speed Scalar.
     *
     * @param staminaSpeedDivision The new scalar to use.
     */
    public static void setStaminaSpeedDivision(int staminaSpeedDivision) {
        STAMINA_SPEED_DIVISION = staminaSpeedDivision;
    }

    /**
     * Update the obstacle collision velocity penalty.
     *
     * @param obstacleCollisionPenalty The new penalty to use.
     */
    public static void setObstacleCollisionPenalty(int obstacleCollisionPenalty) {
        OBSTACLE_COLLISION_PENALTY = obstacleCollisionPenalty;
    }

    /**
     * Update the obstacle collision time penalty.
     *
     * @param obstacleCollisionTime The new penalty to use.
     */
    public static void setObstacleCollisionTime(float obstacleCollisionTime) {
        OBSTACLE_COLLISION_TIME = obstacleCollisionTime;
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Update the game difficulty.
     * 
     * @param difficulty the new difficulty.
     */
    public static void setGameDifficulty(int difficulty) {
        GAME_DIFFICULTY = difficulty;
    }
}