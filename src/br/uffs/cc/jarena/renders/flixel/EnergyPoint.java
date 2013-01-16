package br.uffs.cc.jarena.renders.flixel;

import org.flixel.*;

public class EnergyPoint extends FlxSprite
{	
	private static final String ImgAlien = "assets/flixel/pack:alien";	//The graphic of the squid monster
	
	public EnergyPoint() {
		super();
		loadGraphic(ImgAlien, true);

		addAnimation("default", new int[]{0,1,0,2}, (int)(6+FlxG.random()*4));
		play("default");
		
		setColor(0xff0000ff);
		kill();
	}
}