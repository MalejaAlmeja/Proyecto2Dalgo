import java.util.Arrays;
import java.util.Scanner;

public class OptimizacionManhattan {

    public static String laberinto(int n, int E, boolean[] isRobot, int[] powerUp) {
        // currentEnergy[p] = max energÃ­a con la que podemos llegar a p usando k acciones
        int[] currentEnergy = new int[n + 1];
        String[] currentTrack = new String[n + 1];
        Arrays.fill(currentEnergy, -1);
        currentEnergy[0] = E;
        currentTrack[0] = "";

        for (int acciones = 0; acciones <= n; acciones++) {
            // si ya llegamos a n
            if (currentEnergy[n] >= 0) {
                return acciones + " " + currentTrack[n].trim();
            }

            int[] nextEnergy = new int[n + 1];
            String[] nextTrack = new String[n + 1];
            Arrays.fill(nextEnergy, -1);

            // 1) caminar y saltos
            for (int p = 0; p <= n; p++) {
                int ener = currentEnergy[p];
                if (ener < 0 || isRobot[p]) continue;
                String path = currentTrack[p];

                // caminar C- y C+
                for (int d : new int[]{-1, +1}) {
                    int q = p + d;
                    if (q >= 0 && q <= n && !isRobot[q]) {
                        if (ener > nextEnergy[q]) {
                            nextEnergy[q] = ener;
                            nextTrack[q] = path + (d > 0 ? "C+ " : "C- ");
                        }
                    }
                }

                // salto S- y S+
                int salto = powerUp[p];
                if (salto > 0) {
                    for (int d : new int[]{-1, +1}) {
                        int q = p + d * salto;
                        if (q >= 0 && q <= n && !isRobot[q]) {
                            if (ener > nextEnergy[q]) {
                                nextEnergy[q] = ener;
                                nextTrack[q] = path + (d > 0 ? "S+ " : "S- ");
                            }
                        }
                    }
                }
            }

            // 2) teletransportaciÃ³n TÂ±x por transformada de Manhattan
            // prefijos
            int[] bestPrefVal = new int[n + 1];
            int[] bestPrefPos = new int[n + 1];
            bestPrefVal[0] = currentEnergy[0] + 0;
            bestPrefPos[0] = 0;
            for (int i = 1; i <= n; i++) {
                int val = currentEnergy[i] + i;
                if (val > bestPrefVal[i - 1]) {
                    bestPrefVal[i] = val;
                    bestPrefPos[i] = i;
                } else {
                    bestPrefVal[i] = bestPrefVal[i - 1];
                    bestPrefPos[i] = bestPrefPos[i - 1];
                }
            }
            // sufijos
            int[] bestSufVal = new int[n + 1];
            int[] bestSufPos = new int[n + 1];
            bestSufVal[n] = currentEnergy[n] - n;
            bestSufPos[n] = n;
            for (int i = n - 1; i >= 0; i--) {
                int val = currentEnergy[i] - i;
                if (val > bestSufVal[i + 1]) {
                    bestSufVal[i] = val;
                    bestSufPos[i] = i;
                } else {
                    bestSufVal[i] = bestSufVal[i + 1];
                    bestSufPos[i] = bestSufPos[i + 1];
                }
            }

            for (int q = 0; q <= n; q++) {
                // desde la izquierda
                int p1 = bestPrefPos[q];
                if (p1 < q && currentEnergy[p1] >= 0 && !isRobot[q]) {
                    int enerLeft = currentEnergy[p1] - (q - p1);
                    if (enerLeft > nextEnergy[q]) {
                        nextEnergy[q] = enerLeft;
                        nextTrack[q] = currentTrack[p1] + "T" + (q - p1) + " ";
                    }
                }
                // desde la derecha
                int p2 = bestSufPos[q];
                if (p2 > q && currentEnergy[p2] >= 0 && !isRobot[q]) {
                    int enerLeft = currentEnergy[p2] - (p2 - q);
                    if (enerLeft > nextEnergy[q]) {
                        nextEnergy[q] = enerLeft;
                        nextTrack[q] = currentTrack[p2] + "T-" + (p2 - q) + " ";
                    }
                }
            }

            // avanzar nivel
            currentEnergy = nextEnergy;
            currentTrack  = nextTrack;
        }

        return "NO SE PUEDE";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ncasos = Integer.parseInt(sc.nextLine().trim());

        for (int tc = 0; tc < ncasos; tc++) {
            String[] datos = sc.nextLine().trim().split(" ");
            int n = Integer.parseInt(datos[0]);
            int E = Integer.parseInt(datos[1]);

            boolean[] isRobot = new boolean[n + 1];
            int[] powerUp = new int[n + 1];

            // leer robots
            String[] robots = sc.nextLine().trim().split(" ");
            if (!(robots.length == 1 && robots[0].isEmpty())) {
                for (String r : robots) {
                    int pos = Integer.parseInt(r);
                    isRobot[pos] = true;
                }
            }

            // leer power-ups
            String[] ups = sc.nextLine().trim().split(" ");
            for (int i = 0; i + 1 < ups.length; i += 2) {
                int pos = Integer.parseInt(ups[i]);
                powerUp[pos] = Integer.parseInt(ups[i + 1]);
            }

            // inicio y meta en Ã­ndice  n  (meta "FIN")
            String resultado = laberinto(n, E, isRobot, powerUp);
            System.out.println(resultado.trim());
        }

        sc.close();
    }
}