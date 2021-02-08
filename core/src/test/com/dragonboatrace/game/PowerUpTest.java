package test.com.dragonboatrace.game;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import main.com.dragonboatrace.game.entities.Obstacle;
import main.com.dragonboatrace.game.entities.ObstacleType;
import main.com.dragonboatrace.game.entities.PowerUp;
import main.com.dragonboatrace.game.entities.PowerUpType;
import main.com.dragonboatrace.game.entities.boats.Boat;
import main.com.dragonboatrace.game.entities.boats.BoatType;
import main.com.dragonboatrace.game.tools.Lane;
import main.com.dragonboatrace.game.tools.Tuple;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (GdxTestRunner.class)
public class PowerUpTest {
    @Test
    public void POWERUPS() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        assertTrue(b.checkPowerUpCollisions());
    }

    @Test
    public void POWERUPS_REMOVE_ON_COLLISION() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getLane().getPowerUps().size() == 0);
    }

    @Test
    public void POWERUPS_SHIELD() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.SHIELD, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getShield() > 0);
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        assertTrue(b.checkObstacleCollisions());
        assertTrue(b.getHealth() == b.getBoatType().getHealth());

    }

    @Test
    public void POWERUPS_BOMB() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x+100, y), false));
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.CLEAR, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getLane().getObstacles().size() == 0);
    }

    @Test
    public void POWERUPS_STAMINA() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.addStamina(-10);
        float before = b.getStamina();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.STAMINA, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getStamina() > before);
        assertTrue(b.getStamina() == b.getBoatType().getStamina());
    }

    @Test
    public void POWERUPS_HEALTH() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        b.addHealth(-10);
        float before = b.getHealth();
        b.checkPowerUpCollisions();
        assertTrue(b.getHealth() > before);
    }

    @Test
    public void POWERUPS_MAX_HEALTH() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        b.addHealth(-10);
        b.checkPowerUpCollisions();
        assertTrue(b.getHealth() == b.getBoatType().getHealth());
    }

    @Test
    public void POWERUPS_SPEED() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.SPEED, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getBoost() > 0);
    }
}
