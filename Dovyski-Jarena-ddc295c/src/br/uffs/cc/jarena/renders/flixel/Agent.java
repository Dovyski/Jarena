package br.uffs.cc.jarena.renders.flixel;

import org.flixel.*;

public class Agent extends FlxSprite
{	
	private static final String ImgAlien = "assets/flixel/pack:sprites1";	//The graphic of the squid monster
	
	public Agent() {
		super();
		loadGraphic(ImgAlien, true);

		addAnimation("default", new int[]{0,1,0,2}, (int)(6+FlxG.random()*4));
		play("default");
		
		kill();
	}
}
