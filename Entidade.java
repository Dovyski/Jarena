/**
 * Representa qualquer entidade que pode ocupar uma posição dentro da
 * arena. Qualquer elemento que for desenhado na arena deve estender
 * essa classe.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */
 
 import java.awt.*;
 
abstract class Entidade
{
	private static int contador							= 0;
	
	public static final int DESENHO_MAGO 				= 0;
	public static final int DESENHO_GUERREIRO_RUIVO 	= 1;
	public static final int DESENHO_ELFO 				= 2;
	
	public static final int DIREITA						= 1;
	public static final int ESQUERDA					= 2;
	public static final int CIMA						= 3;
	public static final int BAIXO						= 4;
	
	private int x;
	private int y;
	private int tipoDesenho; // tipo de desenho
	private int energia;
	private int direcao;
	private boolean parado;
	private boolean morta;
	private ContextoArena contexto;
	private int id;
	
	// Métodos abstratos
	abstract void pensa();
	abstract void pertoFonteEnergia();
	abstract void tomouDano();
	abstract String getEquipe();
	
	// Métodos privados
	private void gastaEnergia(int quanto) {
		this.energia -= quanto;
		
		if(this.energia <= 0) {
			// A entidade morreu!
			morre();
		}
	}
	
	private boolean movePara(int direcao) {
		boolean retorno = true;
		
		if(podeMoverPara(direcao)) {		
			switch(direcao) {
				case DIREITA:
					x += Constants.ENTIDADE_VELOCIDADE;
					break;
				case ESQUERDA:
					x -= Constants.ENTIDADE_VELOCIDADE;
					break;
				case CIMA:
					y -= Constants.ENTIDADE_VELOCIDADE;
					break;
				case BAIXO:
					y += Constants.ENTIDADE_VELOCIDADE;
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
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_ANDAR);
		}
		
		return retorno;
	}
	
	// Métodos públicos
	public Entidade(int x, int y, int energia) {
		this.x 			= 0;
		this.y 			= 0;
		this.morta 		= false;
		this.energia 	= energia;
		this.id			= Entidade.contador++;
		
		setDirecao(geraDirecaoAleatoria());
		setTipoDesenho(Entidade.DESENHO_ELFO);
	}
	
	public final void morre() {
		this.morta = true;
		contexto.removeEntidade(this);
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
	
	public final boolean podeMoverPara(int direcao) {
		boolean retorno = true;
		
		switch(direcao) {
			case DIREITA:
				if(x >= Constants.LARGURA_MAPA) {
					retorno = false;
				}
				break;
			case ESQUERDA:
				if(x <= 0) {
					retorno = false;
				}
				break;
			case CIMA:
				if(y <= 0) {
					retorno = false;
				}
				break;
			case BAIXO:
				if(y >= Constants.ALTURA_MAPA) {
					retorno = false;
				}
				break;
				
			default:
				retorno = false;
		}
		
		return retorno;
	}
	
	public final boolean podeDividir() {
		return ((getEnergia() - Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR) / 2 ) > 0;
	}
	
	public final boolean temInimigo(int direcao) {
		// TODO: fazer..
		return false;
	}
	
	public final int getEnergia() {
		return this.energia;
	}
	
	public final boolean divide() {
		if(podeDividir()) {
			gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_DIVIDIR);
			gastaEnergia(getEnergia() / 2);
			
			contexto.divideEntidade(this);
			
			return true;
		} else {
			return false;
		}
	}
	
	public final void update() {
		if(isMorta()) {
			return;
		}
		
		if(!isParado()) {
			movePara(getDirecao());
		}
		
		gastaEnergia(Constants.ENTIDADE_ENERGIA_GASTO_VIVER);
		System.out.println("[UPDATE] " + this);
	}
	
	public final void para() {
		this.parado = true;
	}
	
	public final boolean isParado() {
		return this.parado;
	}
	
	public final boolean isMorta() {
		return this.morta;
	}
	
	public void setContexto(ContextoArena contexto) {
		this.contexto = contexto;
	}
	
	public final int getX() {
		return this.x;
	}
	
	public final int getY() {
		return this.y;
	}
	
	public int getDirecao() {
		return this.direcao;
	}
	
	public void setDirecao(int direcao) {
		this.direcao = direcao;
		this.parado = false;
	}
	
	public void setTipoDesenho(int tipo) {
		this.tipoDesenho = tipo;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void desenha(Graphics g) {
		// TODO: desenhar as coisas aqui...
		g.drawOval(getX(), getY(), 50, 50);
	}
	
	public String toString() {
		return "["+getEquipe()+getId()+"] energia="+getEnergia()+", x="+getX()+", y="+getY() + ", status=" + (isParado() ? "parado":"andando");
	}
}
