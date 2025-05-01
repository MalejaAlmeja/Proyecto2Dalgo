import java.io.*;
import java.util.*;

public class BestAttempt {
    static class Edge {
        int origen, destino, costoEnergia;
        String tipoArista;

        public Edge(int origen, int destino, int costoEnergia, String tipoArista) {
            this.origen = origen;
            this.destino = destino;
            this.costoEnergia = costoEnergia;
            this.tipoArista = tipoArista;
        }
    }

    static class Estado {
        int pos, energia, costo;
        List<String> acciones;
        
        public Estado(int pos, int energia, int costo, List<String> acciones) {
            this.pos = pos;
            this.energia = energia;
            this.costo = costo;
            this.acciones = acciones;
        }
    }

    public static String laberinto(int plataformas, int energia, String[] plataforma) {
        // OptimizaciÃ³n inicial para teletransporte directo
        if (energia >= plataformas && !plataforma[plataformas].equals("R")) {
            return "1 T" + plataformas;
        }

        // Crear grafo optimizado
        ArrayList<ArrayList<Edge>> grafo = crearGrafo(plataformas, energia, plataforma);
        
        // BFS optimizado con poda
        PriorityQueue<Estado> cola = new PriorityQueue<>((a, b) -> {
            if (a.costo != b.costo) return Integer.compare(a.costo, b.costo);
            return Integer.compare(a.acciones.size(), b.acciones.size());
        });
        
        // Cache de estados visitados para poda
        Set<String> visitados = new HashSet<>();
        
        cola.offer(new Estado(0, energia, 0, new ArrayList<>()));
        int mejorCosto = Integer.MAX_VALUE;
        List<String> mejorCamino = null;

        while (!cola.isEmpty()) {
            Estado actual = cola.poll();
            
            if (actual.costo >= mejorCosto) continue;
            
            String estadoKey = actual.pos + "," + actual.energia;
            if (visitados.contains(estadoKey)) continue;
            visitados.add(estadoKey);

            if (actual.pos == plataformas) {
                if (actual.costo < mejorCosto) {
                    mejorCosto = actual.costo;
                    mejorCamino = new ArrayList<>(actual.acciones);
                }
                continue;
            }

            // Explorar vecinos de manera eficiente
            for (Edge arista : grafo.get(actual.pos)) {
                if (actual.energia >= arista.costoEnergia) {
                    List<String> nuevasAcciones = new ArrayList<>(actual.acciones);
                    nuevasAcciones.add(arista.tipoArista);
                    
                    Estado nuevo = new Estado(
                        arista.destino,
                        actual.energia - arista.costoEnergia,
                        actual.costo + 1,
                        nuevasAcciones
                    );
                    
                    cola.offer(nuevo);
                }
            }
        }

        if (mejorCamino != null) {
            return mejorCosto + " " + String.join(" ", mejorCamino);
        }
        return "NO SE PUEDE";
    }

    private static ArrayList<ArrayList<Edge>> crearGrafo(int plataformas, int energia, String[] plataforma) {
        ArrayList<ArrayList<Edge>> grafo = new ArrayList<>(plataformas + 1);
        for (int i = 0; i <= plataformas; i++) {
            grafo.add(new ArrayList<>());
        }

        for (int i = 0; i < plataformas; i++) {
            if (plataforma[i].equals("R")) continue;

            // Caminar
            if (i + 1 <= plataformas && !plataforma[i + 1].equals("R")) {
                grafo.get(i).add(new Edge(i, i + 1, 0, "C+"));
            }
            if (i > 0 && !plataforma[i - 1].equals("R")) {
                grafo.get(i).add(new Edge(i, i - 1, 0, "C-"));
            }

            // Saltos especiales
            if (!plataforma[i].equals("NA") && !plataforma[i].equals("FIN")) {
                try {
                    int salto = Integer.parseInt(plataforma[i]);
                    if (i + salto <= plataformas && !plataforma[i + salto].equals("R")) {
                        grafo.get(i).add(new Edge(i, i + salto, 0, "S+"));
                    }
                    if (i - salto >= 0 && !plataforma[i - salto].equals("R")) {
                        grafo.get(i).add(new Edge(i, i - salto, 0, "S-"));
                    }
                } catch (NumberFormatException e) {
                    // No es un nÃºmero, ignorar
                }
            }

            // Teletransporte optimizado
            if (energia > 0) {
                for (int dist = 1; dist <= Math.min(energia, plataformas - i); dist++) {
                    int destino = i + dist;
                    if (destino <= plataformas && !plataforma[destino].equals("R")) {
                        grafo.get(i).add(new Edge(i, destino, dist, "T" + dist));
                    }
                }
                for (int dist = 1; dist <= Math.min(energia, i); dist++) {
                    int destino = i - dist;
                    if (destino >= 0 && !plataforma[destino].equals("R")) {
                        grafo.get(i).add(new Edge(i, destino, dist, "T-" + dist));
                    }
                }
            }
        }
        return grafo;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int casos = Integer.parseInt(br.readLine().trim());
        
        for (int t = 0; t < casos; t++) {
            String[] linea = br.readLine().trim().split(" ");
            int n = Integer.parseInt(linea[0]);
            int e = Integer.parseInt(linea[1]);
            
            String[] plataforma = new String[n + 1];
            Arrays.fill(plataforma, "NA");
            
            // Leer robots
            linea = br.readLine().trim().split(" ");
            for (String robot : linea) {
                if (!robot.trim().isEmpty()) {
                    int pos = Integer.parseInt(robot);
                    if (pos >= 0 && pos <= n) {
                        plataforma[pos] = "R";
                    }
                }
            }
            
            // Leer poderes
            linea = br.readLine().trim().split(" ");
            for (int i = 0; i < linea.length - 1; i += 2) {
                int pos = Integer.parseInt(linea[i]);
                if (pos >= 0 && pos <= n) {
                    plataforma[pos] = linea[i + 1];
                }
            }
            
            plataforma[n] = "FIN";
            System.out.println(laberinto(n, e, plataforma));
        }
    }
}
