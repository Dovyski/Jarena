/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

package br.uffs.cc.jarena.renders.flixel;

import java.awt.event.KeyListener;
import java.util.HashMap;

import org.flixel.*;
import br.uffs.cc.jarena.*;

public class DesenhistaFlixel implements Desenhista
{
	static Arena arena;
	private static final int TAM_SPRITE = 32;
	
	public DesenhistaFlixel() {
	}
	
	public void init(Arena a, KeyListener k) {
		arena = a;
		new FlxDesktopApplication(new Aplicacao(), Constants.LARGURA_TELA, Constants.ALTURA_TELA);
	}
	
	public void render() {
		HashMap <String,Object> dados;
		FlxSprite s = null;
		PlayState p = (PlayState) FlxG.getState();
		
		for(Entidade e : DesenhistaFlixel.arena.getEntidades()) {
			dados = e.getDados();
			
			if(dados.get("flxSprite") != null) {
				s = (FlxSprite)dados.get("flxSprite");
				
			} else if(p != null && p.isReady()){
				// Essa entidade nunca foi inserida no PlayState. Vamos
				// configurar ela e inserir então.
				s = reciclaSprite(e);
				s.reset(0, 0);
				
				// Marcamos ele como inserido no PlayState
				dados.put("flxSprite", s);
			}
			
			if(s != null) {
				s.x = e.getX();
				s.y = e.getY();
			}
		}
	}
	
	private FlxSprite reciclaSprite(Entidade e) {
		PlayState p = (PlayState) FlxG.getState();
		FlxSprite s = null;
		
		if(p != null && p.isReady()) {
			if(e instanceof Agente) {
				s = (FlxSprite) p.getAgents().getFirstAvailable();		
			} else if (e instanceof PontoEnergia) {
				s = (FlxSprite) p.getEnergyPoints().getFirstAvailable();
			}
		}
		
		return s;
	}
	
	public void terminate() {
	}

	public int getTamanho(Entidade e, int tipo) {
		int ret = 0;
		
		if(e instanceof Agente) {
			ret = TAM_SPRITE;
			
		} else if(e instanceof PontoEnergia) {
			ret = tipo == LARGURA ? 50 : 60;
		}
		
		return ret;
	}
	
	//////////////////////
	// Métodos invocados pela arena para avisar sobre acontecimentos importantes
	/////////////////////
	
	public void agenteRecebeuEnergia(Agente a) {
		//ativaAnimacao("energia", a, 500);
	}
	
	public void agenteTomouDano(Agente a) {
		
	}

	public void agenteBateuAlguem(Agente batendo, Agente apanhando) {
		
	}

	public void agenteGanhouCombate(Agente a) {
		
	}

	public void agenteMorreu(Agente a) {
		
	}
	
	public void agenteMorreuPorExcecao(Agente a, Exception e) {
		
	}

	public void agenteClonou(Agente origem, Agente clone) {
	}

	public void agenteEnviouMensagem(Agente a, String msg) {
		//ativaAnimacao("enviouMsg", a, 1000);	
	}

	public void agenteRecebeuMensagem(Agente destinatario, Agente remetente) {
		//ativaAnimacao("recebeuMsg", destinatario, 500);
	}
	
	public void entidadeAdicionada(Entidade e) {
	}
	
	public void entidadeRemovida(Entidade e) {
	}
}
