package test.com.dragonboatrace.game;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import main.com.dragonboatrace.game.entities.Obstacle;
import main.com.dragonboatrace.game.entities.ObstacleType;
import main.com.dragonboatrace.game.entities.boats.Boat;
import main.com.dragonboatrace.game.entities.boats.BoatType;
import main.com.dragonboatrace.game.tools.Lane;
import main.com.dragonboatrace.game.tools.Tuple;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (GdxTestRunner.class)
public class ObstacleTest {
    @Test
    public void OBSTACLE_COLLISION() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        assertTrue(b.checkObstacleCollisions());
    }

    @Test
    public void OBSTACLE_COLLISION_REDUCE_HEALTH() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        float startHealth = b.getHealth();
        b.checkObstacleCollisions();
        assertTrue(startHealth != b.getHealth());
    }

    @Test
    public void OBSTACLE_COLLISION_REMOVE_ON_COLLISION() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        b.getLane().getObstacles().add(new Obstacle(ObstacleType.BRANCH, new Vector2(x, y), false));
        float startHealth = b.getHealth();
        b.checkObstacleCollisions();
        assertTrue(b.getLane().getObstacles().size() == 0);
    }

    @Test
    public void OBSTACLE_DAMAGE() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "testBoat", false);
        float x = b.getHitBox().getX();
        float y = b.getHitBox().getY();
        for (ObstacleType type : ObstacleType.values()) {
            float reduction = type.getDamage();
            b.getLane().getObstacles().add(new Obstacle(type, new Vector2(x, y), false));
            float startHealth = b.getHealth();
            b.checkObstacleCollisions();
            assertTrue(b.getHealth() == startHealth - reduction);
            b.addHealth(reduction);
        }
    }
}
