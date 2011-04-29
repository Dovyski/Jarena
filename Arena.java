/**
 * Descreve uma arena para combate entre agentes. A arena contém uma lista com agentes
 * vivos e uma lista com os pontos de energia. Em cada iteração, todos os agentes vivos
 * são processados.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.Vector;

class Arena extends JFrame implements Runnable
{
	// Constantes para controle da tela (tamanho, resolução, etc).
	public static final int LARGURA_TELA 		= 800;
	public static final int ALTURA_TELA 		= 600;	
	public static final int TAM_PIXEL 			= 4;	
	
	// Define de quanto em quanto tempo a arena irá atualizar
	// todos os agentes
	public static final long INTERVALO_UPDATE 	= 100;
	
	private Vector<Entidade> entidades;
	private Vector<Entidade> nascendo;
	private Vector<Entidade> morrendo;
	private Entidade[][] mapa;
	private Desenhista desenhista;
	private ContextoArena contexto;
	
	public Arena() {
	    super("Arena PR-2011-T8");
	    
	    // Inicializamos as coisas da arena (agentes, energia, etc)
	    criaAmbiente();
	    adicionaPontosEnergia();
	    adicionaAgentes();
	    
	    // Depois que tudo estiver configurado, arrumamos a tela
	    // e iniciamos a aplicação
	    initTela();
	}
	
	private void initTela() {
		setBounds(50, 50, Arena.LARGURA_TELA, Arena.ALTURA_TELA);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Container tela 	= this.getContentPane();
	    this.desenhista	= new Desenhista(this); // essa classe que vai desenhar tudo na tela
		
		tela.add(this.desenhista);
		setVisible(true);
	}
	
	private void criaAmbiente() {
		mapa 		= new Entidade[Constants.ALTURA_MAPA][Constants.LARGURA_MAPA];
		entidades 	= new Vector<Entidade>();
		nascendo 	= new Vector<Entidade>();
		morrendo	= new Vector<Entidade>();
		contexto	= new ContextoArena(this);
	}
	
	private void adicionaAgentes() {
		// TODO: criar os agentes...
		AgenteDummy a = new AgenteDummy(0, 1, Constants.ENTIDADE_ENERGIA_INICIAL);
		adicionaEntidade(a);
	}
	
	private void adicionaPontosEnergia() {
		//TODO: criar os pontos de energia
	}
	
	public Vector<Entidade> getEntidades() {
		return this.entidades;
	}
	
	public void agendaNascimento(Entidade e) {
		this.nascendo.add(e);
	}
	
	public void agendaMorte(Entidade e) {
		System.out.println("[MORTE] " + e);
		this.morrendo.add(e);
	}
	
	public Entidade[][] getMapa() {
		return this.mapa;
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				update();
			} catch(InterruptedException e) {
				System.out.println("Thread da Arena acordou inexperadamente!");
			}
		}
	}
	
	private void update() {
		desenhista.repaint();
		
		for(Entidade e:entidades) {	
			e.update();
		} 
		
		if(nascendo.size() > 0) {
			for(Entidade j: nascendo) {	
				adicionaEntidade(j);
			}
			nascendo.removeAllElements();
		}
		
		if(morrendo.size() > 0) {
			entidades.removeAll(morrendo);
			morrendo.removeAllElements();
		}
	}
	
	private void adicionaEntidade(Entidade e) {
		e.setContexto(contexto);
		entidades.add(e);
		
		System.out.println("[NASCEU] " + e);
	}
  
	public static void main(String arg[]) {
		new Arena();
	}
}
