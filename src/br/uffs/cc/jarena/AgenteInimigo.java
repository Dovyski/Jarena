/**
 * Um exemplo de agente que anda aleatoriamente na arena. Esse agente pode ser usado como base
 * para a criação de um agente mais esperto. Para mais explicações, veja o AgenteDummy.java.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

package br.uffs.cc.jarena;

public class AgenteInimigo extends Agente
{
	public AgenteInimigo(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
	}
	
	public void pensa() {
		if(!podeMoverPara(getDirecao())) {
			setDirecao(geraDirecaoAleatoria());
		}
	}
	
	public void recebeuEnergia() {
	}
	
	public void tomouDano(int energiaRestanteInimigo) {
	}
	
	public void ganhouCombate() {
	}
	
	public void recebeuMensagem(String msg) {
	}	
	
	public String getEquipe() {
		return "Inimigo";
	}
}
