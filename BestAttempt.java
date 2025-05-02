import java.io.*;
import java.util.*;

public class BestAttempt {
    /*
     * Clase para poder guardar la info de una arista incluyendo su tipo de manera más sencilla
     */
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

    /* 
    * Clase para poder guardar el camino que tomamos para llegar a un nodo y la cantidad de energia que nos queda
    */
    static class NodoCamino {
        int energia;
        int numNodo;
        int costo;
        List<String> acciones;

        public NodoCamino(int numNodo, int energia, int costo, List<String> acciones) {
            this.energia = energia;
            this.costo = costo;
            this.acciones = acciones;
            this.numNodo = numNodo;
        }
    }

    /*
     * Queremos priorizar por costo de acciones del camino, pero si son iguales priorizamos por estado de energia
     */
    static class NodoCaminoComparator implements Comparator<NodoCamino> {
        @Override
        public int compare(NodoCamino a, NodoCamino b) 
        {
            if (a.costo != b.costo) 
                return Integer.compare(a.costo, b.costo);
            else 
                return Integer.compare(b.energia, a.energia);
        }
    }

    public static String laberinto(int plataformas, int energia, String[] plataforma) {
        //Caso de cuando tenemos suficiente energia para llegar a la meta en una sola teletransportación
        if (energia >= plataformas)
        { 
            return "1 T" + plataformas;
        }

        ArrayList<ArrayList<Edge>> grafo = crearGrafo(plataformas, energia, plataforma);

        //Creamos una cola de prioridad para priorizar los caminos más cortos
        PriorityQueue<NodoCamino> cola = new PriorityQueue<>(new NodoCaminoComparator());

        //Necesitamos guardar los estados de las plataformas que ya visitamos para ahorrar el calculo de ellos, y guardaremos el costo mínimo para llegar a ese estado
        int [][] estadosVisitados = new int[plataformas + 1][energia + 1];

        //Inicialiazamos la tabla de visitados con -1 que indica que no hemos visitado ese estado
        for(int i = 0; i < estadosVisitados.length; i++)
        {
            for(int j = 0; j < estadosVisitados[i].length; j++)
            {
                estadosVisitados[i][j] = -1;
            }
        }

        estadosVisitados[0][energia] = 0;
        cola.add(new NodoCamino(0, energia, 0, new ArrayList<>()));

        while (!cola.isEmpty()) {
            NodoCamino actual = cola.poll();

            //Si ya llegamos a la plataforma final, como es priority queue podemos devolver el camino más corto automaticamente
            if (actual.numNodo == plataformas) 
            {
                return actual.costo + " " + String.join(" ", actual.acciones);
            }

            //Si ya hemos estado en esta plataforma a un menor costo del que estamos ahora y con la misma cantidad de energia, saltamos el caso
            if (estadosVisitados[actual.numNodo][actual.energia]!=-1 && estadosVisitados[actual.numNodo][actual.energia] <= actual.costo)
            { 
                continue;
            }

            //Marcamos el nuevo estado en el que estamos con el costo que tenemos
            estadosVisitados[actual.numNodo][actual.energia]=actual.costo;

            //Recorremos las aristas que salen de la plataforma en la que estamos para crear los posibles caminos
            for (Edge arista : grafo.get(actual.numNodo)) 
            {
                //Solo consideramos la arista si tenemos suficiente energia para ella
                if (actual.energia >= arista.costoEnergia) 
                {
                    //Calculamos cual seria la energia restante si utilizamos esa arista 
                    int nuevaEnergia = actual.energia - arista.costoEnergia;

                    //Guardamos las acciones que tomamos para llegar a ese nuevo camino
                    List<String> nuevasAcciones = new ArrayList<>(actual.acciones);
                    nuevasAcciones.add(arista.tipoArista);

                    //Apilamos en la cola el nuevo camino posible que podemos tomar
                    cola.offer(new NodoCamino(arista.destino, nuevaEnergia, actual.costo + 1, nuevasAcciones));
                }
            }
        }

        return "NO SE PUEDE";
    }

    private static ArrayList<ArrayList<Edge>> crearGrafo(int plataformas, int energia, String[] plataforma) {
        ArrayList<ArrayList<Edge>> grafo = new ArrayList<>(plataformas + 1);

        //Creamos el grafo con la cantidad de plataformas
        for (int i = 0; i < plataformas; i++) 
        {
            grafo.add(i, new ArrayList<>());

            if ("R".equals(plataforma[i])) 
                continue;

            //Añadimos aristas de caminar adelante y atras
            if (i + 1 <= plataformas && !"R".equals(plataforma[i + 1])) 
            {
                grafo.get(i).add(new Edge(i, i + 1, 0, "C+"));
            }
            if (i > 0 && !"R".equals(plataforma[i - 1]))
            {
                grafo.get(i).add(new Edge(i, i - 1, 0, "C-"));
            }

            //Añadimos aristas de salto
            if (!"NA".equals(plataforma[i]) && !"FIN".equals(plataforma[i])) 
            {
                int salto = Integer.valueOf(plataforma[i]);

                if (i + salto <= plataformas && !"R".equals(plataforma[i + salto])) 
                {
                    grafo.get(i).add(new Edge(i, i + salto, 0, "S+"));
                }
                if (i - salto >= 0 && !"R".equals(plataforma[i - salto])) 
                {
                    grafo.get(i).add(new Edge(i, i - salto, 0, "S-"));
                }
            }

            //Añadimos aristas de teletransportación
            for (int dist = 2; dist <= energia; dist++) 
            {
                int adelante = i + dist;
                if (adelante <= plataformas && !"R".equals(plataforma[adelante])) 
                {
                    grafo.get(i).add(new Edge(i, adelante, dist, "T" + dist));
                }
                int atras = i - dist;
                if (atras >= 0 && !"R".equals(plataforma[atras])) 
                {
                    grafo.get(i).add(new Edge(i, atras, dist, "T-" + dist));
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

            // Robots
            linea = br.readLine().trim().split(" ");
            for (String robot : linea) {
                if (!robot.isEmpty()) {
                    int idx = Integer.parseInt(robot.trim());
                    if (idx >= 0 && idx <= n) plataforma[idx] = "R";
                }
            }

            // Poderes
            linea = br.readLine().trim().split(" ");
            for (int i = 0; i + 1 < linea.length; i += 2) {
                int idx = Integer.parseInt(linea[i].trim());
                String poder = linea[i + 1].trim();
                if (idx >= 0 && idx <= n && !"R".equals(plataforma[idx])) {
                    plataforma[idx] = poder;
                }
            }

            plataforma[n] = "FIN";
            System.out.println(laberinto(n, e, plataforma));
        }
    }
}
