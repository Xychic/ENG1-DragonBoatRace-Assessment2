package com.dragonboatrace.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.dragonboatrace.DragonBoatRace;
import com.dragonboatrace.entities.FinishLine;
import com.dragonboatrace.entities.boats.Boat;
import com.dragonboatrace.entities.boats.BoatType;
import com.dragonboatrace.entities.boats.ComputerBoat;
import com.dragonboatrace.entities.boats.PlayerBoat;
import com.dragonboatrace.screens.GameOverScreen;
import com.dragonboatrace.screens.RoundsScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Race.
 *
 * @author Benji Garment, Joe Wrieden, Jacob Turner
 */
public class Race {
    /**
     * The length of the race.
     */
    private final int length;
    /**
     * The list of boats in the race, not including the player.
     */
    private final List<Boat> boats;
    /**
     * The players boat.
     */
    private final Boat player;
    /**
     * The finish line.
     */
    private final FinishLine finishLine;
    /**
     * The separator between each lane.
     */
    private final Texture barrier;

    /**
     * The timer for the race.
     */
    private float timer;

    private int round;

    /**
     * Creates a new race of a specified length.
     *
     * @param raceLength The length of the race.
     * @param boatChosen The {@link BoatType} that the player chose.
     * @param round The current round of the race.
     */
    public Race(int raceLength, BoatType boatChosen, int round) {
        this.length = raceLength;
        this.round = round;
        this.finishLine = new FinishLine(new Vector2(0, Gdx.graphics.getHeight()), Gdx.graphics.getWidth());
        int size = Gdx.graphics.getWidth() / Config.PLAYER_COUNT;
        this.timer = 0;

        this.player = new PlayerBoat(boatChosen, new Lane(new Vector2(0, 0), size, round), "Player");

        this.barrier = new Texture("line.png");

        boats = new ArrayList<Boat>();
        for (int i = 1; i < Config.PLAYER_COUNT; i++) {
            int rand = ThreadLocalRandom.current().nextInt(0, BoatType.values().length);
            boats.add(new ComputerBoat(BoatType.values()[rand], new Lane(new Vector2(size * i, 0), size, round), "COMP" + i, i));
        }
        this.timer = System.nanoTime();
    }

    public Race(JsonValue data) {
        this.length = data.getInt("length");
        this.round = data.getInt("round");
        this.finishLine = new FinishLine(new Vector2(0, Gdx.graphics.getHeight()), Gdx.graphics.getWidth());
        this.timer = 0;

        JsonValue playerJson = data.get("player");
        BoatType boatType = new Json().fromJson(BoatType.class, playerJson.getString("type"));
        Lane lane = new Lane(playerJson.get("lane"));
        String name = playerJson.getString("name");
        Vector2 pos = new Vector2(playerJson.get("pos").getFloat("x"), playerJson.get("pos").getFloat("y"));
        Vector2 vel = new Vector2(playerJson.get("vel").getFloat("x"), playerJson.get("vel").getFloat("y"));
        this.player = new PlayerBoat(pos, vel, boatType, lane, name, playerJson.get("data"));

        this.barrier = new Texture("line.png");

        this.boats = new ArrayList<Boat>();
        JsonIterator boatIter = data.get("boats").iterator();
        while (boatIter.hasNext()) {
            int boatNum = 1;
            JsonValue boatJson = boatIter.next();
            BoatType CPUBoatType = new Json().fromJson(BoatType.class, boatJson.getString("type"));
            Lane CPULane = new Lane(boatJson.get("lane"));
            String CPUName = boatJson.getString("name");
            Vector2 boatPos = new Vector2(boatJson.get("pos").getFloat("x"), boatJson.get("pos").getFloat("y"));
            Vector2 boatVel = new Vector2(boatJson.get("vel").getFloat("x"), boatJson.get("vel").getFloat("y"));
            this.boats.add(new ComputerBoat(boatPos, boatVel, CPUBoatType, CPULane, CPUName, boatJson.get("data"), boatNum++));
        }

        this.timer = System.nanoTime();
    }

    /**
     * Update the race in respects to the amount of time passed since the last frame.
     *
     * @param deltaTime The time since the last frame.
     * @param game The instance of the game.
     */
    public void update(float deltaTime, DragonBoatRace game) {
        player.updateYPosition(this.finishLine.getHitBox().getHeight(), length);
        player.update(deltaTime);
        finishLine.update(player.getDistanceTravelled(), this.length, deltaTime, player.getVelocity().y);
        if (player.getHealth() <= 0) {
            game.setScreen(new GameOverScreen(game, "Your boat is broken. Better luck next time!"));
        }
        for (Boat boat : this.boats) {

            ((ComputerBoat) boat).updateYPosition(player.getHitBox().getY(), player.getDistanceTravelled());
            boat.update(deltaTime);
            if (boat.getDistanceTravelled() + this.finishLine.getHitBox().getHeight() >= this.length && boat.getTime() == 0) {
                boat.setTime(Math.round((System.nanoTime() - this.timer) / 10000000) / (float) 100);
                boat.setTotalTime(boat.getTime());
            }
        }
        if (player.getDistanceTravelled() + this.finishLine.getHitBox().getHeight() >= this.length) {
            player.setTime(Math.round((System.nanoTime() - this.timer) / 10000000) / (float) 100);
            player.setTotalTime(player.getTime());
            List<Float> dnfList = new ArrayList<Float>();
            for (Boat boat : boats) {
                if (boat.getTime() == 0) {
                    dnfList.add(boat.getDistanceTravelled());
                }
            }
            Collections.sort(dnfList);
            Collections.reverse(dnfList);
            for (float dist : dnfList) {
                for (Boat boatN : boats) {
                    if (boatN.getDistanceTravelled() == dist) {
                        switch (dnfList.indexOf(dist) + 1) {
                            case 1:
                                boatN.setTime(player.getTime() + 1);
                                break;
                            case 2:
                                boatN.setTime(player.getTime() + 3);
                                break;
                            case 3:
                                boatN.setTime(player.getTime() + 5);
                                break;
                            default:
                                boatN.setTime(player.getTime() + ThreadLocalRandom.current().nextInt(6, 30));
                        }

                    }
                }
            }
            getLeaderBoard(game);
        }
    }

