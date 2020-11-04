
package br.uffs.cc.jarena;

public class tlouzinhos extends Agente
{
	public tlouzinhos (Integer x, Integer y, Integer energia) {
		super(500, 600, energia);
		setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {
		
		if(!podeMoverPara(getDirecao())) {
			
			setDirecao(geraDirecaoAleatoria());
		}
		
		
		
			
			
	}
	
	public void recebeuEnergia() {
		enviaMensagem("GANHEI ENERGIA POLA");
		int a;
		a = getDirecao();
		para();
			
		
			
		
	}
	
	public void tomouDano(int energiaRestanteInimigo) {
		setDirecao(geraDirecaoAleatoria());
		
		
	}
	
	public void ganhouCombate() {
		
	}
	
	public void recebeuMensagem(String msg) {
				int a;
				a = getDirecao();
				setDirecao(a);
				

			}

		
	
	
	public String getEquipe() {
		
		return "comit";
	}
}

