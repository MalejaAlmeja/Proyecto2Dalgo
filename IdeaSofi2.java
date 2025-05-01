import java.util.*;

public class IdeaSofi2 {
    
    static class Estado {
        int pos, energia;

        Estado(int p, int e) {
            pos = p;
            energia = e;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Estado)) return false;
            Estado e = (Estado) o;
            return pos == e.pos && energia == e.energia;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, energia);
        }
    }

    public static String laberinto(int plataformas, int energia, String[] plataforma) {
        int[][] acciones = new int[plataformas + 1][energia + 1];
        String[][] track = new String[plataformas + 1][energia + 1];
        for (int[] fila : acciones) Arrays.fill(fila, Integer.MAX_VALUE);

        Queue<Estado> cola = new LinkedList<>();
        Set<Estado> visitados = new HashSet<>();

        cola.add(new Estado(0, energia));
        acciones[0][energia] = 0;
        track[0][energia] = "";

        while (!cola.isEmpty()) {
            Estado actual = cola.poll();
            int pos = actual.pos;
            int ene = actual.energia;
            if (visitados.contains(actual)) continue;
            visitados.add(actual);

            if (pos == plataformas) continue;

            // Caminatas normales
            for (int dir : new int[]{-1, 1}) {
                int next = pos + dir;
                if (next >= 0 && next <= plataformas && !"R".equals(plataforma[next])) {
                    if (acciones[next][ene] > acciones[pos][ene] + 1) {
                        acciones[next][ene] = acciones[pos][ene] + 1;
                        track[next][ene] = track[pos][ene] + (dir == 1 ? "C+ " : "C- ");
                        cola.add(new Estado(next, ene));
                    }
                }
            }

            // Saltos con poder
            if (!"R".equals(plataforma[pos]) && !"NA".equals(plataforma[pos]) && !"FIN".equals(plataforma[pos])) {
                int salto = Integer.parseInt(plataforma[pos]);
                for (int dir : new int[]{-1, 1}) {
                    int next = pos + dir * salto;
                    if (next >= 0 && next <= plataformas && !"R".equals(plataforma[next])) {
                        if (acciones[next][ene] > acciones[pos][ene] + 1) {
                            acciones[next][ene] = acciones[pos][ene] + 1;
                            track[next][ene] = track[pos][ene] + (dir == 1 ? "S+ " : "S- ");
                            cola.add(new Estado(next, ene));
                        }
                    }
                }
            }

            // Teletransportaciones
            for (int d = 1; d <= ene; d++) {
                for (int dir : new int[]{-1, 1}) {
                    int next = pos + dir * d;
                    int nuevaEnergia = ene - d;
                    if (next >= 0 && next <= plataformas && !"R".equals(plataforma[next])) {
                        if (acciones[next][nuevaEnergia] > acciones[pos][ene] + 1) {
                            acciones[next][nuevaEnergia] = acciones[pos][ene] + 1;
                            track[next][nuevaEnergia] = track[pos][ene] + "T" + (dir * d) + " ";
                            cola.add(new Estado(next, nuevaEnergia));
                        }
                    }
                }
            }
        }

        int mejor = Integer.MAX_VALUE;
        String mejorRuta = "NO SE PUEDE";
        for (int e = 0; e <= energia; e++) {
            if (acciones[plataformas][e] < mejor) {
                mejor = acciones[plataformas][e];
                mejorRuta = mejor + " " + track[plataformas][e].trim();
            }
        }

        return mejorRuta;
    }

    // Prueba simple
    public static void main(String[] args) {
        int n = 9;
        int e = 2;
        String[] plataforma = new String[n + 1];
        Arrays.fill(plataforma, "NA");
        for (int r : new int[]{4, 5, 7}) plataforma[r] = "R";
        plataforma[1] = "4";
        plataforma[3] = "3";
        plataforma[6] = "5";
        plataforma[n] = "FIN";
        System.out.println(laberinto(n, e, plataforma));
    }
}

