package br.uffs.cc.jarena.renders.flixel;

import org.flixel.*;

public class Agent extends FlxSprite
{	
	private static final String ImgAlien = "assets/flixel/pack:alien";	//The graphic of the squid monster
	
	public Agent(float x, float y) {
		super(x, y);
		loadGraphic(ImgAlien, true);

		addAnimation("default", new int[]{0,1,0,2}, (int)(6+FlxG.random()*4));
		play("default");
	}
}
