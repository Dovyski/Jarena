package br.uffs.cc.jarena;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Recolhe estatísticas sobre a arena, como população existente, número de
 * batalhas vencidas, etc. 
 *
 */
public class Estatistico {
	public static final int TOTAL_DIVISOES 		= 0; // divisoes realizadas pela equipe
	public static final int MAX_POPULACAO 		= 1; // populacao maxima atingida pela equ
	public static final int MAX_ENERGIA_TOTAL 	= 2; // quantidade maxima de energia conseguida com a soma de energia de todo mundo vivo
	public static final int MAX_ENERGIA_AGENTE 	= 3; // quantidade maxima de energia que um agente conseguiu
	public static final int MAX_TEMPO_VIDA 		= 4; // quanto tempo a equipe durou na arena (em milisegundos)
	
	private static final int MAX_ESTATISTICAS 	= 5;
	
	private Arena arena;
	private HashMap<String, long[]> infos;
	private long ultimoUpdate;
	
	public Estatistico(Arena a) {
		arena 			= a;
		infos 			= new HashMap<String, long[]>();
		ultimoUpdate 	= 0;
	}
	
	public void colheEstatisticas() {
		Agente a;
		HashMap<String, long[]> tempInfos = new HashMap<String, long[]>();
		long[] nums;
		long[] numsTemp;
		
		for(Entidade e : arena.getEntidades()) {
			if(e instanceof Agente) {
				a 		 = (Agente) e;
				numsTemp = getInfoEquipe(a.getEquipe(), tempInfos);
				
				numsTemp[MAX_POPULACAO]++;
				numsTemp[MAX_ENERGIA_TOTAL] += a.getEnergia();
				
				if(a.getEnergia() > numsTemp[MAX_ENERGIA_AGENTE]) {
					numsTemp[MAX_ENERGIA_AGENTE] = a.getEnergia();
				}
				
				numsTemp[MAX_TEMPO_VIDA] = Calendar.getInstance().getTimeInMillis();
			}
		}
		
		Set<String> chaves = tempInfos.keySet();
		
		for (String chave : chaves) {
			nums 		= getInfoEquipe(chave, infos);
			numsTemp 	= getInfoEquipe(chave, tempInfos);
			
			for(int i = 0; i < nums.length; i++) {
				if(numsTemp[i] > nums[i]) {
					nums[i] = numsTemp[i];
				}
			}
		}
	}
	
	public void contabilizaDivisao(Agente a) {
		long[] nums = getInfoEquipe(a.getEquipe(), infos);
		nums[TOTAL_DIVISOES]++;
	}
	
	private long[] getInfoEquipe(String nome, HashMap<String, long[]> infos) {
		long[] nums = infos.get(nome);
		
		if(nums == null) {
			nums = new long[MAX_ESTATISTICAS];
			infos.put(nome, nums);
		}
		
		return nums;
	}
	
	public String getNomeFromIdEstatistica(int id) {
		String nome;
		
		switch(id) {
			case TOTAL_DIVISOES: 		nome = "Divisões"; break;
			case MAX_POPULACAO: 		nome = "População max."; break;
			case MAX_ENERGIA_TOTAL: 	nome = "Energia max. (equipe)"; break;
			case MAX_ENERGIA_AGENTE: 	nome = "Energia max. (agente)"; break;
			case MAX_TEMPO_VIDA: 		nome = "Tempo de vida"; break;
			default:					nome = "(desconhecido)"; break;
		}
		
		return nome;
	}
	
	public void imprimeEstatisticas() {
		Set<String> chaves = infos.keySet();
		long[] nums;
		long tempo;
		String infoTempo;

		System.out.println("\n\nEstatisticas");
		System.out.println("---------------------------------------\n");
		
		for (String chave : chaves) {
			nums = infos.get(chave);
			
			System.out.println("Equipe " + chave);
			
			for(int i = 0; i < nums.length; i++) {
				if(i == MAX_TEMPO_VIDA) {
					tempo = nums[i] - arena.getTimestampInicio();
					infoTempo = String.format("%d min, %d seg", 
						    TimeUnit.MILLISECONDS.toMinutes(tempo),
						    TimeUnit.MILLISECONDS.toSeconds(tempo) - 
						    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tempo))
					);
					System.out.println("\t" + getNomeFromIdEstatistica(i) + ": " + infoTempo);
				} else {
					System.out.println("\t" + getNomeFromIdEstatistica(i) + ": " + nums[i]);					
				}
			}
			System.out.println();
		}
	}
}
