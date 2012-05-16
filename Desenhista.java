import java.awt.event.KeyListener;

/**
 * Desenha os elementos da arena na tela.
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

interface Desenhista
{
	public void init(Arena a, KeyListener k);
	public void render();
	public void terminate();
	
	// Métodos invocados para avisar sobre coisas acontecendo na arena
	public void agenteRecebeuEnergia(Agente a);
	public void agenteTomouDano(Agente a);
	public void agenteBateuAlguem(Agente batendo, Agente apanhando);
	public void agenteGanhouCombate(Agente a);
	public void agenteMorreu(Agente a);
	public void agenteMorreuPorExcecao(Agente a, Exception e);
	public void agenteClonou(Agente origem, Agente clone);
	public void agenteEnviouMensagem(Agente a, String msg);
	public void agenteRecebeuMensagem(Agente destinatario, Agente remetente);
}
