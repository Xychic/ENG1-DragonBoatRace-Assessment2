package test.com.dragonboatrace.game;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

@RunWith (GdxTestRunner.class)
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
        int countRoundOne = Lane.createObstacleCount(1, 1, 1);
        int countRoundTwo = Lane.createObstacleCount(2, 1, 1);
        int countRoundThree = Lane.createObstacleCount(3, 1, 1);

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
        // player and cpu boat of each type
        PlayerBoat[] playerBoats = generatePlayerBoatList();
        ComputerBoat[] computerBoats = generateCPUBoatList();

        for (i = 0; i < BoatType.values().length; i++) {
            PlayerBoat[i].addVelocity(0, 100);
            ComputerBoat[i].addVelocity(0, 100);
        }

        // then have them simulate moving forward a bunch
        // this will be 50 seconds at 1fps
        for (i = 0; i < 3000; i++) {
            for (Boat boat : playerBoats) {
                boat.update(1000);
            }
            for (Boat boat : computerBoats) {
                boat.update(1000);
            }
        }
        // check their stamina and if its less than their original then it works
        assertTrue(checkBoatStaminaDecrease(playerBoats) & checkBoatStaminaDecrease(computerBoats));
    }

    public PlayerBoat[] generatePlayerBoatList() {

        PlayerBoat[] playerBoats = new PlayerBoat[BoatType.values().length];

        for (i = 0; i < BoatType.values().length; i++) {
            PlayerBoat[i] = PlayerBoat(BoatType.values()[i], new Lane(new Vector2(), 10, 1), "TestBoat");
        }

        return playerBoats;

    }

    public ComputerBoat[] generateCPUBoatList() {
        ComputerBoat[] computerBoats = new ComputerBoat[BoatType.values().length];

        for (i = 0; i < BoatType.values().length; i++) {
            ComputerBoat[i] = PlayerBoat(BoatType.values()[i], new Lane(new Vector2(), 10, 1), "TestBoat");
        }

        return computerBoats;
    }

    public Boolean checkBoatStaminaDecrease(Boat[] boats) {
        for (Boat b : boats) {
            if (b.boatType.stamina == b.getStamina) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void UR_PLAYER_PENALTYTest() {
        PlayerBoat[] playerBoats = generatePlayerBoatList();
        ComputerBoat[] computerBoats = generateCPUBoatList();

        
    }

}
