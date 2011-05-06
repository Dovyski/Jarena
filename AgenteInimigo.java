/**
 * Um exemplo de agente que anda aleatoriamente na arena.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

class AgenteInimigo extends Agente
{
	public AgenteInimigo(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
	}
	
	public void pensa() {
		if(!podeMoverPara(getDirecao())) {
			setDirecao(geraDirecaoAleatoria());
		}
		
		if(podeDividir()) {
			//divide();
		}
	}
	
	public void recebeuEnergia() {
	}
	
	public void tomouDano() {
	}
	
	public void ganhouCombate() {
	}
	
	public void recebeuMensagem(String msg, Agente remetente) {
	}	
	
	public String getEquipe() {
		return "Inimigo";
	}
}
