package com.dragonboatrace.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import main.com.dragonboatrace.game.DragonBoatRace;
import main.com.dragonboatrace.game.tools.Config;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        Config.setResolution(size.width, size.height);
        config.width = Config.WIDTH;
        config.height = Config.HEIGHT;
        config.fullscreen = Config.FULLSCREEN;
        config.resizable = true;
        config.vSyncEnabled = false;
        config.foregroundFPS = 60;
        new LwjglApplication(new DragonBoatRace(), config);
    }
}
