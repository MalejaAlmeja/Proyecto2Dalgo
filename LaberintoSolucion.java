import java.util.ArrayList;
import java.util.Scanner;

public class LaberintoSolucion
{
    public static ArrayList<String> laberinto(int plataformas, int energia, String[] plataforma){
        ArrayList<String> camino = new ArrayList<String>();
        //En plataforma, los valores posibles son: null (no hay nada), "R" (robot), "k" (plataformas que se pueden saltar) o "FIN" (llegada al villano final)

        return camino;
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
        }
        sc.close();
    }
}