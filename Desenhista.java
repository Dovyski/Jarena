import java.awt.event.KeyListener;

/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

interface Desenhista
{
	public void init(Arena a, KeyListener k);
	public void render();
}
