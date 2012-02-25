package com.zero.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.useCPUSynch = false;
		config.title = "Space shooter v0.0.1";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new SpaceShooter(), config);

	}

}
