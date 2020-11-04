
package br.uffs.cc.jarena;

public class tlouzinhos extends Agente
{
	public tlouzinhos (Integer x, Integer y, Integer energia) {
		super(300, 400, energia);
		setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {
		
		if(!podeMoverPara(getDirecao())) {
			
			setDirecao(geraDirecaoAleatoria());
		}
		
		
		
			
			
	}
	
	public void recebeuEnergia() {
		enviaMensagem("GANHEI ENERGIA POLA");
		para();
			
		
			
		
	}
	
	public void tomouDano(int energiaRestanteInimigo) {
		setDirecao(geraDirecaoAleatoria());
		
		
	}
	
	public void ganhouCombate() {
		
	}
	
	public void recebeuMensagem(String msg) {
			if (isParado()){
				setDirecao(geraDirecaoAleatoria());
			}

		
	}
	
	public String getEquipe() {
		
		return "pepo";
	}
}

