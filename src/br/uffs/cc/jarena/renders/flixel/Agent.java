package br.uffs.cc.jarena.renders.flixel;

import org.flixel.*;

public class Agent extends FlxSprite
{	
	private static final String ImgShip = "assets/flixel/pack:ship";	//Graphic of the player's ship
	
	public Agent(float x, float y) {
		super(x, y, ImgShip);
	}
}
