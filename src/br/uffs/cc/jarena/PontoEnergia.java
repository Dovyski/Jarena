/**
 * Representa um ponto de energia no mapa. Os pontos de energia recarregam a
 * energia dos agentes que estiverem próximos a ele.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

package br.uffs.cc.jarena;

public class PontoEnergia extends Entidade
{	
	private int velocidadeX;
	private int velocidadeY;
	private boolean andando;
	private long horaProximoEstado;
	
	public PontoEnergia(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		
		velocidadeX 		= (int)(Math.random() * Constants.PONTO_ENERGIA_VELOCIDADE) * (Math.random() < 0.5 ? -1 : 1);
		velocidadeY 		= (int)(Math.random() * Constants.PONTO_ENERGIA_VELOCIDADE) * (Math.random() < 0.5 ? -1 : 1);
		andando 			= false;
		horaProximoEstado 	= 0;
	}
	
	public void update() {
		if(isMorta()) {
			return;
		}
		
		ganhaEnergia(Constants.PONTO_ENERGIA_REGENERA_TURNO);
		
		for(Entidade a : getArena().getEntidades()) {
			if((a instanceof Agente) && (distancia(a) <= Constants.PONTO_ENERGIA_AREA)) {
				recarregaEnergiaAgente((Agente) a);
			}
		}

		if(Constants.PONTO_ENERGIA_MOVEL) {
			movimenta();
		}
	}
	
	private void movimenta() {
		long tempoAgora 				= System.currentTimeMillis();
		long tempoRestanteMudarEstado 	= horaProximoEstado - tempoAgora;
		
		if(andando) {
			evitaSairTela();
			alteraX(velocidadeX);
			alteraY(velocidadeY);
		}
		
		if(tempoRestanteMudarEstado <= 0) {
			andando = !andando;
			horaProximoEstado = tempoAgora + (andando ? Constants.PONTO_ENERGIA_TEMPO_ANDANDO : Constants.PONTO_ENERGIA_TEMPO_DESCANSO);
		}
	}
	
	private void evitaSairTela() {
		boolean movendoDireita 	= velocidadeX > 0;
		boolean movendoCima 	= velocidadeY < 0;
		
		if(movendoDireita && getX() >= Constants.LARGURA_MAPA) {
			velocidadeX *= -1;
		}

		if(!movendoDireita && getX() <= 0) {
			velocidadeX *= -1;
		}

		if(movendoCima && getY() <= 0) {
			velocidadeY *= -1;
		}

		if(!movendoCima && getY() >= Constants.ALTURA_MAPA) {
			velocidadeY *= -1;
		}
	}
	
	private void recarregaEnergiaAgente(Agente a) {
		// Primeiro, descontamos a energia do nosso estoque.
		a.ganhaEnergia(Constants.PONTO_ENERGIA_ENTREGA_TURNO);
		gastaEnergia(Constants.PONTO_ENERGIA_ENTREGA_TURNO);		
		
		// Depois avisamos o agente que ele recebeu energia.
		a.sinalizaRecebeuEnergia();
		
		System.out.println("PontoEnergia"+getId()+" dando vida para " + a);
	}
	
	public String toString() {
		return "[PontoEnergia" + getId()+"] energia="+getEnergia()+", x="+getX()+", y="+getY();
	}
}
