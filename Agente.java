/**
 * Representa um agente na arena. O agente pode se mover, guardar energia, se multiplicar e
 * morrer.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

abstract class Agente extends Entidade
{	
	private int direcao;
	private boolean parado;	
	private boolean avisouMorte;	
	private boolean infosTrancadas;
	private boolean fazerDivisao;
	
	abstract void pensa();
	abstract void recebeuEnergia();
	abstract void tomouDano(Agente inimigo);
	abstract void ganhouCombate();
	abstract void recebeuMensagem(String msg, Agente remetente);
	abstract String getEquipe();	
	
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
		
	
	public Agente(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		setDirecao(geraDirecaoAleatoria());
		
		avisouMorte = false;
		fazerDivisao = false;
	}
	
	public final boolean gastaEnergia(int quanto) {
		return super.gastaEnergia(quanto);
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
		if(isMorta()) {
			return;
		}
		
		protegeInformacoes(true);
		pensa();
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
				ganhouCombate();
			}
		}
		
	}
	
	private boolean dahPancada(Agente inimigo) {
		boolean morreu;
		
		morreu = inimigo.gastaEnergia(Constants.ENTIDADE_COMBATE_DANO);
		
		if(!morreu) {
			inimigo.tomouDano(this);
		}
		
		return morreu;
	}
	
	private Agente getInimigoJuntoComigo() {
		Agente i = null, retorno = null;
		
		for(Entidade a : getArena().getEntidades()) {
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
	
	public final boolean isParado() {
		return this.parado;
	}
		
	public final void para() {
		this.parado = true;
	}
	
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
	
	public final boolean podeDividir() {
		return ((getEnergia() - Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR) / 2 ) > 0;
	}	
	
	public final int getDirecao() {
		return this.direcao;
	}
	
	public final void setDirecao(int direcao) {
		this.direcao = direcao;
		this.parado = false;
	}
	
	public final boolean divide() {
		if(podeDividir()) {
			fazerDivisao = true;			
			return true;
		} else {
			fazerDivisao = false;
			return false;
		}
	}
	
	private void fazDivisao() {
		fazerDivisao = false;
		
		if(podeDividir()) {
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR);
			gastaEnergia(getEnergia() / 2);
			
			getArena().divideEntidade(this);
		}
	}	
	
	public String toString() {
		return "["+getEquipe() + getId()+"] energia="+getEnergia()+", x="+getX()+", y="+getY() + ", status=" + (isParado() ? "parado":"andando");
	}
	
	public final void morre() {
		if(!avisouMorte) {
			avisouMorte = true;
			getArena().removeEntidade(this);
		}
	}
	
	public final void enviaMensagem(String msg) {
		Agente d;
		
		for(Entidade a : getArena().getEntidades()) {
			if((a instanceof Agente) && (distancia(a) <= Constants.AGENTE_ALCANCE_MENSAGEM)) {
				d = (Agente) a;
				d.recebeuMensagem(msg, this);
			}
		}
	}	
	
	protected final void alteraX(int quanto) {
		System.out.println("Agentes não podem alterar sua posição X diretamente. Use setDirecao().");
	}
	
	protected final void alteraY(int quanto) {
		System.out.println("Agentes não podem alterar sua posição Y diretamente. Use setDirecao().");
	}
}
