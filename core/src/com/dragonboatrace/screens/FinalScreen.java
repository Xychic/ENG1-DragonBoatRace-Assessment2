package com.dragonboatrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.DragonBoatRace;
import com.dragonboatrace.entities.boats.Boat;
import com.dragonboatrace.entities.boats.BoatType;
import com.dragonboatrace.entities.boats.PlayerBoat;
import com.dragonboatrace.tools.Settings;
import com.dragonboatrace.tools.Lane;

/**
 * Displays the screen that announces that the player has made it to the final.
 *
 * @author Benji Garment, Joe Wrieden, Jacob Turner
 */
public class FinalScreen implements Screen {

    /**
     * The instance of the game.
     */
    private final DragonBoatRace game;
    /**
     * The players boat to be carried through the rounds.
     */
    private final Boat playerBoat;

    /* Font related items */
    private final BitmapFont font;
    private final GlyphLayout layout;

    /**
     * Creates a new screen that displays the announcement message to the player.
     *
     * @param game       The instance of the game to use.
     * @param boatChosen The {@link BoatType} that the player chose.
     */
    public FinalScreen(DragonBoatRace game, BoatType boatChosen) {
        this.game = game;
        this.playerBoat = new PlayerBoat(boatChosen, new Lane(new Vector2(0, 0), Gdx.graphics.getWidth() /  Settings.PLAYER_COUNT, this.game.getRound()), "Player");

        /* Font related items */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("osaka-re.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 5 / Settings.SCALAR;
        parameter.color = Color.WHITE;
        this.font = generator.generateFont(parameter);

        this.layout = new GlyphLayout();
    }


    @Override
    public void show() {
        Settings.setPlayerCount(4);
    }

    /**
     * Renders the screen and the message to the player.
     *
     * @param delta The time passed since the last frame.
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.getBatch().begin();

        layout.setText(font, "You have made it into the final!");
        font.draw(this.game.getBatch(), "You have made it into the final!", (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2);

        layout.setText(font, "Press Space to continue to the final!");
        font.draw(this.game.getBatch(), "Press Space to continue to the final!", (Gdx.graphics.getWidth() - layout.width) / 2, 100 / Settings.SCALAR + layout.height);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.game.setScreen(new MainGameScreen(this.game, this.playerBoat.getBoatType()));
        }

        this.game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
