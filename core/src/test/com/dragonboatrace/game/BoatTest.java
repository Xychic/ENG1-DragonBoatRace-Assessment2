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

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (GdxTestRunner.class)
public class BoatTest {

    @Test
	public void boatSaveConsistentcy() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        String before = b.toJson();
        
        JsonValue playerJson = new JsonReader().parse(before);
        BoatType boatType = new Json().fromJson(BoatType.class, playerJson.getString("type"));
        Lane lane = new Lane(playerJson.get("lane"), false);
        String name = playerJson.getString("name");
        Vector2 pos = new Vector2(playerJson.get("pos").getFloat("x"), playerJson.get("pos").getFloat("y"));
        Vector2 vel = new Vector2(playerJson.get("vel").getFloat("x"), playerJson.get("vel").getFloat("y"));
        Boat b2 = new Boat(pos, vel, boatType, lane, name, playerJson.get("data"), false);
        String after = b2.toJson();

        System.out.println(before);
        System.out.println(after);
		assertEquals(before, after);
    }

    @Test
    public void obstacleCollision() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        assertTrue(b.checkObstacleCollisions());
    }

    @Test
    public void obstacleReducesHealth() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        float startHealth = b.getHealth();
        b.checkObstacleCollisions();
        assertTrue(startHealth != b.getHealth());
    }

    @Test
    public void powerUpCollision() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        assertTrue(b.checkPowerUpCollisions());
    }

    @Test
    public void powerUpShieldEffect() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.SHIELD, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getShield() > 0);
    }

    @Test
    public void powerUpBombEffect() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x+100, y), false));
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.CLEAR, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getLane().getObstacles().size() == 0);
    }

    @Test
    public void powerUpStaminaEffect() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.addStamina(-10);
        float before = b.getStamina();
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.STAMINA, new Vector2(x, y), false));
        b.checkPowerUpCollisions();
        assertTrue(b.getStamina() > before);
    }

    @Test
    public void powerUpHealthEffect() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        b.getLane().getPowerUps().add(new PowerUp(PowerUpType.REPAIR, new Vector2(x, y), false));
        b.checkObstacleCollisions();
        float before = b.getHealth();
        b.checkPowerUpCollisions();
        assertTrue(b.getHealth() > before);
    }
}