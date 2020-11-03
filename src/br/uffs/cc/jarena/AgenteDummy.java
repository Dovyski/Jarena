
package br.uffs.cc.jarena;

public class AgenteDummy extends Agente
{
	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(500, 500, energia);
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
		if (getEnergia()>=900){
			morre();
		}
		
		
	}
	
	public void ganhouCombate() {
		
	}
	
	public void recebeuMensagem(String msg) {
			setDirecao(geraDirecaoAleatoria());
		
	}
	
	public String getEquipe() {
		
		return "pinto";
	}
}

