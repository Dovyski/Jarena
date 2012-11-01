/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import javax.swing.JFrame;

import java.awt.event.KeyListener;
import java.util.HashMap;

import org.flixel.FlxDesktopApplication;

import com.yourname.flixelgame.FlxInvaders;

class DesenhistaFlixel extends JFrame implements Desenhista
{
	private static final int TAM_SPRITE = 32;
	private static final long INTERVALO_RENDER_SPRITES = 150;
	private static final int FRAMES_SPRITE = 3;
	
	private static final int SPRITE_RUIVO 		= 0;
	private static final int SPRITE_MENINA 		= 1;
	private static final int SPRITE_VERDE 		= 2;
	private static final int SPRITE_AZUL 		= 3;
	private static final int SPRITE_ELFO 		= 4;
	private static final int SPRITE_CHANEL 		= 5;
	private static final int SPRITE_MAL 		= 6;
	private static final int SPRITE_MESTRE 		= 7;
	
	private Arena arena;
	private Image imgBackground;
	private Image imgSprites;
	private Image imgPontosEnergia;
	private HashMap<String, Integer> tipoSprite;	
	private int spriteAtual;
	
	private Color corAuraEnergia 	= new Color(0f, 0f, 1f, 0.3f);
	private Color corAuraCombate 	= new Color(1f, 0f, 0f, 0.5f);
	private Color corAuraRecebeuMsg = new Color(0f, 1f, 0f, 0.5f);
	private Color corAuraEnviouMsg  = new Color(1f, 0f, 1f, 0.5f);
	
	public DesenhistaFlixel() {
	}
	
	public void init(Arena a, KeyListener k) {
		arena = a;
		tipoSprite = new HashMap<String, Integer>();
		spriteAtual = SPRITE_RUIVO;
		addKeyListener(k);
		
		new FlxDesktopApplication(new FlxInvaders(), 800, 480);
	}
	
	public void render() {
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
}
