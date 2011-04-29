/**
 * Um exemplo de agente que anda aleatoriamente na arena.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

class AgenteDummy extends Agente
{
	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		System.out.println("Agente iniciado");
	}
	
	public void pensa() {
		if(!podeMoverPara(getDirecao())) {
			setDirecao(geraDirecaoAleatoria());
		}
		
		if(podeDividir()) {
			//divide();
		}
		
		if(getEnergia() >= 100 && getEnergia() <= 300) {
			para();
		} else {
			setDirecao(geraDirecaoAleatoria());
		}
	}
	
	public void recebeuEnergia() {
	}
	
	public void tomouDano() {
	}
	
	public String getEquipe() {
		return "Fernando";
	}
}
