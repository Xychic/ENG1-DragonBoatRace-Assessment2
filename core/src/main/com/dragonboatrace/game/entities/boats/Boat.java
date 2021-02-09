package main.com.dragonboatrace.game.entities.boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import main.com.dragonboatrace.game.entities.Entity;
import main.com.dragonboatrace.game.entities.EntityType;
import main.com.dragonboatrace.game.entities.Obstacle;
import main.com.dragonboatrace.game.entities.PowerUp;
import main.com.dragonboatrace.game.tools.Hitbox;
import main.com.dragonboatrace.game.tools.Lane;
import main.com.dragonboatrace.game.tools.Config;

import java.util.Iterator;
import java.util.ListIterator;
/**
 * Represents a generic Boat.
 *
 * @author Benji Garment, Joe Wrieden, Babar Khan, Jacob Turner
 */
public class Boat extends Entity {

    /**
     * The rate at which the stamina is used or regenerated at.
     */
    private final float staminaRate = 10;

    /**
     * The minimum amount of speed gained from using stamina.
     */
    private final int minBoostSpeed = 5;

    /**
     * The formatter used to align the text on-screen.
     */
    protected GlyphLayout layout;

    /**
     * The health of the boat.
     */
    protected float health;

    /**
     * The max health of the boat.
     */
    protected float maxHealth;

    /**
     * The shield of the boat.
     */
    protected float shield;

    /**
     * The boost of the boat.
     */
    protected float boost;

    /**
     * The stamina of the boat.
     */
    protected float stamina;

    /**
     * The agility of the boat.
     */
    protected float agility;

    /**
     * The speed of the boat.
     * <p>
     * This is the speed attribute of the boat, not how fast it actually is moving.
     */
    protected float speed;

    /**
     * The maximum amount of stamina the boat can have.
     */
    protected float maxStamina;

    /**
     * The lane of the boat.
     */
    protected Lane lane;

    /**
     * The lanes hit box, use to determine if the boat is still in the lane.
     */
    protected Hitbox laneBox;

    /**
     * The name of the boat.
     */
    protected String name;

    /**
     * The total distance travelled by the boat.
     */
    protected float distanceTravelled = 0.0f;

    /**
     * If there has been a recent collision.
     */
    protected boolean recentCollision = false;

    /**
     * Timer used to countdown for when the boat can move again after a collision.
     */
    protected float collisionTime = 0;

    /**
     * Boat Type of boat used to remember the chosen boat type
     */
    protected BoatType boatType;

    /**
     * The current time of the boat in the current round.
     */
    protected float time;

    /**
     * Total time added up across all rounds.
     */
    protected float totalTime;

    /**
     * Total time penalties the boat got.
     */
    protected float penaltyTime;

    /**
     * Generator for FreeType Font.
     */
    protected FreeTypeFontGenerator generator;

    /**
     * Parameter for FreeType Font.
     */
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    /**
     * Font for Health Bar.
     */
    protected BitmapFont healthFont;

    /**
     * Font for Stamina Bar.
     */
    protected BitmapFont staminaFont;

    /**
     * Font for Shield Bar.
     */
    protected BitmapFont shieldFont;

    /**
     * Font for Shield Bar.
     */
    protected BitmapFont boostFont;

    /**
     * Font for Name Tag.
     */
    protected BitmapFont nameFont;

