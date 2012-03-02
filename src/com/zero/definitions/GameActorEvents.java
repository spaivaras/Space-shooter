package com.zero.definitions;

public class GameActorEvents
{
	private static final int EVENT_BASE = 0;
	
	public static final int EVENT_DISPLAY_SPLASH_SCREEN = EVENT_BASE + 1;
	public static final int EVENT_TRANSITION_TO_MENU_SCENE = EVENT_BASE + 2;
	public static final int EVENT_TRANSITION_TO_SETTINGS_SCENE = EVENT_BASE + 3;
	public static final int EVENT_TRANSITION_TO_GAME_SCENE = EVENT_BASE + 4;
	public static final int EVENT_TRANSITION_TO_ABOUT_SCENE = EVENT_BASE + 5;
	public static final int EVENT_START_PULSE = EVENT_BASE + 6;
	public static final int EVENT_START_ASTEROID = EVENT_BASE + 7;
	public static final int EVENT_START_ASTEROID_EXPLOSION = EVENT_BASE + 8;
	public static final int EVENT_COLLISION_ASTEROID_SHIP = EVENT_BASE + 9;
	public static final int EVENT_COLLISION_ASTEROID_PULSE = EVENT_BASE + 10;
	public static final int EVENT_END_PULSE = EVENT_BASE + 11;
	public static final int EVENT_END_ASTEROID = EVENT_BASE + 12;
	public static final int EVENT_END_EXPLOSION = EVENT_BASE + 13;
	public static final int EVENT_EXIT = EVENT_BASE + 14;
}
