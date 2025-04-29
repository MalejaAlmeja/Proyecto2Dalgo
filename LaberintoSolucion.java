import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LaberintoSolucion
{
    public static String laberinto(int plataformas, int energia, String[] plataforma){
        String ans="NO SE PUEDE";
        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)

        HashMap<Integer, String> tipoArista = new HashMap<Integer, String>();
        ArrayList<int[]> aristas =crearAristas(plataformas, energia, plataforma, tipoArista);
        //La tabla de acciones es nodo x unidad de energia restante, y el valor adentro es el número de acciones mínimas en pos 0, y acciones en pos 1
        int [][] acciones = new int[plataformas+1][energia+1];
        String [][] track = new String[plataformas+1][energia+1];

        //Inicializo la tabla de acciones
        for (int i = 0; i <= plataformas; i++)
        {
            for (int j = 0; j <= energia; j++)
            {
                if (i==0)
                {
                    acciones[i][j]=0;
                    track[i][j]="";
                }
                else
                {
                    acciones[i][j]=Integer.MAX_VALUE - energia - 5;
                    track[i][j]="";
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
                        track[destino][iE-costoEnergia]=track[origen][iE]+tipoArista.get(edge[4])+" ";

                    }
                }
                
            }
        }
        
        int minAccion= Integer.MAX_VALUE - energia - 5;
        
        for (int i3=0; i3<=energia; i3++)
        {
            if (acciones[plataformas][i3]<minAccion)
            {
                minAccion= acciones[plataformas][i3];
                ans=String.valueOf(minAccion)+" "+track[plataformas][i3];
            }
        }

        return ans;
    }

    /*
     * Crea una lista de aristas del siguiente formato: [origen, destino, costo normal, costo energia, tipo]
     */
    public static  ArrayList<int[]>  crearAristas(int plataformas, int energia, String[] plataforma, HashMap<Integer, String> tipoArista)
    {
        ArrayList<int[]> aristas = new ArrayList<int[]>();
        int numAristas=0;

        for (int i = 0; i < plataformas; i++) 
        {
            //Añado aristas de caminar adelante (y de paso de atras)
            if (!plataforma[i].equals("R") && !plataforma[i+1].equals("R") ) 
            {
                numAristas++;
                int[] edge1= {i, i+1, 1, 0, numAristas};
                aristas.add(edge1);
                tipoArista.put(numAristas, "C+");

                numAristas++;
                int[] edge2= {i+1, i, 1, 0, numAristas};
                aristas.add(edge2);
                tipoArista.put(numAristas, "C+");
            }
            
            //Añado aristas de saltar
            if (!plataforma[i].equals("NA") && !plataforma[i].equals("R") )
            {
                int numSaltos= Integer.parseInt(plataforma[i]);
                if (i-numSaltos >=1)
                {
                    if (!plataforma[i-numSaltos].equals("R"))
                    {
                        numAristas++;
                        int[] edge={i, i-numSaltos, 1, 0, numAristas};
                        aristas.add(edge);  
                        tipoArista.put(numAristas, "S-");
                    }
                }
                if (i+numSaltos<=plataformas)
                {
                    if (!plataforma[i+numSaltos].equals("R"))
                    {
                        numAristas++;
                        int[] edge={i, i+numSaltos, 1, 0, numAristas};
                        aristas.add(edge);  
                        tipoArista.put(numAristas, "S+");
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
                            numAristas++;
                            int[] edge={i, i2+i, 1, i2, numAristas};
                            aristas.add(edge);  
                            tipoArista.put(numAristas, "T"+String.valueOf(i2));
                        }
                    }
                    if (i-i2 >=1)
                    {
                        if (!plataforma[i-i2].equals("R"))
                        {
                            numAristas++;
                            int[] edge={i, i-i2, 1, i2, numAristas};
                            aristas.add(edge);  
                            tipoArista.put(numAristas, "T"+"-"+String.valueOf(i2));

                        }
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
            
            String ans = laberinto(n,e, plataforma);
            System.out.println(ans);
            
        }
        sc.close();
    }
    /* 

    
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

        String ans = laberinto(n,e, plataforma);
        System.out.println(ans);
    }
        */
        
}