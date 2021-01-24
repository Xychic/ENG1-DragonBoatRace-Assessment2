package com.dragonboatrace.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.files.FileHandle;
import com.dragonboatrace.DragonBoatRace;
import com.dragonboatrace.entities.boats.BoatType;
import com.dragonboatrace.entities.Button;
import com.dragonboatrace.entities.EntityType;
import com.dragonboatrace.tools.Race;
import com.dragonboatrace.tools.ScrollingBackground;
import com.dragonboatrace.tools.Config;


/**
 * Represents the Main Game Screen where the game actually happens.
 *
 * @author Benji Garment, Joe Wrieden, Jacob Turner
 */
public class MainGameScreen implements Screen {

    /**
     * The game instance.
     */
    private final DragonBoatRace game;
    /**
     * Used to make sure the countdown happens at equal intervals.
     */
    private final Timer timer;
    /**
     * The race instance.
     */
    private final Race race;
    /**
     * The background of the window.
     */
    private final ScrollingBackground background;
    /**
     * Use to log the FPS for debugging.
     */
    private final FPSLogger logger;
    /**
     * GlyphLayout used for centering fonts
     */
    private final GlyphLayout layout;
    /**
     * Font used for rendering to screen
     */
    private final BitmapFont font;
    /**
     * A white version of teh font used for rendering to screen
     */
    private final BitmapFont fontWhite;
    /**
     * Pause game, starts true.
     */
    private boolean paused = true;
    /**
     * The button used to exit the game.
     */
    private final Button exitButton;
    /**
     * The button used to save the game.
     */
    private final Button saveButton;
    /**
     * The button used to go to the main menu.
     */
    private final Button mainMenuButton;
    /**
     * The time left on the initial countdown.
     */
    private int countDownRemaining = 3;
    /**
     * The String being displayed in the countdown.
     */
    private String countDownString = "";

