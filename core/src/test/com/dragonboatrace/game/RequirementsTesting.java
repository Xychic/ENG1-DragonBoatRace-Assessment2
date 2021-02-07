package test.com.dragonboatrace.game;

import static org.junit.Assert.*;

import java.beans.Transient;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import main.com.dragonboatrace.game.entities.boats.Boat;
import main.com.dragonboatrace.game.entities.boats.BoatType;
import main.com.dragonboatrace.game.tools.Lane;

import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith (GdxTestRunner.class)
public class RequirementsTesting {

    /**
     * This is the test for requirement UR_BOAT_UNIQUENESS
     */
    @Test
    public void UR_BOAT_UNIQUENESSTest() {
        assertTrue(uniqueBoatTestHelper());
    }

    /**
     * This function is used by UR_BOAT_UNIQUENESSTest() to test for
     * UR_BOAT_UNIQUENESS
     * 
     * @return true if all boats are unique, false otherwise
     */
    public Boolean uniqueBoatTestHelper() {
        HashSet<float[]> boatTypeList = new HashSet<float[]>();

        for (BoatType boatType : BoatType.values()) {
            float[] attrList = { boatType.getHealth(), boatType.getAgility(), boatType.getSpeed(),
                    boatType.getStamina() };
            if (boatTypeList.contains(attrList)) {
                return false;
            } else {
                boatTypeList.add(attrList);
            }
        }
        return true;
    }

    /**
     * This is the test for UR_DIFFICULTY_LEVEL
     */
    @Test
    public void UR_DIFFICULTY_LEVELTest() {
        // first check how many obstacles will spawn every round, this will represent
        // the difficulty of each round
        int countRoundOne = (new Lane(new Vector2(123, 456), 1, 2, 3, false)).createObstacleCount(1, 1, 1);
        int countRoundTwo = (new Lane(new Vector2(123, 456), 1, 2, 3, false)).createObstacleCount(2, 1, 1);
        int countRoundThree = (new Lane(new Vector2(123, 456), 1, 2, 3, false)).createObstacleCount(3, 1, 1);

        // then check if as the rounds increase the number of obstacles spawning
        // increases
        // there are only 3 rounds so we only need to check twice
        // I could make this code better so that it works with n rounds but dont have
        // time
        Boolean conditionOne = countRoundOne < countRoundTwo;
        Boolean conditionTwo = countRoundTwo < countRoundThree;

        // if both conditions are true then the difficulty should increase with every
        // round
        assertTrue(conditionOne & conditionTwo);
    }

    @Test
    public void UR_PADDLERS_STAMINA_DECREASETest() {
        Boat myBoat = new Boat(BoatType.AGILE, new Lane(new Vector2(123, 456), 1 ,2 , 3 ,false) , "name", false);
        myBoat.addVelocity(0,100);

        for (int i = 0; i < 3000; i++) {
            myBoat.update(1000);
        }

        Boolean staminaDecrease = true;

        if (myBoat.getBoatType().getStamina() == myBoat.getStamina()) {
            staminaDecrease = false;
        }

        assertTrue(staminaDecrease);
    }
    
}
