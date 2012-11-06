package br.uffs.cc.jarena.renders.flixel;

import java.util.HashMap;
import java.util.Vector;

import org.flixel.FlxG;
import org.flixel.FlxGamePad;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.AFlxCollision;

import br.uffs.cc.jarena.Agente;
import br.uffs.cc.jarena.Entidade;
import br.uffs.cc.jarena.PontoEnergia;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;

public class PlayState extends FlxState
{
	private Boolean pronto;
	private FlxGroup agents;
	private FlxGroup energyPoints;
	
	private FlxGamePad pad;
	
	public PlayState() {
		pronto = false;
	}
	
	public void create() {
		FlxG.setBgColor(0xFF000000);
		
		agents = new FlxGroup();
		add(agents);

		Agent a;
		
		for(int i = 0; i < 100; i++) {
			a = new Agent(0,0);
			a.kill();
			agents.add(a);
		}
		
		pronto = true;
	}
	
	public void update()
	{
		//This just says if the user clicked on the game to hide the cursor
		if(FlxG.mouse.justPressed()) {
			FlxG.mouse.hide();
		}
		
		//FlxG.overlap(playerBullets,vsPlayerBullets,new AFlxCollision(){@Override public void callback(FlxObject Object1, FlxObject Object2){stuffHitStuff(Object1, Object2);};});
		//FlxG.overlap(alienBullets,vsAlienBullets,new AFlxCollision(){@Override public void callback(FlxObject Object1, FlxObject Object2){stuffHitStuff(Object1, Object2);};});
		
		super.update();
	}
	
	public Boolean isPronto() 			{ return this.pronto; }
	public FlxGroup getAgentes() 		{ return this.agents; }
	public FlxGroup getPontosEnergia() 	{ return this.energyPoints; }
}
