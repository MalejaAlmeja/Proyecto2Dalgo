import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class DFS
{
    static class Edge
    {
        int origen;
        int destino;
        int costoNormal;
        int costoEnergia;
        String tipoArista;

        public Edge(int origen, int destino, int costoNormal, int costoEnergia, String tipoArista)
        {
            this.origen=origen;
            this.destino=destino;
            this.costoNormal=costoNormal;
            this.costoEnergia=costoEnergia;
            this.tipoArista=tipoArista;
        }
    }

    static class Accion
    {
        int aristaEscogida;
        ArrayList<Edge> aristasPosibles;

        public Accion(int aristaEscogida, ArrayList<Edge> aristasPosibles)
        {
            this.aristaEscogida=aristaEscogida;
            this.aristasPosibles=aristasPosibles;
        }
    }

    public static String laberinto(int plataformas, int energia, String[] plataforma, int numRobots){
        String ans="NO SE PUEDE";
        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)

        //Caso optimizado si tenemos un monton de teletransportación
        if (energia>= plataformas)
        {
            return "1 T"+String.valueOf(plataformas);
        }


        ArrayList<LinkedList<Edge>> grafo =crearGrafo(plataformas, energia, plataforma);


        //Variables auxiliares durante el recorrido 
        boolean[] visitado = new boolean[plataformas+1];
        int numEnergia=energia;
        boolean caminoValido=false;
        boolean endCamino=false;

        //Donde guardare el mejor camino posible
        Stack<Edge> bestCamino = new Stack<Edge>();
        int mejorCosto= Integer.MAX_VALUE - energia - 5;

        //Donde guardare las posibles acciones de visitar cada nodo
        Stack <Accion> acciones= new Stack<Accion>();

        //Añado la primer posible acción al stack
        ArrayList<Edge> aristasPosibles= new ArrayList<Edge>();
        for (Edge arista: grafo.get(0))
        {
            aristasPosibles.add(arista);
        }
        Accion accion= new Accion(0, aristasPosibles);
        acciones.push(accion);

        //Se acaba cuando ya he considerado todas las acciones posibles
        while (!acciones.isEmpty())
        {   
            //Se acaba cuando llegue a un dead end o a la plataforma final
            while (!endCamino)
            {
                Accion currAccion= acciones.peek();

                //Ya no hay más aristas posibles en esa acción, entonces podemos regresar a las acciones que teníamos antes de visitar ese nodo
                if (currAccion.aristaEscogida>=currAccion.aristasPosibles.size())
                {
                    caminoValido=false;
                    acciones.pop();
                    break;
                }

                Edge currArista = currAccion.aristasPosibles.get(currAccion.aristaEscogida);
                //Actualizo los visitados
                visitado[currArista.destino]=true;
                //Actualizo la energia restante 
                numEnergia-=currArista.costoEnergia;

                if (currArista.destino==plataformas)
                {
                   caminoValido=true;
                   break;
                }

                //Donde guardare las posibles aristas de la nueva acción
                ArrayList<Edge> aristasNuevaAccion= new ArrayList<Edge>();

                for (Edge aristaNodoDestino: grafo.get(currArista.destino))
                {
                    //Me aseguro que no estoy visitando un nodo que ya he visitado y que el costo de energia es menor o igual al que me queda
                    if (!visitado[aristaNodoDestino.destino] && aristaNodoDestino.costoEnergia<=numEnergia)
                    {
                        aristasNuevaAccion.add(aristaNodoDestino);
                    }
                }

                if (!aristasNuevaAccion.isEmpty())
                {
                    Accion nuevaAccion= new Accion(0, aristasNuevaAccion);
                    acciones.push(nuevaAccion);
                }
                else
                {
                    //La creo con 1 para que en la siguiente iteración la acción sea poppeada
                    Accion nuevaAccion= new Accion(1, aristasNuevaAccion);
                    acciones.push(nuevaAccion);
                }
            }

            if (caminoValido)
            {
                //Comparo el tamaño de este camino con el mejor camino, y si es mejor, lo guardo
                if (acciones.size()<mejorCosto)
                {
                    mejorCosto=acciones.size();
                    bestCamino.clear();
                    //Guardo las aristas del camino que fueron usadas
                    for (Accion acc :acciones)
                    {
                        bestCamino.push(acc.aristasPosibles.get(acc.aristaEscogida));
                    }
                }

                caminoValido=false;
            }

            if (acciones.isEmpty())
            {
                break;
            }
            
            Accion currAccion= acciones.peek();
            //Retorno el costo de energia de la arista que había usado
            numEnergia+=currAccion.aristasPosibles.get(currAccion.aristaEscogida).costoEnergia;
            //Desmarco el nodo como visitado
            visitado[currAccion.aristasPosibles.get(currAccion.aristaEscogida).destino]=false;

            //Considero la siguiente posible arista de la acción
            currAccion.aristaEscogida+=1;
        }

        
        if (!bestCamino.isEmpty())
        {
            ans=String.valueOf(bestCamino.size())+" ";
            for (Edge arist: bestCamino)
            {
                ans+=arist.tipoArista+" ";
            }
        }

        return ans;
    }

    /*
     * Crea una lista de aristas del siguiente formato: [origen, destino, costo normal, costo energia, numero de la arista]
     */
    public static  ArrayList<LinkedList<Edge>>  crearGrafo(int plataformas, int energia, String[] plataforma)
    {
        ArrayList<LinkedList<Edge>> grafo = new ArrayList<LinkedList<Edge>>() ;

        for (int i = 0; i < plataformas; i++) 
        {
            grafo.add(i, new LinkedList<Edge>());

            //Añado aristas de caminar adelante 
            if (!plataforma[i].equals("R") && !plataforma[i+1].equals("R") ) 
            {
                Edge edge1= new Edge(i, i+1, 1, 0, "C+");
                grafo.get(i).add(edge1);
            }

            //Añado aristas de caminar atras
            if (i!=0)
            {
                if (!plataforma[i].equals("R") && !plataforma[i-1].equals("R") ) 
                {
                    Edge edge2= new Edge(i, i-1, 1, 0, "C-");
                    grafo.get(i).add(edge2);
                }
            }
            
            //Añado aristas de saltar
            if (!plataforma[i].equals("NA") && !plataforma[i].equals("R") )
            {
                int numSaltos= Integer.parseInt(plataforma[i]);
                if (i-numSaltos >=1)
                {
                    if (!plataforma[i-numSaltos].equals("R"))
                    {
                        Edge edge3= new Edge(i, i-numSaltos, 1, 0, "S-");
                        grafo.get(i).add(edge3);
                    }
                }
                if (i+numSaltos<=plataformas)
                {
                    if (!plataforma[i+numSaltos].equals("R"))
                    {
                        Edge edge4= new Edge(i, i+numSaltos, 1, 0, "S+");
                        grafo.get(i).add(edge4);
                    }
                }
            }

            //Añado aristas de teletransportación
            if (!plataforma[i].equals("R") && energia>1)
            {
                for (int i2 = 2; i2 <= energia; i2++)
                {
                    if (i+i2<=plataformas)
                    {
                        if (!plataforma[i2+i].equals("R"))
                        {
                            Edge edge5= new Edge(i, i2+i, 1, i2, "T"+String.valueOf(i2));
                            grafo.get(i).add(edge5);
                        }
                    }
                    if (i-i2 >=1)
                    {
                        if (!plataforma[i-i2].equals("R"))
                        {
                            Edge edge6= new Edge(i, i-i2, 1, i2, "T-"+String.valueOf(i2));
                            grafo.get(i).add(edge6);

                        }
                    }
                }
            }
        }
        return grafo;
    }

     /* 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int ncasos = Integer.parseInt(sc.nextLine());

        for (int t = 0; t < ncasos; t++) {

            String[] datos = sc.nextLine().trim().split(" ");
            int n = Integer.parseInt(datos[0]);
            int e = Integer.parseInt(datos[1]);

            String[] plataforma = new String[n+1];

            for (int i1 =0; i1 < plataforma.length; i1++)
            {
                plataforma[i1]="NA";
            }

            String[] robots = sc.nextLine().trim().split(" ");
            
            for (String robot: robots) {
                plataforma[Integer.parseInt(robot)] = "R";
            }

            String[] powerUps = sc.nextLine().trim().split(" ");
            for (int i =0; i < powerUps.length;){
                plataforma[Integer.parseInt(powerUps[i])] = powerUps[i+1];
                i+=2;
            }
            plataforma[n]="FIN";
            
            String ans = laberinto(n,e, plataforma, robots.length);
            System.out.println(ans);
            
        }
        sc.close();
    }
    */

    
    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        //int ncasos = Integer.parseInt(sc.nextLine());

        int n = 14;
        int e = 2;

        String[] plataforma = new String[n+1];

        String[] robots = "4 5 7 9 10 12".split(" ");

        for (int i1 =0; i1 < plataforma.length; i1++)
        {
            plataforma[i1]="NA";
        }
        
        for (String robot: robots) {
            plataforma[Integer.parseInt(robot)] = "R";
        }

        String[] powerUps = "1 7 3 2 6 5 11 3".split(" ");
        for (int i =0; i < powerUps.length;){
            plataforma[Integer.parseInt(powerUps[i])] = powerUps[i+1];
            i+=2;
        }
        plataforma[n]="FIN";

        String ans = laberinto(n,e, plataforma, robots.length);
        System.out.println(ans);
    }
        
        
}
