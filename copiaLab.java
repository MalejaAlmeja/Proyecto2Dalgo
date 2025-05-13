import java.util.PriorityQueue;
import java.util.Scanner;

public class copiaLab
{
    static class Nodo implements Comparable<Nodo> {
        int origen;
        int energiaRestante;
        int acciones;
        String camino;

        Nodo(int origen, int energiaRestante, int acciones, String camino){
            this.origen=origen;
            this.energiaRestante=energiaRestante;
            this.acciones=acciones;
            this.camino=camino;}
        
        @Override
        public int compareTo(Nodo o) {
            int menor = Integer.compare(this.acciones, o.acciones);
            if (menor != 0) return menor;
            return Integer.compare(o.energiaRestante, this.energiaRestante);
        }
        }
    
    public static String laberinto(int plataformas, int energia, String[] plataforma){

        String ans="NO SE PUEDE";
        final int infinito = Integer.MAX_VALUE - energia - 5;
        //En plataforma, los valores posibles son: NA (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)
        //La tabla de acciones es nodo x unidad de energia restante, y el valor adentro es el nÃºmero de acciones mÃ­nimas
        int [][] acciones = new int[plataformas+1][energia+1];
        PriorityQueue<Nodo> cola = new PriorityQueue<>();
        //Cada elemento/nodo en la cola tiene la estructura (posicion actual, energia restante)
        //Inicializo la tabla de acciones
        for (int i = 0; i <= plataformas; i++)
        {
            for (int j = 0; j <= energia; j++)
            {
                if (i==0){acciones[i][j]=0;}
                else{acciones[i][j]=infinito;}
            }
        }

        //BFS
        cola.add(new Nodo(0, energia, 0, ""));
        while (!cola.isEmpty())
        {
            Nodo plataformaActual = cola.remove();
            int origen = plataformaActual.origen;
            int energiaRestante = plataformaActual.energiaRestante;
            int acc = plataformaActual.acciones;
            String camino = plataformaActual.camino;
            String tipoPlataforma = plataforma[origen];

            //Llegamos al final
            if (tipoPlataforma.equals("FIN")){return acc + " " + camino.trim();}
            
            //AÃ±ado aristas sin powerup
            if (tipoPlataforma.equals("NA") ||  (!tipoPlataforma.equals("R") && !tipoPlataforma.equals("FIN")))
            {
                int plataformaAtras = origen-1;
                int plataformaAdelante = origen+1;
                if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                {
                    if (acciones[plataformaAtras][energiaRestante] > acc+1)
                    {
                        acciones[plataformaAtras][energiaRestante]=acc+1;
                        cola.add(new Nodo(plataformaAtras, energiaRestante, acc+1, camino+"C- "));
                    }
                }
                if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                {
                    if (acciones[plataformaAdelante][energiaRestante] > acc+1)
                    {
                        acciones[plataformaAdelante][energiaRestante]=acc+1;
                        cola.add(new Nodo(plataformaAdelante, energiaRestante, acc+1, camino+"C+ "));
                    }
                }
            }

            //Añado aristas con powerup
            if (!tipoPlataforma.equals("NA") && !tipoPlataforma.equals("R") && !tipoPlataforma.equals("FIN"))
            {
                int plataformaAtras = origen-Integer.parseInt(tipoPlataforma);
                int plataformaAdelante = origen+Integer.parseInt(tipoPlataforma);

                if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                {
                    if (acciones[plataformaAtras][energiaRestante] > acc+1)
                    {
                        acciones[plataformaAtras][energiaRestante]=acc+1;
                        cola.add(new Nodo(plataformaAtras, energiaRestante, acc+1, camino+"S- "));
                    }
                }
                if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                {
                    if (acciones[plataformaAdelante][energiaRestante] > acc+1)
                    {
                        acciones[plataformaAdelante][energiaRestante]=acc+1;
                        cola.add(new Nodo(plataformaAdelante, energiaRestante, acc+1, camino+"S+ "));
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
                    int energiaActual = energiaRestante-iE;
                    if (plataformaAdelante <= plataformas && !plataforma[plataformaAdelante].equals("R"))
                    {
                        if (acciones[plataformaAdelante][energiaRestante-iE] > acc+1)
                        {
                            acciones[plataformaAdelante][energiaRestante-iE]=acc+1;
                            cola.add(new Nodo(plataformaAdelante,energiaActual,acc+1,camino+"T"+iE+" "));
                        }
                    }
                    if (plataformaAtras >= 1 && !plataforma[plataformaAtras].equals("R"))
                    {
                        if (acciones[plataformaAtras][energiaRestante-iE] > acc+1)
                        {
                            acciones[plataformaAtras][energiaRestante-iE]=acc+1;
                            cola.add(new Nodo(plataformaAtras,energiaActual,acc+1,camino+"T-"+iE+" "));
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