    // >>>> Modified in assessment 2 <<<<
    /**
     * Creates a Boat with the specified BoatType for pre-defined values,
     * a Lane to give the boat its position and a name for easy identification.
     *
     * @param boat The type of boat to use as a template.
     * @param lane The lane the boat is in.
     * @param name The name of the boat.
     */
    public Boat(BoatType boat, Lane lane, String name) {
        this(boat, lane, name, true);
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Creates a Boat with the specified BoatType for pre-defined values,
     * a Lane to give the boat its position and a name for easy identification.
     *
     * @param boat The type of boat to use as a template.
     * @param lane The lane the boat is in.
     * @param name The name of the boat.
     * @param loadTextures If the enity should load its textures.
     */
    public Boat(BoatType boat, Lane lane, String name, boolean loadTextures) {
        /* Get boat position from the position of the lane. */
        super(new Vector2(lane.getHitbox().getX() + (lane.getHitbox().getWidth() - EntityType.BOAT.getWidth()) / 2.0f, 100), new Vector2(), EntityType.BOAT, boat.getImageSrc(), loadTextures);
        this.shield = 0;
        this.boost = 0;
        this.maxHealth = boat.getHealth();
        this.health = boat.getHealth();
        this.stamina = boat.getStamina();
        this.agility = boat.getAgility();
        this.speed = boat.getSpeed();
        this.maxStamina = boat.getStamina();
        this.lane = lane;
        this.name = name;
        this.boatType = boat;
        this.time = 0;
        this.totalTime = 0;
        this.penaltyTime = 0;

        /* Store the lanes hit box to save time on using Getters. */
        laneBox = lane.getHitbox();
        if (loadTextures) {
            generateFonts();
        }
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Creates a Boat with the specified BoatType for pre-defined values,
     * a Lane to give the boat its position and a name for easy identification.
     *
     * @param boat The type of boat to use as a template.
     * @param lane The lane the boat is in.
     * @param name The name of the boat.
     * @param data The JsonValue that contains any other data the class requires to fully reconstruct it.
     */
    public Boat(Vector2 pos, Vector2 vel, BoatType boat, Lane lane, String name, JsonValue data) {
        this(pos, vel, boat, lane, name, data, true);
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Creates a Boat with the specified BoatType for pre-defined values,
     * a Lane to give the boat its position and a name for easy identification.
     *
     * @param boat The type of boat to use as a template.
     * @param lane The lane the boat is in.
     * @param name The name of the boat.
     * @param data The JsonValue that contains any other data the class requires to fully reconstruct it.
     * @param loadTextures If the enity should load its textures.
     */
    public Boat(Vector2 pos, Vector2 vel, BoatType boat, Lane lane, String name, JsonValue data, Boolean loadTextures) {
        super(pos, vel, EntityType.BOAT, boat.getImageSrc(), loadTextures);
        this.shield = data.getInt("shield");
        this.boost = data.getInt("boost");
        this.maxHealth = boat.getHealth();
        this.health = data.getFloat("health");
        this.stamina = data.getFloat("stamina");
        this.agility = boat.getAgility();
        this.speed = boat.getSpeed();
        this.maxStamina = boat.getStamina();
        this.lane = lane;
        this.name = name;
        this.boatType = boat;
        this.time = data.getFloat("time");
        this.totalTime = data.getFloat("totalTime");
        this.penaltyTime = data.getFloat("penaltyTime");
        this.distanceTravelled = data.getFloat("distanceTravelled");

        laneBox = lane.getHitbox();
        if (loadTextures) {
            generateFonts();
        }
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Method to generate the fonts needed for displaying the text.
     */
    private void generateFonts() {
        /* Setup fonts to use in the HUD */
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("osaka-re.ttf"));
        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.layout = new GlyphLayout();

        /*Font for displaying the name */
        parameter.size = 50;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        this.nameFont = generator.generateFont(parameter);

        layout.setText(nameFont, this.name);
        if (this.layout.width > this.laneBox.getWidth()) {
            parameter.size = (int) (50 / (this.layout.width / this.laneBox.getWidth()));
            parameter.color = Color.BLACK;
            nameFont = generator.generateFont(parameter);
        }

        /* Font for displaying the health */
        parameter.size = 50;
        parameter.color = Color.RED;
        this.healthFont = generator.generateFont(parameter);

        layout.setText(healthFont, "Health:  XXX");
        if (this.layout.width > this.laneBox.getWidth()) {
            parameter.size = (int) (50 / (this.layout.width / this.laneBox.getWidth()));
            parameter.color = Color.RED;
            healthFont = generator.generateFont(parameter);
        }

        /* Font for displaying the stamina */
        parameter.size = 50;
        parameter.color = Color.GREEN;
        this.staminaFont = generator.generateFont(parameter);

        layout.setText(staminaFont, "Stamina: XXX");
        if (this.layout.width > this.laneBox.getWidth()) {
            parameter.size = (int) (50 / (this.layout.width / this.laneBox.getWidth()));
            parameter.color = Color.GREEN;
            staminaFont = generator.generateFont(parameter);
        }

        /* Font for displaying the shield */
        parameter.size = 50;
        parameter.color = Color.BLUE;
        this.shieldFont = generator.generateFont(parameter);

        layout.setText(shieldFont, "Shield: XXX");
        if (this.layout.width > this.laneBox.getWidth()) {
            parameter.size = (int) (50 / (this.layout.width / this.laneBox.getWidth()));
            parameter.color = Color.BLUE;
            shieldFont = generator.generateFont(parameter);
        }

        /* Font for displaying the boost */
        parameter.size = 50;
        parameter.color = Color.YELLOW;
        this.boostFont = generator.generateFont(parameter);

        layout.setText(shieldFont, "Boost: XXX");
        if (this.layout.width > this.laneBox.getWidth()) {
            parameter.size = (int) (50 / (this.layout.width / this.laneBox.getWidth()));
            parameter.color = Color.YELLOW;
            boostFont = generator.generateFont(parameter);
        }
    }

    /**
     * Return a scalar to multiply the velocity by when using stamina.
     *
     * @return A float between 0.25 and 1 which is then scaled by {@link main.com.dragonboatrace.game.tools.Config#STAMINA_SPEED_DIVISION}.
     */
    protected float velocityPercentage() {
        double result = 0.25 + Math.log(this.stamina + 1) / 3;
        return (float) result / Config.STAMINA_SPEED_DIVISION;
    }

    /**
     * Get the amount of stamina to use for the current amount of stamina.
     * <p>
     * The amount of stamina that gets used per cycle is not linear. The more stamina you have the slower it is used,
     * and the less stamina the faster.
     *
     * @return A float of how much stamina will be used.
     */
    protected float useStamina() {
        double result = Math.pow(this.maxStamina, -this.stamina / (2 * this.maxStamina)) * this.staminaRate + this.staminaRate + this.minBoostSpeed;
        return (float) result;
    }

    /**
     * Get the amount of stamina that will be gained for the current amount of stamina.
     *
     * @return A float of how much stamina will be gained.
     */
    protected float regenerateStamina() {
        double result = -1 * this.staminaRate * Math.pow(this.maxStamina, -this.stamina / (2 * this.maxStamina)) + this.staminaRate + 1;
        return (float) result / 10;
    }

    /**
     * Update the position of the boat in relation to the amount of time passed.
     *
     * @param deltaTime The time passed since the last frame.
     */
    public void update(float deltaTime) {

        /* Check if boat is still in the lane */
        if (this.getHitBox().leaves(this.laneBox)) {
            this.penaltyTime += 0.1;
        }
        this.penaltyTime = Math.round(this.penaltyTime * 100) / (float) 100;

        this.distanceTravelled += this.velocity.y * deltaTime;

        /* Update lane contents */
        this.lane.update(deltaTime, this.velocity.y);

        /* Slowly return the velocity to 0 */
        float dampen = agility / 100;
        if (!(this.velocity.isZero((float) 0.001))) {
            this.position.x += this.velocity.x * deltaTime;
            this.velocity.scl(dampen);
        }

        // >>>> Added in assessment 2 <<<<
        if (this.boost > 0) {
            this.boost--;
            if (this.boost == 1) {
                this.speed -= 100;
            }
        }

        /* The hit box needs moving to keep at the same pos as the boat */
        this.hitbox.move(position.x, position.y);
    }

    /**
     * Render the boat and its lane contents. Renders the boat as well as its health and stamina.
     *
     * @param batch The SpriteBatch that the renders will be added to.
     * @param renderer The ShapeRender that renders the shield.
     */
    public void render(SpriteBatch batch, ShapeRenderer renderer) {
        batch.begin();
        this.lane.render(batch);

        layout.setText(healthFont, "Health:  XXX");

        healthFont.draw(batch, "Health:  " + (int) this.getHealth(), this.lane.getHitbox().getX() + 5, Gdx.graphics.getHeight() - 55);

        layout.setText(staminaFont, "Stamina: XXX");

        staminaFont.draw(batch, "Stamina: " + (int) this.getStamina(), this.lane.getHitbox().getX() + 5, Gdx.graphics.getHeight() - 105);

        // >>>> Added in assessment 2 <<<<
        layout.setText(shieldFont, "Shield: XXX");

        shieldFont.draw(batch, "Shield: " + (int) this.getShield(), this.lane.getHitbox().getX() + 5, Gdx.graphics.getHeight() - 155);

        // >>>> Added in assessment 2 <<<<
        layout.setText(boostFont, "Boost: XXX");
        boostFont.draw(batch, "Boost: " + (int) this.getBoost(), this.lane.getHitbox().getX() + 5, Gdx.graphics.getHeight() - 205);

        // >>>> Added in assessment 2 <<<<
        super.render(batch);
        batch.end();	
        if (this.shield > 0) {	
            Gdx.gl.glEnable(GL20.GL_BLEND);	
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
            batch.begin();	
            renderer.setColor(new Color(0, 0, 1, this.shield / 150));	
            renderer.begin(ShapeType.Filled);	
            renderer.circle(this.position.x + (EntityType.BOAT.getWidth() / 2), this.position.y + (EntityType.BOAT.getHeight() / 2), EntityType.BOAT.getHeight() * 0.6f);	
            renderer.end();	
            batch.end();	
        }
    }

    // >>>> Modified in assessment 2 <<<<
    /**
     * Check for collisions by getting the contents of the lane and checking their positions to the boat position.
     *
     * @return True if a collision occurred, False if no collision.
     */
    public boolean checkObstacleCollisions() {
        boolean recentCollision = false;

        /* Check for collisions with obstacles */
        Iterator<Obstacle> obstacleIterator = this.lane.getObstacles().iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if (obstacle.getHitBox().collidesWith(this.hitbox)) {
                obstacle.dispose();
                obstacleIterator.remove();

                float obstacleDamage = obstacle.getDamage();
                /* Manage health loss when there is a shield */
                if (this.shield == 0){
                    this.health -= obstacleDamage;
                } else {
                    /* if the shield is stronger than the damage of the obstacle */
                    if (this.shield >= obstacleDamage){
                        this.addShield(-1 * obstacleDamage);
                    }
                    /* if the shield is weaker than the damage of the obstacle */
                    else {
                        this.health -= (obstacleDamage - this.shield);
                        this.shield = 0;
                    }
                }
                recentCollision = true;
            }
        }

        return recentCollision;
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Check for collisions by getting the contents of the lane and checking their positions to the boat position.
     *
     * @return True if a collision occurred, False if no collision.
     */
    public boolean checkPowerUpCollisions() {
        boolean recentCollision = false;

        /* Check for collisions with power ups */
        Iterator<PowerUp> powerUpIterator = this.lane.getPowerUps().iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (powerUp.getHitBox().collidesWith(this.hitbox)) {
                recentCollision = true;
                powerUp.dispose();
                powerUpIterator.remove();
                
                switch (powerUp.getType()) {
                    case "CLEAR":
                        ListIterator<Obstacle> iter = this.lane.getObstacles().listIterator();
                        while (iter.hasNext()) {
                            Obstacle obstacle = iter.next();
                            iter.remove();
                            obstacle.dispose();
                            this.lane.removeObstacle(obstacle);
                        }
                        break;
                    case "SPEED":
                        this.boost += 100;
                        this.speed += 100;
                        break;
                    case "REPAIR":
                        this.health = this.maxHealth;
                        break;
                    case "SHIELD":
                        this.shield = 50;
                        break;
                    case "STAMINA":
                        this.stamina = this.maxStamina;
                        break;
                    default:
                        break;
                }
            }
        }
        return recentCollision;
    }

    /**
     * Update the vertical position of the boat onscreen.
     * <p>
     * This is not implemented in the generic Boat class and must be implemented in the specific Boat kind.
     *
     * @param lineHeight   The height of the finish line Entity.
     * @param raceDistance The length of the race.
     */
    public void updateYPosition(int lineHeight, int raceDistance) {

    }

    /* Adders */

    /**
     * Increase the velocity of the boat with a push.
     *
     * @param pushX The x component of the push
     * @param pushY The y component of the push.
     */
    public void addVelocity(float pushX, float pushY) {
        this.velocity.add(pushX, pushY);
    }

    /**
     * Increase the current boat health.
     *
     * @param change The amount of health to be added.
     */
    public void addHealth(float change) {
        this.health += change;
    }

    /**
     * Increase the current boat stamina.
     *
     * @param change The amount of stamina to be added.
     */
    public void addStamina(float change) {
        this.stamina += change;
    }

    /**
     * Increase the current boat shield.
     *
     * @param change The amount of shield to be added.
     */
    public void addShield(float change) {
        this.shield += change;
    }

    
    /**
     * Increase the current boat boost..
     *
     * @param change The amount of boost to be added.
     */
    public void addBoost(float change) {
        this.boost += change;
    }

    /* Getters */

    /**
     * Get the current velocity of the boat.
     *
     * @return A Vector2 of the boats current velocity.
     */
    public Vector2 getVelocity() {
        return this.velocity;
    }

    /**
     * Get the speed of the boat. <p>
     * This is the speed property of the boat, not the speed at which it is moving.
     *
     * @return The speed of the boat.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the current health of the boat.
     *
     * @return The health of the boat as a float.
     */
    public float getHealth() {
        return this.health;
    }

    /**
     * Get the current stamina of the boat.
     *
     * @return The stamina of the boat as a float.
     */
    public float getStamina() {
        return this.stamina;
    }

    public void setStamina(float value){
        this.stamina = value;
    }

    /**
     * Get the current shield of the boat.
     *
     * @return The shield of the boat as a float.
     */
    public float getShield() {
        return this.shield;
    }

    /**
     * Get the current shield of the boat.
     *
     * @return The shield of the boat as a float.
     */
    public float getBoost() {
        return this.boost;
    }

    /**
     * Get the current agility of the boat.
     *
     * @return The agility of the boat as a float.
     */
    public float getAgility() {
        return this.agility;
    }

    /**
     * Get the lane that the boat is in.
     *
     * @return The actual Lane object the boat is in.
     */
    public Lane getLane() {
        return this.lane;
    }

    /**
     * Get the name of the boat.
     *
     * @return A string of the boats name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the boat type.
     *
     * @return A BoatType.
     */
    public BoatType getBoatType() {
        return this.boatType;
    }


    /**
     * Get the boat time
     *
     * @return A float of boat time
     */
    public float getTime() {
        return this.time;
    }

    /**
     * Set the boat time
     * @param nowTime The time passed since last call.
     */
    public void setTime(float nowTime) {
        this.time += nowTime;
    }

    /**
     * Get the total boat time
     *
     * @return A float of total boat time
     */
    public float getTotalTime() {
        return this.totalTime;
    }

    /**
     * Set the total boat time.
     * @param nowTime The time passed since last call.
     */
    public void setTotalTime(float nowTime) {
        this.totalTime += nowTime;
    }

    public float getPenaltyTime() {
        return this.penaltyTime;
    }

    /**
     * Get the total distance travelled by the boat so far.
     *
     * @return A float of the distance travelled.
     */
    public float getDistanceTravelled() {
        return this.distanceTravelled;
    }

    /**
     * Dispose of the fonts used in the HUD and then perform {@link Entity}'s dispose.
     */
    public void dispose() {

        this.lane.dispose();
        super.dispose();
    }

    // >>>> Added in assessment 2 <<<<
    /**
     * Creates a JSON string needed to fully reconstruct the class.
     * 
     * @return JSON String contain all values needed to reconstruct the class.
     */
    public String toJson() {
        return String.format("{pos:{x:%f, y:%f}, vel:{x:%f, y:%f}, type:%s, lane:%s, name:%s, data:{shield:%f, boost:%f, health:%f, stamina:%f, time:%f, totalTime:%f, penaltyTime:%f, distanceTravelled:%f}}", 
            this.position.x,
            this.position.y,
            this.velocity.x,
            this.velocity.y,
            this.boatType,
            this.lane.toJson(),
            this.name,
            this.shield,
            this.boost,
            this.health,
            this.stamina,
            this.time,
            this.totalTime,
            this.penaltyTime,
            this.distanceTravelled
        );
    }
}
