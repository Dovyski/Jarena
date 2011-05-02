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
	public static final int NENHUMA_DIRECAO				= 5;
	
	private int x;
	private int y;
	private int tipoDesenho; // tipo de desenho
	private int energia;
	private Arena arena;
	private int id;

	abstract void update();
	
	// Métodos públicos
	public Entidade(int x, int y, int energia) {
		this.x 			= 0;
		this.y 			= 0;
		this.energia 	= energia;
		this.id			= Entidade.contador++;
		
		setTipoDesenho(Entidade.DESENHO_ELFO);
	}
	
	
	public boolean gastaEnergia(int quanto) {
		boolean retorno = false;
		
		this.energia -= quanto;
		
		if(this.energia <= 0) {
			morre();
			retorno = true;
		}
		
		return retorno;
	}
	
	public void ganhaEnergia(int quanto) {
		this.energia += quanto;
	}
	
	public void morre() {
		getArena().removeEntidade(this);
	} 
	
	public final int getEnergia() {
		return this.energia;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	protected void alteraX(int quanto) {
		x += quanto;
	}
	
	protected void alteraY(int quanto) {
		y += quanto;
	}
	
	public void setArena(Arena a) {
		this.arena = a;
	}
	
	public Arena getArena() {
		return this.arena;
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
	
	public boolean isMorta() {
		return this.energia <= 0;
	}
}
