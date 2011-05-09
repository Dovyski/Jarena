/**
 * Um exemplo de agente que anda aleatoriamente na arena.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

class AgenteDummy extends Agente
{
	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {
		if(!podeMoverPara(getDirecao())) {
			setDirecao(geraDirecaoAleatoria());
		}
		
		if(podeDividir() && getEnergia() >= 400) {
			divide();
		}
	}
	
	public void recebeuEnergia() {
	}
	
	public void tomouDano(Agente inimigo) {
	}
	
	public void ganhouCombate() {
	}
	
	public void recebeuMensagem(String msg, Agente remetente) {
	}
	
	public String getEquipe() {
		return "Fernando";
	}
}
