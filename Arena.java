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

import java.util.Calendar;
import java.util.Vector;
import java.lang.reflect.Constructor;

class Arena implements Runnable {
	// Constantes para controle da tela (tamanho, resolução, etc).
	public static final int LARGURA_TELA = 800;
	public static final int ALTURA_TELA = 600;
	public static final int TAM_PIXEL = 4;

	// Define de quanto em quanto tempo a arena irá atualizar
	// todos os agentes
	public static final long INTERVALO_UPDATE = 100;

	private Vector<Entidade> entidades;
	private Vector<Entidade> nascendo;
	private Vector<Entidade> morrendo;
	private Desenhista desenhista;
	private long ultimoUpdate;

	public Arena() {
		// Inicializamos as coisas da arena (agentes, energia, etc)
		criaAmbiente();
		adicionaPontosEnergia();
		adicionaAgentes();

		// Depois que tudo estiver configurado, arrumamos a tela
		// e iniciamos a aplicação
		initTela();
	}

	private void initTela() {
		this.desenhista = new DesenhistaSimples2D(); // TODO: usar o render definido no config.
		this.desenhista.init(this);
	}

	private void criaAmbiente() {
		ultimoUpdate = 0;
		entidades = new Vector<Entidade>();
		nascendo = new Vector<Entidade>();
		morrendo = new Vector<Entidade>();
	}

	private void adicionaAgentes() {
		int i;

		for (i = 0; i < 20; i++) {
			adicionaEntidade(new AgenteDummy(0, 0, Constants.ENTIDADE_ENERGIA_INICIAL));
			adicionaEntidade(new AgenteInimigo(Constants.LARGURA_MAPA, 0, Constants.ENTIDADE_ENERGIA_INICIAL));
		}
	}

	private void adicionaPontosEnergia() {
		double rand;
		int i, j;
		
		j = Constants.ALTURA_TELA/Constants.PONTO_ENERGIA_QUANTIDADE;

		for (i = 0; i < Constants.PONTO_ENERGIA_QUANTIDADE; i++) {
			rand = Math.random();
			adicionaEntidade(new PontoEnergia((int) (Constants.LARGURA_TELA * 0.8 * rand), j * i, Constants.PONTO_ENERGIA_SUPRIMENTO_INICIAL));
		}
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

	public void run() {
		long agora;
		
		while (true) {
			agora = Calendar.getInstance().getTimeInMillis();
			
			if ((agora - ultimoUpdate) >= Constants.INTERVALO_UPDATE) {
				update();
				ultimoUpdate = agora;
			}
			
			desenhista.render();
		}
	}

	private void update() {
		for (Entidade e : entidades) {
			if (!e.isMorta()) {
				e.update();
			}
		}

		if (nascendo.size() > 0) {
			for (Entidade j : nascendo) {
				adicionaEntidade(j);
			}
			nascendo.removeAllElements();
		}

		if (morrendo.size() > 0) {
			entidades.removeAll(morrendo);
			morrendo.removeAllElements();
		}
	}

	public void adicionaEntidade(Entidade e) {
		e.setArena(this);
		entidades.add(e);

		System.out.println("[NASCEU] " + e);
	}

	public void removeEntidade(Entidade entidade) {
		agendaMorte(entidade);
	}

	public void divideEntidade(Entidade entidade) {
		try {
			Class[] argsConstrutor = new Class[] { Integer.class,
					Integer.class, Integer.class };

			Class classe = entidade.getClass();
			Constructor construtor = classe.getConstructor(argsConstrutor);

			Entidade nova = (Entidade) construtor.newInstance(entidade.getX(),
					entidade.getY(), entidade.getEnergia());
			agendaNascimento(nova);

		} catch (Exception e) {
			System.out.println("Erro na hora de dividir a entidade!"
					+ e.getMessage());
		}
	}
}
