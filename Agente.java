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
	
	abstract void pensa();
	abstract void recebeuEnergia();
	abstract void tomouDano();
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
	}
	
	public final void gastaEnergia(int quanto) {
		super.gastaEnergia(quanto);
	}
	
	public final void ganhaEnergia(int quanto) {
		super.ganhaEnergia(quanto);
	}
	
	public final void update() {		
		if(isMorta()) {
			return;
		}
		
		pensa();
		
		if(!isParado()) {
			movePara(getDirecao());
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_ANDAR);
		}
		
		gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_VIVER);
		System.out.println("[UPDATE] " + this);
	}
	
	public boolean isParado() {
		return this.parado;
	}
		
	public void para() {
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
	
	public final boolean temInimigo(int direcao) {
		// TODO: fazer..
		return false;
	}
	
	public int getDirecao() {
		return this.direcao;
	}
	
	public void setDirecao(int direcao) {
		this.direcao = direcao;
		this.parado = false;
	}
	
	public final boolean divide() {
		if(podeDividir()) {
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR);
			gastaEnergia(getEnergia() / 2);
			
			getArena().divideEntidade(this);
			
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "["+getEquipe() + getId()+"] energia="+getEnergia()+", x="+getX()+", y="+getY() + ", status=" + (isParado() ? "parado":"andando");
	}
	
	public void morre() {
		if(!avisouMorte) {
			avisouMorte = true;
			super.getArena().removeEntidade(this);
		}
	}
	
	public final Arena getArena() {
		return null; // agentes não tem acesso à arena.
	}
	
	protected final void alteraX(int quanto) {
		System.out.println("Agentes não podem alterar sua posição X diretamente. Use setDirecao().");
	}
	
	protected final void alteraY(int quanto) {
		System.out.println("Agentes não podem alterar sua posição Y diretamente. Use setDirecao().");
	}
}
