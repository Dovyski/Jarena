/**
 * Descreve uma arena para combate entre agentes. A arena contém uma lista com agentes
 * vivos e uma lista com os pontos de energia. Em cada iteração, todos os agentes vivos
 * são processados.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.ArrayList;

class Constants
{
	// Constantes para controle da tela (tamanho, resolução, etc).
	public static final int LARGURA_TELA 						= 900;
	public static final int ALTURA_TELA 						= 700;	
	public static final int LARGURA_MAPA						= LARGURA_TELA - 40;	
	public static final int ALTURA_MAPA							= ALTURA_TELA - 40;	
	
	// Define de quanto em quanto tempo a arena irá atualizar
	// todos os agentes
	public static final long INTERVALO_UPDATE 					= 200;
	
	// Informações sobre entidade
	public static final int ENTIDADE_ENERGIA_INICIAL 			= 2000;	
	public static final int ENTIDADE_ENERGIA_GASTO_ANDAR 		= 2;
	public static final int ENTIDADE_ENERGIA_GASTO_VIVER 		= 1;
	public static final int ENTIDADE_ENERGIA_GASTO_DIVIDIR 		= 20;
	public static final int ENTIDADE_COMBATE_DANO 				= 5;
	public static final int ENTIDADE_COMBATE_RECOMPENSA			= 30;
	public static final int ENTIDADE_VELOCIDADE 				= 5;
	
	// Informações sobre agentes
	public static final int AGENTE_ALCANCE_MENSAGEM					= 70;
	
	// Informações sobre o estatistico
	public static final long ESTATISTICO_INTERVALO 					= 1000;
	
	// Informações sobre os pontos de energia
	public static final int PONTO_ENERGIA_SUPRIMENTO_INICIAL		= 1000;
	public static final int PONTO_ENERGIA_REGENERA_TURNO			= 0;
	public static final int PONTO_ENERGIA_ENTREGA_TURNO				= ENTIDADE_ENERGIA_GASTO_VIVER + ENTIDADE_ENERGIA_GASTO_ANDAR + 2;
	public static final double PONTO_ENERGIA_AREA					= 10;
	public static final int PONTO_ENERGIA_QUANTIDADE				= 5;
}
