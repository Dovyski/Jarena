/**
 * Descreve o contexto no qual um agente está inserido. O agente pode perguntar para o contexto informações
 * como se existem alguém em determinada posição, o que está próximo dele, etc.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */
 
import java.lang.reflect.Constructor;

class ContextoArena
{	
	private Arena arena;
	
	public ContextoArena(Arena a) {
		arena = a;
	}
	
	public Entidade getEntidadeEm(int x, int y) {
		// TODO: rever nullpointer...
		// TODO: eliminar o mapa.
		Entidade[][] mapa = arena.getMapa();
		return mapa[x][y];
	}
	
	public void divideEntidade(Entidade entidade) {
		//Entidade nova = new Entidade(entidade.getX(), entidade.getY(), entidade.getEnergia());
		try {
            Class[] argsConstrutor = new Class[] { Integer.class, Integer.class, Integer.class };
			
			Class classe			= entidade.getClass();
			Constructor construtor 	= classe.getConstructor(argsConstrutor);
			
			Entidade nova = (Entidade) construtor.newInstance(entidade.getX(), entidade.getY(), entidade.getEnergia());
			arena.agendaNascimento(nova);
			
		} catch(Exception e) {
			System.out.println("Erro na hora de dividir a entidade!" + e.getMessage());
		}
	}
	
	public void removeEntidade(Entidade entidade) {	
		arena.agendaMorte(entidade);
	}
}
