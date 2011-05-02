/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

class Desenhista extends Canvas
{
	private Arena arena;
	
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
		Image img = null;
		
		try {
			img = ImageIO.read(new File("imagens/sprites1.png"));
			
		} catch(IOException e) {
			System.out.println("Não foi possível carregar a imagem...");
			System.exit(0);
		}	
	}
	
	private void desenhaBackground(Graphics g) {
		//g.drawImage(img, 0, 0, this);
	}
	
	private void desenhaAgente(Graphics g, Agente a) {
		Graphics2D g2d = (Graphics2D) g;
		Color c = null;
		
		if(a.getEquipe().equals("Fernando")) {
			c = Color.RED;
		} else {
			c = Color.BLUE;
		}
    
		g2d.setPaint(c);                                  
		g2d.fill(new Ellipse2D.Double(a.getX(), a.getY(), Constants.ENTIDADE_VELOCIDADE, Constants.ENTIDADE_VELOCIDADE));
	}
	
	private void desenhaPontoEnergia(Graphics g, PontoEnergia p) {
		Graphics2D g2d = (Graphics2D) g;
    
		g2d.setPaint(Color.GREEN);                                  
		g2d.fill(new Ellipse2D.Double(p.getX(), p.getY(), 25, 25));
	}
}
