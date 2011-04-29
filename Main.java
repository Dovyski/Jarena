/**
 * Faz uma batalha entre agentes dentro de uma arena.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

class Main
{	
	public static void main(String arg[]) {
		Arena a = new Arena();
		new Thread(a).start();
	}
}
