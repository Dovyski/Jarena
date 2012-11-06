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
	public FlxGroup agents;
	public FlxGroup energyPoints;
	
	private FlxGamePad pad;
	
	public PlayState() {
		pronto = false;
	}
	
	public void create() {
		FlxG.setBgColor(0xFF000000);
		
		pad = new FlxGamePad(FlxGamePad.LEFT_RIGHT, FlxGamePad.A);
		
		agents = new FlxGroup();
		add(agents);
			
		add(pad);
		pad.setAlpha(0.5f);

		adicionaSprites();
	}
	
	private void adicionaSprites() {
		Agent a;
		HashMap <String,Object> dados;
		
		for(Entidade e : DesenhistaFlixel.arena.getEntidades()) {
			if(e instanceof Agente) {
				dados = e.getDados();
				
				if(dados.get("flxSprite") == null) {
					// Esse agente nunca foi inserido no PlayState. Vamos
					// configurar ele e inserir então.
					a = new Agent(e.getX(), e.getY());
					agents.add(a);
					
					// Marcamos ele como inserido no PlayState
					dados.put("flxSprite", a);
				} else {
					// O agente já foi inserido alguma vez.
					throw new RuntimeException("Alguma coisa estranah");
				}
				
			} else if(e instanceof PontoEnergia) {
				
			}
		}
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
}
