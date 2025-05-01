
public class DPSolucion 
{
    
    public static String laberinto(int plataformas, int energia, String[] plataforma, int numRobots){
        String ans="NO SE PUEDE";
        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)

        if (energia>= plataformas)
        {
            return "1 T"+String.valueOf(plataformas);
        }

        //Nodo x energia x intermediario
       int [][][] dist = new int[plataformas+1][energia+1][plataformas+1];

       //Inicializo matriz de distancias
       for (int intermediario=0; intermediario<=plataformas; intermediario++)
       {
            for (int nodo=0; nodo<=plataformas; nodo++)
            {   
                for (int iEnergia1=0; iEnergia1<=energia; iEnergia1++)
                {
                    dist[nodo][iEnergia1][intermediario]=Integer.MAX_VALUE - energia - 5;

                    if (intermediario==0)
                    {
                        int valorSalto=0;
                        int distNodo=plataformas-nodo;

                        if (nodo==plataformas)
                        {
                            dist[plataformas][iEnergia1][intermediario]=0;
                        }
                        else if (!plataforma[nodo].equals("R"))
                        {
                            //Chequeo si tiene salto
                            if (!plataforma[nodo].equals("R") && !plataforma[nodo].equals("NA"))
                            {
                                valorSalto=Integer.valueOf(plataforma[nodo]);
                            }
                        
                            //Si se puede saltar o caminar, energia 0 
                            if ( (nodo==plataformas-1 || valorSalto== plataformas-nodo ) && iEnergia1==energia)
                            {
                                dist[nodo][iEnergia1][0]=1;
                            }
                            //Si se puede teletransportar
                            else if (iEnergia1==energia-distNodo)
                            {   
                                dist[nodo][iEnergia1][0]=1;
                            }
                        }
                    }
                }
            }
       }

       //Comienzo DP
       for (int intermediario=1; intermediario<plataformas; intermediario++)
       {
           for (int nodoOrigen=0; nodoOrigen<plataformas; nodoOrigen++)
           {
                if (!plataforma[nodoOrigen].equals("R") && !plataforma[intermediario].equals("R"))
                {
                    int valorSalto=0;
                    int distNodo=plataformas-nodoOrigen;

                    //Chequeo si tiene salto
                    if (!plataforma[nodoOrigen].equals("R") && !plataforma[nodoOrigen].equals("NA"))
                    {
                        valorSalto=Integer.valueOf(plataforma[nodoOrigen]);
                    }

                    for (int iEnergia=0; iEnergia<=energia; iEnergia++)
                    {

                        if (nodoOrigen==intermediario-1 || nodoOrigen==intermediario+1 ||  distNodo==valorSalto) //se puede saltar o caminar
                        {
                                if (dist[nodoOrigen][iEnergia][intermediario]>dist[intermediario][iEnergia][intermediario-1]+1)
                                {
                                    dist[nodoOrigen][iEnergia][intermediario]=dist[intermediario][iEnergia][intermediario-1]+1;
                                }
                        }
                        if ( iEnergia + distNodo<=energia) //se puede teletransportar
                            {
                                if (dist[nodoOrigen][iEnergia][intermediario]>dist[intermediario][iEnergia][intermediario-1]+1)
                                {
                                    dist[nodoOrigen][iEnergia][intermediario]=dist[intermediario][iEnergia+distNodo][intermediario-1] +1;
                                }
                            }
                    }
                }
           }
        }
        
        int minAccion= Integer.MAX_VALUE - energia - 5;
        
        for (int i3=0; i3<=energia; i3++)
        {
            if (dist[0][i3][plataformas]<minAccion)
            {
                minAccion= dist[0][i3][plataformas];
                ans=String.valueOf(minAccion);
                //+" "+track[plataformas][i3];
            }
        }

        for (int i = 0; i <=energia; i++) {          // Row loop
            for (int j = 0; j <=plataformas; j++) {      // Column loop
                System.out.print(dist[j][i][plataformas] + " ");
            }
            System.out.println();              // Newline after each row
        }

        return ans;
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

        String ans = laberinto(n,e, plataforma, robots.length);
        System.out.println(ans);
    }
        
        
}
    

