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
import br.uffs.cc.jarena.Constants;
import br.uffs.cc.jarena.Entidade;
import br.uffs.cc.jarena.PontoEnergia;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;

public class PlayState extends FlxState
{
	private static final int MAX_AGENTS 		= 1000;
	private static final int MAX_ENERGY_POINTS 	= Constants.PONTO_ENERGIA_QUANTIDADE;
	
	private Boolean ready;
	private FlxGroup agents;
	private FlxGroup energyPoints;
	
	public PlayState() {
		ready = false;
	}
	
	public void create() {
		int i;
		
		FlxG.setBgColor(0xFF000000);
		
		agents 			= new FlxGroup(MAX_AGENTS);
		energyPoints 	= new FlxGroup(MAX_ENERGY_POINTS);
		
		add(agents);
		add(energyPoints);
		
		for(i = 0; i < MAX_AGENTS; i++) {
			agents.add(new Agent());
		}
		
		for(i = 0; i < MAX_ENERGY_POINTS; i++) {
			energyPoints.add(new EnergyPoint());
		}
		
		ready = true;
	}
	
	public void update() {
		super.update();
	}
	
	public Boolean isReady() 			{ return this.ready; }
	public FlxGroup getAgents() 		{ return this.agents; }
	public FlxGroup getEnergyPoints() 	{ return this.energyPoints; }
}
