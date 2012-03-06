package com.zero.spaceshooter;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//            Settings settings = new Settings();
//            settings.padding = 2;
//            settings.maxWidth = 2116;
//            settings.maxHeight = 2116;
//            settings.incremental = false;
//            TexturePacker.process(settings, "res/textures/packable", "res/atlases");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.useCPUSynch = false;
		config.title = "Space shooter v0.0.2";
		config.width = SpaceShooter2.DEFAULT_WIDTH;
		config.height = SpaceShooter2.DEFAULT_HEIGHT;
		config.useGL20 = true;
		config.fullscreen = false;
		new LwjglApplication(new SpaceShooter2(), config);
	}

}
