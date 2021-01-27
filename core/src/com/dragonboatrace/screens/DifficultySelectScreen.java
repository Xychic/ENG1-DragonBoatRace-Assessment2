package com.dragonboatrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.DragonBoatRace;
import com.dragonboatrace.entities.Button;
import com.dragonboatrace.entities.EntityType;
import com.dragonboatrace.entities.boats.BoatType;
import com.dragonboatrace.tools.Config;

/**
 * Displays the screen that allows the player to choose a difficulty
 *
 * @author Jacob Turner
 */
public class DifficultySelectScreen implements Screen {

    /**
     * Image for the easy difficulty.
     */
    private final Texture easyImage;

    /**
     * Image for the normal difficulty.
     */
    private final Texture normalImage;

    /**
     * Image for the hard difficulty.
     */
    private final Texture hardImage;

    /**
     * Button to select the easy difficulty.
     */
    private final Button easyButton;

    /**
     * Button to select the normal difficulty.
     */
    private final Button normalButton;

    /**
     * Button to select the hard difficulty.
     */
    private final Button hardButton;

    /**
     * Instance of the main game, used to have a collective spritebatch which gives better performance.
     */
    private final DragonBoatRace game;

    /**
     * The boat type the player has selected.
     */
    private final BoatType boatType;


    private final BitmapFont font;
    private final GlyphLayout layout;

    private final int buttonWidth;

    /**
     * Creates a new screen to display the difficulty options to the player.
     *
     * @param game The instance of the {@link DragonBoatRace} game.
     */
    public DifficultySelectScreen(DragonBoatRace game, BoatType boatType) {

        this.game = game;
        this.boatType = boatType;

        this.buttonWidth = EntityType.BUTTON.getWidth();
        float spacing = (Gdx.graphics.getWidth() - buttonWidth * 3.0f) / 4.0f;
        this.easyButton = new Button(new Vector2(spacing, 100), "easy_button_active.png", "easy_button_inactive.png");
        this.normalButton = new Button(new Vector2(spacing + (buttonWidth + spacing), 100), "normal_button_active.png", "normal_button_inactive.png");
        this.hardButton = new Button(new Vector2(spacing + (buttonWidth + spacing) * 2, 100), "hard_button_active.png", "hard_button_inactive.png");

        this.easyImage = new Texture("easy.png");
        this.normalImage = new Texture("normal.png");
        this.hardImage = new Texture("hard.png");

        /* Font related items */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("osaka-re.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 10.0 / Config.SCALAR;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        layout = new GlyphLayout();
        layout.setText(font, "Choose difficulty:");

    }


    @Override
    public void show() {

    }

    /**
     * Renders the difficulty selection screen.
     *
     * @param delta The time passed since the last frame.
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.getBatch().begin();

        font.draw(this.game.getBatch(), "Choose Difficulty:", (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() - 100);

        float scale = ((float) this.buttonWidth / EntityType.BOAT.getWidth()) / 2.0f;

        this.game.getBatch().draw(this.easyImage, this.easyButton.getHitBox().getX() + ((this.easyButton.getHitBox().getWidth() - this.buttonWidth / 2f) / 2f), 150 + EntityType.BUTTON.getHeight(), this.buttonWidth / 2f, EntityType.BOAT.getHeight() * scale);
        this.easyButton.render(this.game.getBatch());
        
        this.game.getBatch().draw(this.normalImage, this.normalButton.getHitBox().getX() + ((this.normalButton.getHitBox().getWidth() - this.buttonWidth / 2f) / 2f), 150 + EntityType.BUTTON.getHeight(), this.buttonWidth / 2f, EntityType.BOAT.getHeight() * scale);
        this.normalButton.render(this.game.getBatch());
        
        this.game.getBatch().draw(this.hardImage, this.hardButton.getHitBox().getX() + ((this.hardButton.getHitBox().getWidth() - this.buttonWidth / 2f) / 2f), 150 + EntityType.BUTTON.getHeight(), this.buttonWidth / 2f, EntityType.BOAT.getHeight() * scale);
        this.hardButton.render(this.game.getBatch());
        
        if (this.easyButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            Config.setGameDifficulty(0);
            this.game.setScreen(new MainGameScreen(this.game, this.boatType));
        } else if (this.normalButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            Config.setGameDifficulty(1);
            this.game.setScreen(new MainGameScreen(this.game, this.boatType));
        } else if (this.hardButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            Config.setGameDifficulty(2);
            this.game.setScreen(new MainGameScreen(this.game, this.boatType));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(new BoatSelectScreen(this.game));
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
