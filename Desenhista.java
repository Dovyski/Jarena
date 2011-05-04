/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

class Desenhista extends Canvas
{
	private static final int TAM_SPRITE = 32;
	private static final long INTERVALO_RENDER_SPRITES = 200;
	private static final int FRAMES_SPRITE = 3;
	
	private Arena arena;
	private Image imgBackground;
	private Image imgSprites;
	
	public Desenhista(Arena a) {
		arena = a;
		carregaAssets();
	}
	
	public void paint(Graphics g) {
		desenhaBackground(g);
		
		for(Entidade e : arena.getEntidades()) {
			if(e instanceof Agente) {
				desenhaAgente(g, (Agente) e);
				
			} else if(e instanceof PontoEnergia) {
				desenhaPontoEnergia(g, (PontoEnergia) e);
			}
		}
	}
	
	private void carregaAssets() {
		try {
			imgBackground 	= ImageIO.read(new File("imagens/grass_background.jpg"));
			imgSprites	 	= ImageIO.read(new File("imagens/sprites1.png"));
			
		} catch(IOException e) {
			System.out.println("Não foi possível carregar a imagem...");
			System.exit(0);
		}	
	}
	
	private void desenhaSprite(Graphics g, int x, int y, int i, int j) {
		// 12x8
		g.drawImage(imgSprites, x, y, x + TAM_SPRITE, y + TAM_SPRITE, j * TAM_SPRITE, i * TAM_SPRITE, j * TAM_SPRITE + TAM_SPRITE, i * TAM_SPRITE+TAM_SPRITE, null);
	}
	
	private void desenhaBackground(Graphics g) {
		g.drawImage(imgBackground, 0, 0, this);
	}
	
	private void desenhaAgente(Graphics g, Agente a) {
		int i = 0, j = 0, frameAtual = 0;
		long tempoAgora = Calendar.getInstance().getTimeInMillis();
		
		//if(tempoAgora >= getTimeProximoRender(a)) {
			switch(a.getDirecao()) {
				case Agente.DIREITA:
					i = 2;
					break;
					
				case Agente.ESQUERDA:
					i = 1;
					break;
					
				case Agente.CIMA:
					i = 3;
					break;
					
				case Agente.BAIXO:	
					i = 0;
					break;
					
				default:
				// idle?
			}
		
			frameAtual = getFrameCorrente(a);
			
			desenhaSprite(g, a.getX(), a.getY(), i, frameAtual);			
			
			setTimeProximoRender(a, tempoAgora + INTERVALO_RENDER_SPRITES);
			incrementaFrame(a, frameAtual);
		//}
	}
	
	private Long getTimeProximoRender(Agente a) {
		Long tempo = (Long)a.getDados().get("proximoRender");
		return tempo != null ? tempo : 0;
	}
	
	private void setTimeProximoRender(Agente a, long tempo) {
		a.getDados().put("proximoRender", tempo);
	}
	
	private Integer getFrameCorrente(Agente a) {
		Integer frame = (Integer)a.getDados().get("frame");
		return frame != null ? frame : 0;
	}
	
	private void incrementaFrame(Agente a, int frameAtual) {
		a.getDados().put("frame", frameAtual < (FRAMES_SPRITE -1) ? frameAtual + 1 : 0);
	}
	
	private void desenhaPontoEnergia(Graphics g, PontoEnergia p) {
		Graphics2D g2d = (Graphics2D) g;
    
		g2d.setPaint(Color.GREEN);                                  
		g2d.fill(new Ellipse2D.Double(p.getX(), p.getY(), 25, 25));
	}
}
