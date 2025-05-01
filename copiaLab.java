import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class copiaLab
{
    
    public static String laberinto(int plataformas, int energia, String[] plataforma){

        String ans="NO SE PUEDE";
        final int infinito = Integer.MAX_VALUE - energia - 5;

        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)
        //La tabla de acciones es nodo x unidad de energia restante, y el valor adentro es el nÃºmero de acciones mÃ­nimas en pos 0, y acciones en pos 1
        int [][] acciones = new int[plataformas+1][energia+1];
        String [][] track = new String[plataformas+1][energia+1];
        Queue<int[]> cola = new LinkedList<>();
        //Cada elemento/nodo en la cola tiene la estructura (posicion actual, energia restante)
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
                    acciones[i][j]=infinito;
                    track[i][j]="";
                }
            }
        }

        //BFS
        cola.add(new int[]{0,energia});
        while (!cola.isEmpty())
        {
            int[] plataformaActual= cola.remove();
            int origen= plataformaActual[0];
            int energiaRestante= plataformaActual[1];
            int acc= acciones[origen][energiaRestante];

            //Llegamos al final
            if (plataforma[origen].equals("FIN")){return acc + " " + track[origen][energiaRestante].trim();}
            
            //AÃ±ado aristas sin powerup
            if (plataforma[origen].equals("NA") ||  (!plataforma[origen].equals("R") && !plataforma[origen].equals("FIN")))
            {
                int plataformaAtras = origen-1;
                int plataformaAdelante = origen+1;
                if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                {
                    if (acciones[plataformaAtras][energiaRestante] > acciones[origen][energiaRestante]+1)
                    {
                        acciones[plataformaAtras][energiaRestante]=acciones[origen][energiaRestante]+1;
                        track[plataformaAtras][energiaRestante]=track[origen][energiaRestante]+"C- ";
                        cola.add(new int[]{plataformaAtras, energiaRestante});
                    }
                }
                if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                {
                    if (acciones[plataformaAdelante][energiaRestante] > acciones[origen][energiaRestante]+1)
                    {
                        acciones[plataformaAdelante][energiaRestante]=acciones[origen][energiaRestante]+1;
                        track[plataformaAdelante][energiaRestante]=track[origen][energiaRestante]+"C+ ";
                        cola.add(new int[]{plataformaAdelante, energiaRestante});
                    }
                }
            }

            //Añado aristas con powerup
            if (!plataforma[origen].equals("NA") && !plataforma[origen].equals("R") && !plataforma[origen].equals("FIN"))
            {
                int plataformaAtras = origen-Integer.parseInt(plataforma[origen]);
                int plataformaAdelante = origen+Integer.parseInt(plataforma[origen]);

                if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                {
                    if (acciones[plataformaAtras][energiaRestante] > acciones[origen][energiaRestante]+1)
                    {
                        acciones[plataformaAtras][energiaRestante]=acciones[origen][energiaRestante]+1;
                        track[plataformaAtras][energiaRestante]=track[origen][energiaRestante]+"S- ";
                        cola.add(new int[]{plataformaAtras, energiaRestante});
                    }
                }
                if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                {
                    if (acciones[plataformaAdelante][energiaRestante] > acciones[origen][energiaRestante]+1)
                    {
                        acciones[plataformaAdelante][energiaRestante]=acciones[origen][energiaRestante]+1;
                        track[plataformaAdelante][energiaRestante]=track[origen][energiaRestante]+"S+ ";
                        cola.add(new int[]{plataformaAdelante, energiaRestante});
                    }
                }
            }

            //AÃ±ado aristas de teletransportaciÃ³n
            if (energiaRestante>1)
            {
                for (int iE=energiaRestante; iE>1; iE--)
                {
                    int plataformaAdelante = origen+iE;
                    int plataformaAtras = origen-iE;
                    if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                    {
                        if (acciones[plataformaAdelante][energiaRestante-iE] > acciones[origen][iE]+1)
                        {
                            acciones[plataformaAdelante][energiaRestante-iE]=acciones[origen][iE]+1;
                            track[plataformaAdelante][energiaRestante-iE]=track[origen][iE]+"T"+iE+" ";
                            cola.add(new int[]{plataformaAdelante, energiaRestante-iE});
                        }
                    }
                    if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                    {
                        if (acciones[plataformaAtras][energiaRestante-iE] > acciones[origen][iE]+1)
                        {
                            acciones[plataformaAtras][energiaRestante-iE]=acciones[origen][iE]+1;
                            track[plataformaAtras][energiaRestante-iE]=track[origen][iE]+"T-"+iE+" ";
                            cola.add(new int[]{plataformaAtras, energiaRestante-iE});
                        }
                    }
                    
                }

            }
        }

        return ans;
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
            System.out.println(ans.trim());
            
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