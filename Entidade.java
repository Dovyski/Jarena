/**
 * Representa qualquer entidade que pode ocupar uma posição dentro da
 * arena. Qualquer elemento que for desenhado na arena deve estender
 * essa classe.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */
 
import java.util.HashMap;
 
abstract class Entidade
{
	private static int contador							= 0;
	
	public static final int DIREITA						= 1;
	public static final int ESQUERDA					= 2;
	public static final int CIMA						= 3;
	public static final int BAIXO						= 4;
	public static final int NENHUMA_DIRECAO				= 5;
	
	private int x;
	private int y;
	private int energia;
	private Arena arena;
	private int id;
	private HashMap<String, Object> dados;

	abstract void update();
	
	// Métodos públicos
	public Entidade(int x, int y, int energia) {
		this.x 			= x;
		this.y 			= y;
		this.energia 	= energia;
		this.id			= Entidade.contador++;
		this.dados		= new HashMap<String, Object>();
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
	
	public int getId() {
		return this.id;
	}
	
	public HashMap<String, Object> getDados() {
		return this.dados;
	}
	
	public boolean isMorta() {
		return this.energia <= 0;
	}
	
	public double distancia(Entidade a) {
		int x,y;
		
		x = a.getX() - getX();
		y = a.getY() - getY();
		
		return Math.sqrt(x*x - y*y);
	}
}