    /**
     * Creates a new game screen with a game instance.
     *
     * @param game The game instance.
     * @param boatChosen The {@link BoatType} that the player chose.
     */
    public MainGameScreen(DragonBoatRace game, BoatType boatChosen) {
        this.game = game;

        this.logger = new FPSLogger();

        this.race = new Race(10000, boatChosen, this.game.getRound());
        this.background = new ScrollingBackground();
        this.background.resize(Gdx.graphics.getWidth());

        this.saveButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.5f), "save_button_active.png", "save_button_inactive.png");
        this.mainMenuButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.4f), "main_menu_button_active.png", "main_menu_button_inactive.png");
        this.exitButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.3f), "exit_button_active.png", "exit_button_inactive.png");

        /* Font related items */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("osaka-re.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 10.0 / Config.SCALAR;
        parameter.color = Color.BLACK;
        this.font = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        this.fontWhite = generator.generateFont(parameter); 
        this.layout = new GlyphLayout();

        /* Countdown initialisation */
        Timer.Task countDownTask = new Timer.Task() {
            @Override
            public void run() {
                paused = true;
                if (countDownRemaining == 3) {
                    countDownString = "READY";
                    countDownRemaining--;
                } else if (countDownRemaining == 2) {
                    countDownString = "STEADY";
                    countDownRemaining--;
                } else if (countDownRemaining == 1) {
                    countDownString = "GO";
                    countDownRemaining--;
                } else {
                    countDownRemaining = -1;
                    countDownString = "";
                    paused = false;
                    this.cancel();
                }
            }
        };
        timer = new Timer();
        timer.scheduleTask(countDownTask, 0, 1);
        // We don't want the countdown to start before the screen has displayed.
        timer.stop();
    }

    /**
     * Creates a new game screen with a game instance.
     *
     * @param game The game instance.
     * @param data The JsonValue that contains any other data the class requires to fully reconstruct it.
     */
    public MainGameScreen(DragonBoatRace game, JsonValue data) {
        this.game = game;

        this.logger = new FPSLogger();

        this.race = new Race(data);
        this.background = new ScrollingBackground();
        this.background.resize(Gdx.graphics.getWidth());

        this.saveButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.5f), "save_button_active.png", "save_button_inactive.png");
        this.mainMenuButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.4f), "main_menu_button_active.png", "main_menu_button_inactive.png");
        this.exitButton = new Button(new Vector2((Gdx.graphics.getWidth() - EntityType.BUTTON.getWidth()) / 2.0f, Gdx.graphics.getHeight() * 0.3f), "exit_button_active.png", "exit_button_inactive.png");

        /* Font related items */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("osaka-re.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 10.0 / Config.SCALAR;
        parameter.color = Color.BLACK;
        this.font = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        this.fontWhite = generator.generateFont(parameter); 
        this.layout = new GlyphLayout();

        /* Countdown initialisation */
        Timer.Task countDownTask = new Timer.Task() {
            @Override
            public void run() {
                paused = true;
                if (countDownRemaining == 3) {
                    countDownString = "READY";
                    countDownRemaining--;
                } else if (countDownRemaining == 2) {
                    countDownString = "STEADY";
                    countDownRemaining--;
                } else if (countDownRemaining == 1) {
                    countDownString = "GO";
                    countDownRemaining--;
                } else {
                    countDownRemaining = -1;
                    countDownString = "";
                    paused = false;
                    this.cancel();
                }
            }
        };
        timer = new Timer();
        timer.scheduleTask(countDownTask, 0, 1);
        // We don't want the countdown to start before the screen has displayed.
        timer.stop();
    }

    /**
     * Runs when the window first starts. Runs the countdown starter.
     */
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ESCAPE && countDownRemaining < 0) {
                    paused = !paused;
                }
                return true;
            }
        });
        timer.start();
    }

    /**
     * Render the main game window. Includes rendering the background and the {@link Race}.
     *
     * @param deltaTime The time since the last frame.
     */
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (!paused) {
            this.logger.log();
            this.background.update(deltaTime * this.race.getPlayer().getVelocity().y);
            this.background.render(game.getBatch());
            this.race.update(deltaTime, this.game);
            this.race.render(game.getBatch(), game.getRenderer());
        } else if (this.countDownRemaining >= 0) {
            this.background.render(game.getBatch());
            this.race.render(game.getBatch(), game.getRenderer());
            displayCountDown();
        } else {
            this.background.render(game.getBatch());
            this.race.render(game.getBatch(), game.getRenderer());
            renderPaused();
        }
    }

    /**
     * Render the current status of the countdown.
     */
    private void displayCountDown() {
        this.game.getBatch().begin();
        layout.setText(font, this.countDownString);
        font.draw(game.getBatch(), this.countDownString, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2.0f);
        this.game.getBatch().end();
    }

    /**
     * Render the pause screen over the game
     */
    private void renderPaused() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.game.getRenderer().begin(ShapeType.Filled);
		this.game.getRenderer().setColor(new Color(0, 0, 0, 0.5f));
		this.game.getRenderer().rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game.getRenderer().end();

        this.game.getBatch().begin();
        layout.setText(font, "PAUSED");
        fontWhite.draw(game.getBatch(), "PAUSED", (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() * 0.9f);


        this.saveButton.render(this.game.getBatch());
        if (this.saveButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            FileHandle saveFile = Gdx.files.external(Config.SAVE_FILE_NAME);
            saveFile.writeString(new JsonReader().parse(this.race.toJson()).toString(), false);
        }
        this.mainMenuButton.render(this.game.getBatch());
        if (this.mainMenuButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            this.game.setScreen(new MainMenuScreen(this.game));
        }
        this.exitButton.render(this.game.getBatch());
        if (this.exitButton.isHovering() && Gdx.input.isButtonJustPressed(0)) {
            Gdx.app.exit();
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
        this.game.getBatch().dispose();
    }
}
