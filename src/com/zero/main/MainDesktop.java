package com.zero.main;

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
//            TexturePacker.process(settings, "res", "res-packed");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.useCPUSynch = false;
		config.title = "Space shooter v0.0.2";
		config.width = 1024;
		config.height = 768;
		config.useGL20 = true;
		new LwjglApplication(new SpaceShooter2(), config);
	}

}
