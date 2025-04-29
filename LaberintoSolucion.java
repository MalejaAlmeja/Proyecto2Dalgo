import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class LaberintoSolucion
{
    public static String laberinto(int plataformas, int energia, String[] plataforma){
        ArrayList<String> camino = new ArrayList<String>();
        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)

        ArrayList<int[]> aristas =crearAristas(plataformas, energia, plataforma);
        //La tabla de acciones es nodo x unidad de energia restante, y el valor adentro es el número de acciones mínimas
        int [][] acciones = new int[plataformas+1][energia+1];

        //Inicializo la tabla de acciones
        for (int i = 1; i < plataformas; i++)
        {
            for (int j = 0; j <= energia; j++)
            {
                if (i==1)
                {
                    acciones[i][j]=0;
                }
                else
                {
                    acciones[i][j]=Integer.MAX_VALUE;
                }
            }
        }

        //CHEQUEA SI SI ES V-1 EN NUESTRO CASO O MENOS ITERACIONES
        //Bellman-Ford editado 
        for (int i = 1; i < plataformas; i++)
        {
            for (int numEdge = 0; numEdge < aristas.size(); numEdge++)
            {
                int[] edge= aristas.get(numEdge);
                int origen= edge[0];
                int destino= edge[1];
                int costoNormal= edge[2];
                int costoEnergia= edge[3];

                for (int iE=energia; iE>=costoEnergia; iE--)
                {
                    if (acciones[origen][iE]+costoNormal<acciones[destino][iE-costoEnergia])
                    {
                        acciones[destino][iE-costoEnergia]=acciones[origen][iE]+costoNormal;
                    }
                }
                
            }
        }
        
        int minAccion= Integer.MAX_VALUE;
        
        for (int i3=0; i3<=energia; i3++)
        {
            if (acciones[plataformas][i3]<minAccion)
            {
                minAccion= acciones[plataformas][i3];
            }
        }

        String strAccion;
        if (minAccion<Integer.MAX_VALUE)
        {
            strAccion= String.valueOf(minAccion);
        }
        else
        {
            strAccion= "NO SE PUEDE";
        }

        return strAccion;
    }

    /*
     * Crea una lista de aristas del siguiente formato: [origen, destino, costo normal, costo energia]
     */
    public static  ArrayList<int[]>  crearAristas(int plataformas, int energia, String[] plataforma)
    {
        ArrayList<int[]> aristas = new ArrayList<int[]>();


        for (int i = 1; i < plataformas; i++) 
        {
            //Añado aristas de caminar adelante y atras
            if (!plataforma[i].equals("R") && !plataforma[i+1].equals("R") ) 
            {
                int[] edge1= {i, i+1, 1, 0};
                int[] edge2= {i+1, i, 1, 0};
                aristas.add(edge1);
                aristas.add(edge2);
            }
            
            //Añado aristas de saltar
            if (plataforma[i] != null && !plataforma[i].equals("R") )
            {
                int numSaltos= Integer.parseInt(plataforma[i]);
                if (i-numSaltos >=1)
                {
                    if (!plataforma[i-numSaltos].equals("R"))
                    {
                        int[] edge={i, i-numSaltos, 1, 0};
                        aristas.add(edge);  
                    }
                }
                if (i+numSaltos<=plataformas)
                {
                    if (!plataforma[i+numSaltos].equals("R"))
                    {
                        int[] edge={i, i+numSaltos, 1, 0};
                        aristas.add(edge);  
                    }
                }
            }

            //Añado aristas de teletransportación
            if (!plataforma[i].equals("R") && energia>1)
            {
                for (int i2 = 2; i2 <= energia; i2++)
                {
                    if (!plataforma[i2+i].equals("R"))
                    {
                        int[] edge={i, i2+i, 1, i2};
                        aristas.add(edge);  
                    }
                }
            }
            

        }

        return aristas;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ncasos = Integer.parseInt(sc.nextLine());

        for (int t = 0; t < ncasos; t++) {
            String[] datos = sc.nextLine().trim().split(" ");
            int n = Integer.parseInt(datos[0]);
            int e = Integer.parseInt(datos[1]);

            String[] plataforma = new String[n+1];

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

            String ans = laberinto(n, e, plataforma);
            /* 
            ArrayList<String> camino = laberinto(n,e, plataforma);
            if (camino.isEmpty()) {
                System.out.println("No hay camino");
            } else {
                String movimientos = new String();
                movimientos += camino.size() + " ";
                for (String elemento : camino) {
                    movimientos+=elemento + " ";
                }
                System.out.println(movimientos);
            }
                */
            System.out.println(ans);
        }
        sc.close();
    }
}