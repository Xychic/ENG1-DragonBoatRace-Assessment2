package main.com.dragonboatrace.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import main.com.dragonboatrace.game.screens.MainMenuScreen;
import main.com.dragonboatrace.game.tools.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Game itself and holds all the screens.
 *
 * @author Benji Garment, Joe Wrieden, Jacob Turner
 */
public class DragonBoatRace extends Game {

    /**
     * The Spritebatch used to group all renders.
     */
    protected SpriteBatch batch;

    /**
     * The current round.
     */
    protected int round = 1;

    /**
     * A list of cumulative times for all boats.
     */
    protected List<Float> totalTimes = new ArrayList<Float>();

    /**
     * The players total time.
     */
    protected float playerTotalTime = 0;

    /**
     * The ShapeRenderer used to render all shapes
     */
    protected ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.setScreen(new MainMenuScreen(this));
        for (int i = 0; i < Config.PLAYER_COUNT; i++)
            totalTimes.add((float) 0);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public ShapeRenderer getRenderer() {
        return this.shapeRenderer;
    }

    public int getRound() {
        return this.round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void upRound() {
        this.round++;
    }

    public void setTimeAt(int i, float t) {
        this.totalTimes.set(i, this.totalTimes.get(i) + t);
    }

    public float getPlayerTotalTime() {
        return this.playerTotalTime;
    }

    public void setPlayerTotalTime(float t) {
        this.playerTotalTime += t;  // TODO check this is correct
    }

    public List<Float> getTotalTimes() {
        return this.totalTimes;
    }
}
