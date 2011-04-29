/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import java.io.*;
import javax.imageio.*;

class Desenhista extends Canvas
{
	private Arena arena;
	
	public Desenhista(Arena a) {
		arena = a;
	}
	
	public void paint(Graphics g) {
		for(Entidade e : arena.getEntidades()) {
			e.desenha(g);
		}
		
		//g.drawOval(10, 10, 50, 50);
		
		//repaint();
		
		/*
		Image img = null;
		
		try {
			img = ImageIO.read(new File("imagens/sprites1.png"));
			
		} catch(IOException e) {
			System.out.println("Não foi possível carregar a imagem...");
			System.exit(0);
		}
		
		g.drawImage(img, 0, 0, this);*/
	}
}
