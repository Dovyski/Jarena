package br.uffs.cc.jarena.renders.flixel;

import org.flixel.FlxG;
import org.flixel.FlxGame;

import br.uffs.cc.jarena.Constants;

public class Aplicacao extends FlxGame
{
	public Aplicacao()
	{
		super(Constants.LARGURA_TELA, Constants.ALTURA_TELA, PlayState.class, 1); //Create a new FlxGame object at 320x240 with 2x pixels, then load PlayState
		forceDebugger = true;
		
		FlxG.mouse.show();
	}
}