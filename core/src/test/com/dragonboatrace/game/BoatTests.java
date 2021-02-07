package test.com.dragonboatrace.game;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import main.com.dragonboatrace.game.entities.boats.Boat;
import main.com.dragonboatrace.game.entities.boats.BoatType;
import main.com.dragonboatrace.game.tools.Lane;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith (GdxTestRunner.class)
public class BoatTests {

    @Test
	public void saveConsistentcy() {
        Boat b = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1, 2, 3, false), "name", false);
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
}

