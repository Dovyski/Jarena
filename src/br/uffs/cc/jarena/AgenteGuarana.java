package br.uffs.cc.jarena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AgenteGuarana extends Agente {

    private static final int EXPLORADOR = 0;
    private static final int COMBATENTE = 1;
    private static final int COLETOR = 2;

    /**
     * Energia que cada agente aceita gastar se movimentando antes de virar
     * sentinela permanente. Andar custa 3 de energia por turno (viver + andar)
     * e ficar parado custa 1, entao cada unidade gasta andando custa o triplo
     * de tempo de vida. Valor definido por medicao (ver ESTRATEGIA.md).
     */
    private static final int ORCAMENTO_EXPLORACAO = 50;

    private final int papel;
    private final int setorMinX;
    private final int setorMaxX;
    private final int setorMinY;
    private final int setorMaxY;
    private final int centroSetorX;
    private final int centroSetorY;

    private final int energiaInicial;

    private int turno;
    private boolean primeiroTurno = true;
    private boolean aposentado;

    private boolean varreduraHorizontal = true;
    private int direcaoHorizontal;
    private int direcaoVertical;
    private int passosVerticais;

    private boolean recebendoEnergia;
    private int turnosSemEnergia;
    private int sequenciaCogumelo;
    private int ultimoEnvioCogumelo = -100;

    private int alvoCogumeloX = -1;
    private int alvoCogumeloY = -1;
    private int validadeCogumelo;

    private boolean emCombate;
    private int combateX;
    private int combateY;
    private int energiaInimigo;
    private int turnosCombate;
    private int turnosSemDano;
    private int sequenciaCombate;
    private int validadeCombate;
    private boolean jaDividiuNoCombate;

    private int recuoTicks;
    private int direcaoRecuo;

    private final Map<Integer, int[]> aliados = new HashMap<Integer, int[]>();
    private final Set<String> eventosRecebidos = new HashSet<String>();
    private final ArrayList<String> ordemEventos = new ArrayList<String>();
    private final Map<String, Integer> ultimoReenvio = new HashMap<String, Integer>();

    public AgenteGuarana(Integer x, Integer y, Integer energia) {
        super(x, y, energia);

        papel = getId() % 5 == 0 ? EXPLORADOR
                : getId() % 5 == 1 ? COMBATENTE : COLETOR;

        int coluna = (getId() % 3);
        int linha = (getId() / 3) % 5;

        setorMinX = coluna * Constants.LARGURA_MAPA / 3;
        setorMaxX = (coluna + 1) * Constants.LARGURA_MAPA / 3;
        setorMinY = linha * Constants.ALTURA_MAPA / 5;
        setorMaxY = (linha + 1) * Constants.ALTURA_MAPA / 5;

        centroSetorX = (setorMinX + setorMaxX) / 2;
        centroSetorY = (setorMinY + setorMaxY) / 2;

        direcaoHorizontal = getId() % 2 == 0 ? DIREITA : ESQUERDA;
        direcaoVertical = getId() % 4 < 2 ? BAIXO : CIMA;
        setDirecao(direcaoHorizontal);

        energiaInicial = getEnergia();
    }

    @Override
    public void pensa() {
        turno++;
        atualizaTemporizadores();
        removeAliadosAntigos();

        if (primeiroTurno) {
            primeiroTurno = false;
            para();
            return;
        }

        if (!aposentado && getEnergia() < energiaInicial - ORCAMENTO_EXPLORACAO) {
            aposentado = true;
        }

        if (aposentado) {
            para();
            return;
        }

        if (turno % 8 == 0) {
            enviaPosicao();
        }

        if (emCombate) {
            if (deveDividir()) {
                jaDividiuNoCombate = true;
                divide();
                return;
            }

            if (deveRecuar()) {
                iniciaRecuo();
            } else {
                para();
                return;
            }
        }

        if (recuoTicks > 0) {
            executaRecuo();
            return;
        }

        if (validadeCombate > 0 && deveAtenderCombate()) {
            moverParaCoordenada(combateX, combateY);
            return;
        }

        if (recebendoEnergia) {
            para();
            return;
        }

        if (validadeCogumelo > 0 && deveBuscarCogumelo()) {
            moverParaCoordenada(alvoCogumeloX, alvoCogumeloY);
            return;
        }

        if (turno % 2 != 0) {
            para();
            return;
        }

        if (!estaDentroDoSetor()) {
            moverParaCoordenada(centroSetorX, centroSetorY);
            return;
        }

        fazVarredura();
    }

    private void atualizaTemporizadores() {
        if (recebendoEnergia) {
            turnosSemEnergia++;
            if (turnosSemEnergia > 2) {
                recebendoEnergia = false;
            }
        }

        if (emCombate) {
            turnosSemDano++;
            if (turnosSemDano > 2) {
                emCombate = false;
                turnosCombate = 0;
                jaDividiuNoCombate = false;
            }
        }

        if (validadeCogumelo > 0) {
            validadeCogumelo--;
        }

        if (validadeCombate > 0) {
            validadeCombate--;
        }
    }

    private void enviaPosicao() {
        enviaMensagem("P|" + getId() + "|" + getX() + "|" + getY() + "|" + getEnergia());
    }

    private void removeAliadosAntigos() {
        ArrayList<Integer> remover = new ArrayList<Integer>();

        for (Map.Entry<Integer, int[]> entrada : aliados.entrySet()) {
            if (turno - entrada.getValue()[3] > 15) {
                remover.add(entrada.getKey());
            }
        }

        for (Integer id : remover) {
            aliados.remove(id);
        }
    }

    private boolean estaDentroDoSetor() {
        return getX() >= setorMinX + 10 && getX() <= setorMaxX - 10
                && getY() >= setorMinY + 10 && getY() <= setorMaxY - 10;
    }

    private void fazVarredura() {
        if (varreduraHorizontal) {
            int proximoX = getX() + (direcaoHorizontal == DIREITA ? 5 : -5);

            if (proximoX < setorMinX + 10 || proximoX > setorMaxX - 10
                    || !podeMoverPara(direcaoHorizontal)) {
                varreduraHorizontal = false;
                passosVerticais = 0;
                if (!podeMoverPara(direcaoVertical)
                        || getY() + (direcaoVertical == BAIXO ? 5 : -5) < setorMinY + 10
                        || getY() + (direcaoVertical == BAIXO ? 5 : -5) > setorMaxY - 10) {
                    direcaoVertical = direcaoOposta(direcaoVertical);
                }
                para();
                return;
            }

            setDirecao(direcaoHorizontal);
            return;
        }

        int proximoY = getY() + (direcaoVertical == BAIXO ? 5 : -5);

        if (passosVerticais < 6 && proximoY >= setorMinY + 10
                && proximoY <= setorMaxY - 10 && podeMoverPara(direcaoVertical)) {
            setDirecao(direcaoVertical);
            passosVerticais++;
            return;
        }

        varreduraHorizontal = true;
        direcaoHorizontal = direcaoOposta(direcaoHorizontal);
        passosVerticais = 0;
        para();
    }

    private void moverParaCoordenada(int destinoX, int destinoY) {
        int dx = destinoX - getX();
        int dy = destinoY - getY();

        if (dx == 0 && dy == 0) {
            para();
            return;
        }

        int primeiraDirecao;
        int segundaDirecao;

        if (Math.abs(dx) >= Math.abs(dy)) {
            primeiraDirecao = dx > 0 ? DIREITA : ESQUERDA;
            segundaDirecao = dy > 0 ? BAIXO : CIMA;
        } else {
            primeiraDirecao = dy > 0 ? BAIXO : CIMA;
            segundaDirecao = dx > 0 ? DIREITA : ESQUERDA;
        }

        if (podeMoverPara(primeiraDirecao)) {
            setDirecao(primeiraDirecao);
        } else if ((dx != 0 || dy != 0) && podeMoverPara(segundaDirecao)) {
            setDirecao(segundaDirecao);
        } else {
            para();
        }
    }

    private boolean deveBuscarCogumelo() {
        if (papel == EXPLORADOR) {
            return distancia(alvoCogumeloX, alvoCogumeloY) <= 70;
        }
        return true;
    }

    private boolean deveAtenderCombate() {
        int distancia = distancia(combateX, combateY);
        return papel == COMBATENTE || distancia <= 100;
    }

    private boolean deveDividir() {
        return !jaDividiuNoCombate
                && turnosCombate >= 2
                && getEnergia() >= 700
                && getEnergia() <= 1300
                && energiaInimigo <= 350
                && getEnergia() >= energiaInimigo * 2 + 100
                && podeDividir();
    }

    private boolean deveRecuar() {
        return turnosCombate >= 8
                && energiaInimigo > getEnergia() + 120
                && aliadosProximos(combateX, combateY) == 0;
    }

    private int aliadosProximos(int x, int y) {
        int quantidade = 0;

        for (int[] info : aliados.values()) {
            if (turno - info[3] <= 10 && distancia(info[0], info[1], x, y) <= 100) {
                quantidade++;
            }
        }

        return quantidade;
    }

    private void iniciaRecuo() {
        int atual = getDirecao();
        direcaoRecuo = atual >= DIREITA && atual <= BAIXO
                ? direcaoOposta(atual) : direcaoParaCentro();

        if (!podeMoverPara(direcaoRecuo)) {
            direcaoRecuo = direcaoParaCentro();
        }

        recuoTicks = 8;
        emCombate = false;
        validadeCombate = 0;
        turnosCombate = 0;
        jaDividiuNoCombate = false;
        setDirecao(direcaoRecuo);
        enviaMensagem("R|" + getId() + "|" + getX() + "|" + getY());
    }

    private void executaRecuo() {
        if (podeMoverPara(direcaoRecuo)) {
            setDirecao(direcaoRecuo);
        } else {
            direcaoRecuo = direcaoParaCentro();
            if (podeMoverPara(direcaoRecuo)) {
                setDirecao(direcaoRecuo);
            } else {
                para();
            }
        }
        recuoTicks--;
    }

    private int direcaoParaCentro() {
        int dx = centroSetorX - getX();
        int dy = centroSetorY - getY();

        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0 ? DIREITA : ESQUERDA;
        }
        return dy >= 0 ? BAIXO : CIMA;
    }

    @Override
    public void recebeuEnergia() {
        if (!recebendoEnergia) {
            sequenciaCogumelo++;
        }

        recebendoEnergia = true;
        turnosSemEnergia = 0;
        alvoCogumeloX = getX();
        alvoCogumeloY = getY();

        if (turno - ultimoEnvioCogumelo >= 5) {
            ultimoEnvioCogumelo = turno;
            enviaMensagem("C|" + getId() + "|" + sequenciaCogumelo + "|"
                    + getX() + "|" + getY() + "|" + getEnergia() + "|0");
        }
    }

    @Override
    public void tomouDano(int energiaRestanteInimigo) {
        if (!emCombate) {
            sequenciaCombate++;
            turnosCombate = 0;
            jaDividiuNoCombate = false;
        }

        emCombate = true;
        turnosSemDano = 0;
        turnosCombate++;
        combateX = getX();
        combateY = getY();
        energiaInimigo = energiaRestanteInimigo;

        if (turnosCombate == 1 || turnosCombate % 3 == 1) {
            enviaMensagem("B|" + getId() + "|" + sequenciaCombate + "|"
                    + getX() + "|" + getY() + "|" + getEnergia() + "|"
                    + energiaRestanteInimigo + "|0");
        }
    }

    @Override
    public void ganhouCombate() {
        emCombate = false;
        validadeCombate = 0;
        turnosCombate = 0;
        jaDividiuNoCombate = false;
        enviaMensagem("V|" + getId() + "|" + getX() + "|" + getY());
    }

    @Override
    public void recebeuMensagem(String msg) {
        if (msg == null) {
            return;
        }

        String[] partes = msg.split("\\|");

        try {
            if (partes[0].equals("P") && partes.length == 5) {
                int id = Integer.parseInt(partes[1]);
                if (id != getId()) {
                    int[] info = new int[4];
                    info[0] = Integer.parseInt(partes[2]);
                    info[1] = Integer.parseInt(partes[3]);
                    info[2] = Integer.parseInt(partes[4]);
                    info[3] = turno;
                    aliados.put(id, info);
                }
                return;
            }

            if (partes[0].equals("C") && partes.length == 7) {
                processaCogumelo(partes);
                return;
            }

            if (partes[0].equals("B") && partes.length == 8) {
                processaCombate(partes);
                return;
            }

            if (partes[0].equals("V") && partes.length == 4) {
                int x = Integer.parseInt(partes[2]);
                int y = Integer.parseInt(partes[3]);
                if (validadeCombate > 0 && distancia(combateX, combateY, x, y) <= 50) {
                    validadeCombate = 0;
                }
                return;
            }

            if (partes[0].equals("R") && partes.length == 4) {
                int id = Integer.parseInt(partes[1]);
                if (id != getId()) {
                    int[] info = new int[4];
                    info[0] = Integer.parseInt(partes[2]);
                    info[1] = Integer.parseInt(partes[3]);
                    info[2] = 0;
                    info[3] = turno;
                    aliados.put(id, info);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void processaCogumelo(String[] partes) {
        int origem = Integer.parseInt(partes[1]);
        int sequencia = Integer.parseInt(partes[2]);
        int x = Integer.parseInt(partes[3]);
        int y = Integer.parseInt(partes[4]);
        int energia = Integer.parseInt(partes[5]);
        int saltos = Integer.parseInt(partes[6]);

        if (origem == getId()) {
            return;
        }

        String evento = "C:" + origem + ":" + sequencia;
        boolean novo = registrarEvento(evento);

        Integer ultimo = ultimoReenvio.get(evento);
        if (saltos < 1 && (novo || ultimo == null || turno - ultimo >= 8)) {
            ultimoReenvio.put(evento, turno);
            enviaMensagem("C|" + origem + "|" + sequencia + "|" + x + "|" + y
                    + "|" + energia + "|" + (saltos + 1));
        }

        boolean designado = papel == COLETOR
                && Math.abs(getId() % 2) == Math.abs(origem % 2);
        boolean proximo = distancia(x, y) <= 80;

        if (!emCombate && energia < 1800 && (designado || proximo)) {
            alvoCogumeloX = x;
            alvoCogumeloY = y;
            validadeCogumelo = 18;
        }
    }

    private void processaCombate(String[] partes) {
        int origem = Integer.parseInt(partes[1]);
        int sequencia = Integer.parseInt(partes[2]);
        int x = Integer.parseInt(partes[3]);
        int y = Integer.parseInt(partes[4]);
        int energiaAliado = Integer.parseInt(partes[5]);
        int energiaInimigoRecebida = Integer.parseInt(partes[6]);
        int saltos = Integer.parseInt(partes[7]);

        if (origem == getId()) {
            return;
        }

        String evento = "B:" + origem + ":" + sequencia;
        boolean novo = registrarEvento(evento);

        Integer ultimo = ultimoReenvio.get(evento);
        if (saltos < 2 && (novo || ultimo == null || turno - ultimo >= 4)) {
            ultimoReenvio.put(evento, turno);
            enviaMensagem("B|" + origem + "|" + sequencia + "|" + x + "|" + y
                    + "|" + energiaAliado + "|" + energiaInimigoRecebida + "|" + (saltos + 1));
        }

        int distancia = distancia(x, y);
        boolean deveIr = papel == COMBATENTE || distancia <= 100;

        if (deveIr && getEnergia() >= 220) {
            combateX = x;
            combateY = y;
            energiaInimigo = energiaInimigoRecebida;
            validadeCombate = 12;
            validadeCogumelo = 0;
        }
    }

    private boolean registrarEvento(String evento) {
        if (eventosRecebidos.contains(evento)) {
            return false;
        }

        eventosRecebidos.add(evento);
        ordemEventos.add(evento);

        if (ordemEventos.size() > 40) {
            String antigo = ordemEventos.remove(0);
            eventosRecebidos.remove(antigo);
            ultimoReenvio.remove(antigo);
        }

        return true;
    }

    private int distancia(int x, int y) {
        return distancia(getX(), getY(), x, y);
    }

    private int distancia(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    private int direcaoOposta(int direcao) {
        switch (direcao) {
            case DIREITA:
                return ESQUERDA;
            case ESQUERDA:
                return DIREITA;
            case CIMA:
                return BAIXO;
            case BAIXO:
                return CIMA;
            default:
                return DIREITA;
        }
    }

    @Override
    public String getEquipe() {
        return "Guarana";
    }
}