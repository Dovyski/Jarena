/**
 * Representa um agente na arena. O agente pode se mover, guardar energia, se multiplicar e
 * morrer.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

class Agente extends Entidade
{
	public Agente(Integer x, Integer y, Integer energia) {
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
	
	public void pertoFonteEnergia() {
	}
	
	public void tomouDano() {
	}
	
	public String getEquipe() {
		return "Fernando";
	}
}
