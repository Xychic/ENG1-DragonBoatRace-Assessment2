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
import main.com.dragonboatrace.game.tools.Race;
import main.com.dragonboatrace.game.tools.Tuple;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (GdxTestRunner.class)
public class FunctionalRequirementsTest {

    @Test
    public void FR_14_TEST() {
        for (int i=0; i<100; i++) {
            Race r = new Race(1, BoatType.AGILE, 1, false);
            String save = r.toJson();
            Race r2 = new Race(new JsonReader().parse(save), false);
            assertEquals(r2.toJson(), save);
        }
    }
    
}
