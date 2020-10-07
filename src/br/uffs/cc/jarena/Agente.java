package br.uffs.cc.jarena;

/**
 * Representa um agente na arena. O agente pode se mover, guardar energia, se multiplicar e
 * morrer.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

abstract public class Agente extends Entidade
{	
	public static final int DIREITA						= 1;
	public static final int ESQUERDA					= 2;
	public static final int CIMA						= 3;
	public static final int BAIXO						= 4;
	public static final int NENHUMA_DIRECAO				= 5;	
	
	private int direcao;
	private boolean parado;	
	private boolean avisouMorte;	
	private boolean infosTrancadas;
	private boolean fazerDivisao;
	

	 // Métodos abstratos que precisam ser implementados
	 // por todas as classes filho.

	/**
	 * Chamado pela arena para que o agente decida a estratégia do turno. É nesse método
	 * que cada jogador deve colocar a lógica para fazer seu agente se movimentar, se dividir,
	 * decidir se combate, etc.
	 */
	public abstract void pensa();
	
	/**
	 * Invocado pela arena sempre que o agente receber energia de algum ponto de energia.
	 * Enquanto agente estiver no alcance do ponto de energia, esse método será invocado a
	 * cada turno.
	 */
	public abstract void recebeuEnergia();
	
	/**
	 * Invocado pela arena sempre que o agente estiver ocupando a mesma posição que um outro
	 * agente que não seja da sua equipe. Agentes que estiverem ocupando o mesmo lugar
	 * no espaço estarão, obrigatoriamente, batalhando. Durante uma batalha, ambos os agentes
	 * perdem uma quantidade de energia extra (que é o dano causado pelo agente adversário).
	 * 
	 * @param energiaRestanteInimigo quantidade de energia restante do inimigo com o qual se está batalhando.
	 */
	public abstract void tomouDano(int energiaRestanteInimigo);
	
	/**
	 * Invocado pela arena sempre que o agente estiver envolvido em uma batalha e o agente inimigo
	 * morrer. Esse método indica ao agente que ele ganhou o combate do agente que estava na mesma
	 * posição que ele (o inimigo morreu).
	 * 
	 * Ao ganhar um combate, o agente ganha um bonus de energia.
	 */
	public abstract void ganhouCombate();
	
	/**
	 * Invocado sempre que o agente receber uma mensagem de algum outro agente de sua equipe que estiver
	 * por perto. Para enviar uma mensagem, utilize o método enviaMensagem().
	 *  
	 * @param msg string contendo o contéudo da mensagem enviada pelo agente aliado.
	 */
	public abstract void recebeuMensagem(String msg);
	
	/**
	 * Esse método deve retornar uma string representando o nome da equipe. O nome da equipe
	 * deve ser algo único, porque esse nome é o que identifica a qual equipe o agente pertence.
	 * 
	 * @return nome da equipe do agente. Ex.: "OsMalvados", "Destruidores", "FulanoSilva".
	 */
	public abstract String getEquipe();	
	
	
	
	 // Métodos públicos que o jogador pode utilizar para criar a IA
	 // de seu agente.

	/**
	 * Testa se o agente pode se mover para uma determinada posição.
	 * 
	 * @param direcao direção a ser testada. Os valores possíveis são Agente.DIREITA, Agente.ESQUERDA, Agente.CIMA e Agente.BAIXO.
	 * @return true se o agente pode se mover para a direção indicada, ou false caso contrário (existe uma parede no caminho ou o mapa acabou, por exemplo).
	 */
	public final boolean podeMoverPara(int direcao) {
		boolean retorno = true;
		
		switch(direcao) {
			case DIREITA:
				if(getX() >= Constants.LARGURA_MAPA) {
					retorno = false;
				}
				break;
			case ESQUERDA:
				if(getX() <= 0) {
					retorno = false;
				}
				break;
			case CIMA:
				if(getY() <= 0) {
					retorno = false;
				}
				break;
			case BAIXO:
				if(getY() >= Constants.ALTURA_MAPA) {
					retorno = false;
				}
				break;
				
			default:
				retorno = false;
		}
		
		return retorno;
	}
		
	/**
	 * Construtor da classe.
	 * 
	 * @param x posição X no mapa onde o agente vai nascer.
	 * @param y posição Y no mapa onde o agente vai nascer.
	 * @param energia quantidade e energia inicial agente.
	 */
	public Agente(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		setDirecao(geraDirecaoAleatoria());
		
		avisouMorte = false;
		fazerDivisao = false;
	}
	
	/**
	 * Gasta uma quantidade de energia do agente. Lembre-se que se o agente ficar sem
	 * energia, ele morre.
	 * 
	 * @param quanto quantidade de energia que será retidada do agente.
	 */
	public final boolean gastaEnergia(int quanto) {
		return super.gastaEnergia(quanto);
	}
	
	/**
	 * Testa se o agente está parado. Um agente parado gasta menor energia do que
	 * um agente que está em movimento.
	 * 
	 * @return true se o agente está parado, ou false caso contrário.
	 */
	public final boolean isParado() {
		return this.parado;
	}
	
	/**
	 * Faz com que o agente pare de se movimentar. Um agente parado gasta menor energia
	 * do que um agente em movimento.
	 */
	public final void para() {
		this.parado = true;
	}
	
	/**
	 * Gera uma direção aleatória.
	 * 
	 * @return uma direção aleatória, que pode ser uma das seguintes: Agente.DIREITA, Agente.ESQUERDA, Agente.CIMA e Agente.BAIXO.
	 */
	public int geraDirecaoAleatoria() {
		double res 	= Math.random();
		int dir 	= DIREITA;
		
		if(res >= 0.75) {
			dir = DIREITA;
		} else if(res >= 0.50) {
			dir = ESQUERDA;
		} else if(res >= 0.25) {
			dir = CIMA;
		} else  {
			dir = BAIXO;
		}
		
		return dir;
	}
	
	/**
	 * Testa se o agente pode se dividir em dois. Ao se dividir, um agente gasta uma certa quantidade
	 * de energia; além disso, a energia do agente que gerou a divisão é distribuida igualmente com
	 * o novo agente criado. Isso quer dizer que se o agente tiver 1000 de energia e o custo da divisão
	 * for 100, o agente que está se dividindo terá sua energia subtraída de 100 (restando 900) e, depois,
	 * distribuída (o agente divisor e o novo agente terão, ambos, 450 de energia, que é o resultado
	 * de 900 / 2).
	 * 
	 * @return true se o agente tem energia suficiente para se dividir, ou false caso contrário.
	 */
	public final boolean podeDividir() {
		return ((getEnergia() - Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR) / 2 ) > 0;
	}	
	
	/**
	 * Obtém a direção atual para onde o agente está andando. Se o agente estiver parado,
	 * a direção será Agente.NENHUMA_DIRECAO.
	 * 
	 * @return a direção atual para onde o agente está andando. Pode ser: Agente.DIREITA, Agente.ESQUERDA, Agente.CIMA, Agente.BAIXO ou Agente.NENHUMA_DIRECAO (se o agente estiver parado).
	 */
	public final int getDirecao() {
		return this.direcao;
	}
	
	/**
	 * Seta a direção atual do agente. Depois de chamar esse método, o agente passará a se
	 * movimentar na direção fornecida.
	 * 
	 * @param direcao direção para a qual o agente deve andar a partir da chamada do método.
	 */
	public final void setDirecao(int direcao) {
		this.direcao = direcao;
		this.parado = false;
	}
	
	/**
	 * Clona o agente que invocou o método. Ao se dividir, um agente gasta uma certa quantidade
	 * de energia; além disso, a energia do agente que gerou a divisão é distribuida igualmente com
	 * o novo agente criado. Isso quer dizer que se o agente tiver 1000 de energia e o custo da divisão
	 * for 100, o agente que está se dividindo terá sua energia subtraída de 100 (restando 900) e, depois,
	 * distribuída (o agente divisor e o novo agente terão, ambos, 450 de energia, que é o resultado
	 * de 900 / 2).  
	 * 
	 * @return true se conseguiu fazer a divisão (tem energia, etc), ou false se não conseguiu (não possui energia suficiente, etc.)
	 */
	public final boolean divide() {
		if(podeDividir()) {
			fazerDivisao = true;			
			return true;
		} else {
			fazerDivisao = false;
			return false;
		}
	}
	
	/**
	 * Mata um agente instantaneamente (comete suicídio). A energia que o agente carregava
	 * é perdida.
	 */
	public final void morre() {
		if(!avisouMorte) {
			avisouMorte = true;
			super.getArena().getDesenhista().agenteMorreu(this);
			super.getArena().removeEntidade(this);
		}
	}
	
	/**
	 * Envia uma mensagem para os agentes que estão próximos.
	 * 
	 * @param msg string que será enviada como mensagem para os agentes que estão próximos. Todos os agentes que receberem a mensagem terão o método recebeuMensagem() invocado. 
	 */
	public final void enviaMensagem(String msg) {
		Agente d;
		
		for(Entidade a : super.getArena().getEntidades()) {
			if((a instanceof Agente) && (((Agente)a).getEquipe() == getEquipe()) && (distancia(a) <= Constants.AGENTE_ALCANCE_MENSAGEM)) {
				
				super.getArena().getDesenhista().agenteEnviouMensagem((Agente) a, msg);
				
				d = (Agente) a;
				d.sinalizaRecebeuMensagem(msg, this);
			}
		}
	}
	
	
	// Método privados.
	
	private boolean movePara(int direcao) {
		boolean retorno = true;
		
		if(podeMoverPara(direcao)) {		
			switch(direcao) {
				case DIREITA:
					super.alteraX(Constants.ENTIDADE_VELOCIDADE);
					break;
				case ESQUERDA:
					super.alteraX(-Constants.ENTIDADE_VELOCIDADE);
					break;
				case CIMA:
					super.alteraY(-Constants.ENTIDADE_VELOCIDADE);
					break;
				case BAIXO:
					super.alteraY(Constants.ENTIDADE_VELOCIDADE);
					break;
				default:
					retorno = false;
					break;
			}
		} else {
			retorno = false;
		}
		
		if(retorno) {
			parado = false;
		}
		
		return retorno;
	}
	
	private void protegeInformacoes(boolean status) {
		infosTrancadas = status;
	}
	
	private boolean isInfosProtegidas() {
		return infosTrancadas;
	}
	
	private void processaCombate() {
		Agente inimigo = getInimigoJuntoComigo();
		boolean morreu = false;
		
		if(inimigo != null) {
			morreu = dahPancada(inimigo);
			
			if(morreu) {
				ganhaEnergia(Constants.ENTIDADE_COMBATE_RECOMPENSA);
				sinalizaGanhouCombate();
			}
		}
		
	}
	
	private boolean dahPancada(Agente inimigo) {
		boolean morreu;
		
		morreu = inimigo.gastaEnergia(Constants.ENTIDADE_COMBATE_DANO);
		super.getArena().getDesenhista().agenteBateuAlguem(this, inimigo);
		
		if(!morreu) {
			inimigo.sinalizaTomouDano(this);
		}
		
		return morreu;
	}
	
	private Agente getInimigoJuntoComigo() {
		Agente i = null, retorno = null;
		
		for(Entidade a : super.getArena().getEntidades()) {
			if(a instanceof Agente) {
				i = (Agente) a;
				
				if(i.getX() == getX() && i.getY() == getY() && isInimigo(i)) {
					// Achamos alguém inimigo e que está na mesma posição
					// que estamos.
					retorno = i;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	private boolean isInimigo(Agente a) {
		return a.getEquipe() != getEquipe();
	}
	
	private void fazDivisao() {
		fazerDivisao = false;
		
		if(podeDividir()) {
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR);
			gastaEnergia(getEnergia() / 2);
			
			super.getArena().divideEntidade(this);
		}
	}
	
	public final void ganhaEnergia(int quanto) {
		if(!isInfosProtegidas()) {
			super.ganhaEnergia(quanto);
		}
	}
	
	public final Arena getArena() {
		return isInfosProtegidas() ? null : super.getArena();
	}
	
	public final void update() {
		if(isMorta() || isInfosProtegidas()) {
			return;
		}
		
		protegeInformacoes(true);
		try {
			pensa();
		} catch (Exception e) {
			morrePorErroExcecao(e);
		}
		protegeInformacoes(false);
		
		if(fazerDivisao) {
			fazDivisao();
		}
		
		if(!isParado()) {
			movePara(getDirecao());
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_ANDAR);
		}
		
		gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_VIVER);
		processaCombate();
	}
	
	private void morrePorErroExcecao(Exception e) {
		boolean status = isInfosProtegidas();
		
		System.out.println("*** [AGENTE] com EXCECAO, morrendo... " + this);
		e.printStackTrace();
		
		super.getArena().getDesenhista().agenteMorreuPorExcecao(this, e);
		
		protegeInformacoes(false);
		morre();
		protegeInformacoes(status);
	}
	
	public final void sinalizaRecebeuEnergia() {
		protegeInformacoes(true);
		try {
			recebeuEnergia();
			super.getArena().getDesenhista().agenteRecebeuEnergia(this);
			
		} catch (Exception e) {
			morrePorErroExcecao(e);
		}
		protegeInformacoes(false);
	}
	
	public final void sinalizaTomouDano(Agente inimigo) {
		protegeInformacoes(true);
		try {
			tomouDano(inimigo.getEnergia());
			super.getArena().getDesenhista().agenteTomouDano(this);
			
		} catch (Exception e) {
			morrePorErroExcecao(e);
		}
		protegeInformacoes(false);
	}
	
	public final void sinalizaGanhouCombate() {
		protegeInformacoes(true);
		try {
			ganhouCombate();
			super.getArena().getDesenhista().agenteGanhouCombate(this);
			
		} catch (Exception e) {
			morrePorErroExcecao(e);
		}		
		protegeInformacoes(false);
	}
	
	public final void sinalizaRecebeuMensagem(String msg, Agente remetente) {
		protegeInformacoes(true);
		try {
			recebeuMensagem(msg);
			super.getArena().getDesenhista().agenteRecebeuMensagem(this, remetente);
			
		} catch (Exception e) {
			morrePorErroExcecao(e);
		}
		protegeInformacoes(false);
	}
	
	public String toString() {
		return "["+getEquipe() + getId()+"] energia="+getEnergia()+", x="+getX()+", y="+getY() + ", status=" + (isParado() ? "parado":"andando");
	}	
	
	protected final void alteraX(int quanto) {
		System.out.println("Agentes não podem alterar sua posição X diretamente. Use setDirecao().");
	}
	
	protected final void alteraY(int quanto) {
		System.out.println("Agentes não podem alterar sua posição Y diretamente. Use setDirecao().");
	}
}