    /**
     * Render the boats in the race and the player boat.
     *
     * @param batch The SpriteBatch to be added to.
     */
    public void render(SpriteBatch batch, ShapeRenderer renderer) {
        renderer.begin(ShapeType.Filled);
        for (int i = 0; i < Config.PLAYER_COUNT; i++) {
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(
                (float) Gdx.graphics.getWidth() / Config.PLAYER_COUNT * i,
                0,
                5,
                Toolkit.getDefaultToolkit().getScreenSize().height
            );
        }
        renderer.end();
        batch.begin();
        finishLine.render(batch);
        batch.end();
        player.render(batch, renderer);
        for (Boat boat : this.boats) {
            boat.render(batch, renderer);
        }
    }

    /**
     * Generate the leaderboard from the race that just occurred and then show the next round screen.
     *
     * @param game The instance of the game.
     */
    public void getLeaderBoard(DragonBoatRace game) {
        List<Float> times = new ArrayList<Float>();
        String reason = "";
        player.setTime(this.player.getPenaltyTime());

        times.add(player.getTime());
        for (Boat boatN : boats) {
            times.add(boatN.getTime());
        }

        game.setPlayerTotalTime(times.get(0));
        for (int i = 0; i < Config.PLAYER_COUNT; i++) {
            game.setTimeAt(i, times.get(i));
        }
        boats.add(player);
        Collections.sort(times);
        List<Float> dup = new ArrayList<Float>(findDuplicates(times));
        if (dup.size() != 0) {
            times.set(times.indexOf(dup.get(0)), (float) (times.get(times.indexOf(dup.get(0))) + 0.02));
        }

        for (float time : times) {
            for (Boat boatN : boats) {
                if (boatN.getTime() == time) {
                    switch (times.indexOf(time) + 1) {
                        case 1:
                            if (game.getRound() == 4) {
                                reason += "Gold Medal:      " + boatN.getName() + "\n";
                            } else {
                                reason += "1st: " + boatN.getName() + "\n";
                            }
                            break;
                        case 2:
                            if (game.getRound() == 4) {
                                reason += "Silver Medal:    " + boatN.getName() + "\n";
                            } else {
                                reason += "2nd: " + boatN.getName() + "\n";
                            }
                            break;
                        case 3:
                            if (game.getRound() == 4) {
                                reason += "Bronze Medal:    " + boatN.getName() + "\n";
                            } else {
                                reason += "3rd: " + boatN.getName() + "\n";
                            }
                            break;
                        default:
                            if (game.getRound() != 4) {
                                reason += times.indexOf(time) + 1 + "th: " + boatN.getName() + "\n";
                            }
                    }
                }
            }
        }
        boats.remove(player);
        this.dispose();
        game.upRound();
        if (game.getRound() != 5) {
            game.setScreen(new RoundsScreen(game, this.player, reason));
        } else {
            game.setScreen(new GameOverScreen(game, reason));
        }
    }

    /**
     * Find any duplicates in an arraylist of floats.
     *
     * @param list An {@link ArrayList} of floats to be combed through.
     * @return An {@link Set} of type float containing unique values.
     */
    public Set<Float> findDuplicates(List<Float> list) {
        final Set<Float> setToReturn = new HashSet<Float>();
        final Set<Float> set1 = new HashSet<Float>();

        for (Float yourFloat : list) {
            if (!set1.add(yourFloat)) {
                setToReturn.add(yourFloat);
            }
        }
        return setToReturn;
    }

    /**
     * Get the players boat.
     *
     * @return A {@link Boat} representing the players boat.
     */
    public Boat getPlayer() {
        return this.player;
    }

    public List<Boat> getBoat() {return this.boats;}

    public void dispose() {
        for (Boat boat : this.boats) {
            boat.dispose();
        }
        this.finishLine.dispose();
        this.barrier.dispose();
    }

    public String toJson() {
        String[] CPUBoatJson = new String[this.boats.size()];
        for (int i=0;i<this.boats.size();i++){
            CPUBoatJson[i] = this.boats.get(i).toJson();
        }
        return String.format("{length:%d, round:%d, player:%s, boats:[%s]}", 
            this.length,
            this.round,
            this.player.toJson(),
            String.join(",", CPUBoatJson)
        );
    }
}
