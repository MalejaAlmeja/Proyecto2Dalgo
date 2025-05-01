import java.util.Arrays;
import java.util.Scanner;

public class OptimizacionManhattan {

    public static String laberinto(int n, int E, String[] plataforma) {
        // currentEnergy[p] = mÃ¡xima energÃ­a restante con la que se puede llegar a p usando exactamente k acciones
        int[] currentEnergy = new int[n + 1];
        // currentTrack[p] = camino que lleva a p usando exactamente k acciones y dejando currentEnergy[p]
        String[] currentTrack = new String[n + 1];

        Arrays.fill(currentEnergy, -1);
        currentEnergy[0] = E;
        currentTrack[0] = "";

        // Hasta n acciones (en el peor caso no usarÃ¡s mÃ¡s)
        for (int acciones = 0; acciones <= n; acciones++) {
            // Si ya alcanzamos FIN con k = acciones, devolvemos resultado
            if (currentEnergy[n] >= 0) {
                return acciones + " " + currentTrack[n].trim();
            }

            // Preparamos el siguiente nivel (k+1 acciones)
            int[] nextEnergy = new int[n + 1];
            String[] nextTrack = new String[n + 1];
            Arrays.fill(nextEnergy, -1);

            // Recorremos todas las plataformas posibles en este nivel
            for (int p = 0; p <= n; p++) {
                int ener = currentEnergy[p];
                if (ener < 0) continue;               // no es alcanzable con k acciones
                if ("R".equals(plataforma[p])) continue; // plataforma con robot

                String pathSoFar = currentTrack[p];

                // 1) Caminar a la izquierda/derecha (C- / C+)
                for (int d : new int[]{-1, +1}) {
                    int q = p + d;
                    if (q >= 1 && q <= n && !"R".equals(plataforma[q])) {
                        if (ener > nextEnergy[q]) {
                            nextEnergy[q] = ener;
                            nextTrack[q] = pathSoFar + (d > 0 ? "C+ " : "C- ");
                        }
                    }
                }

                // 2) Saltos con power-up en p (S- / S+)
                if (!"NA".equals(plataforma[p]) && !"FIN".equals(plataforma[p])) {
                    int salto = Integer.parseInt(plataforma[p]);
                    for (int d : new int[]{-1, +1}) {
                        int q = p + d * salto;
                        if (q >= 1 && q <= n && !"R".equals(plataforma[q])) {
                            if (ener > nextEnergy[q]) {
                                nextEnergy[q] = ener;
                                nextTrack[q] = pathSoFar + (d > 0 ? "S+ " : "S- ");
                            }
                        }
                    }
                }

                // 3) TeletransportaciÃ³n TÂ±x (consume x energÃ­a)
                for (int gasto = 1; gasto <= ener; gasto++) {
                    int enerLeft = ener - gasto;
                    for (int d : new int[]{-1, +1}) {
                        int q = p + d * gasto;
                        if (q >= 1 && q <= n && !"R".equals(plataforma[q])) {
                            if (enerLeft > nextEnergy[q]) {
                                nextEnergy[q] = enerLeft;
                                String signo = (d > 0 ? "" : "-");
                                nextTrack[q] = pathSoFar + "T" + signo + gasto + " ";
                            }
                        }
                    }
                }
            }

            // Avanzamos al siguiente nivel
            currentEnergy = nextEnergy;
            currentTrack  = nextTrack;
        }

        return "NO SE PUEDE";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ncasos = Integer.parseInt(sc.nextLine().trim());

        for (int tc = 0; tc < ncasos; tc++) {
            // Leer n y E
            String[] datos = sc.nextLine().trim().split(" ");
            int n = Integer.parseInt(datos[0]);
            int E = Integer.parseInt(datos[1]);

            // Inicializar plataformas
            String[] plataforma = new String[n + 1];
            Arrays.fill(plataforma, "NA");

            // Leer robots
            String[] robots = sc.nextLine().trim().split(" ");
            if (!(robots.length == 1 && robots[0].isEmpty())) {
                for (String r : robots) {
                    plataforma[Integer.parseInt(r)] = "R";
                }
            }

            // Leer poderes
            String[] poderes = sc.nextLine().trim().split(" ");
            for (int i = 0; i + 1 < poderes.length; i += 2) {
                int pos = Integer.parseInt(poderes[i]);
                plataforma[pos] = poderes[i + 1];
            }

            // Meta
            plataforma[n] = "FIN";

            // Resolver y mostrar
            String resultado = laberinto(n, E, plataforma);
            System.out.println(resultado.trim());
        }

        sc.close();
    }
}
