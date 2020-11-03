/**
 * Representa qualquer entidade que pode ocupar uma posição dentro da
 * arena. Qualquer elemento que for desenhado na arena deve estender
 * essa classe.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */
 
package br.uffs.cc.jarena;

import java.util.HashMap;
 
public abstract class Entidade
{
	private static int contador							= 0;
	
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
		
		this.energia -= quanto < 0 ? quanto*-1 : quanto;
		
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
		Desenhista d = arena.getDesenhista(); // IMPORTANTE: não pode ser getArena()! Vide Agente#protegeInformacoes().
		
		x = (a.getX() + d.getTamanho(a, Desenhista.LARGURA)/2) - (getX() + d.getTamanho(this, Desenhista.LARGURA)/2);
		y = (a.getY() + d.getTamanho(a, Desenhista.ALTURA)/2) - (getY() + d.getTamanho(this, Desenhista.ALTURA)/2);
		
		return Math.sqrt(x*x + y*y);
	}
}
