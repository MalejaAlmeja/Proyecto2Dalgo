import java.util.*;

public class IdeaSofi {

    static class State {
        int pos, energy;
        State(int pos, int energy) {
            this.pos = pos;
            this.energy = energy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return pos == state.pos && energy == state.energy;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, energy);
        }
    }

    static class Action {
        String type;
        int dest;
        Action(String type, int dest) {
            this.type = type;
            this.dest = dest;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int casos = Integer.parseInt(sc.nextLine().trim());

        for (int t = 0; t < casos; t++) {
            // Read input
            String[] partes = sc.nextLine().trim().split(" ");
            int n = Integer.parseInt(partes[0]);
            int energiaInicial = Integer.parseInt(partes[1]);

            boolean[] robots = new boolean[n + 1];
            String lineaRobots = sc.nextLine().trim();
            if (!lineaRobots.isEmpty()) {
                String[] robotsData = lineaRobots.split(" ");
                for (String r : robotsData) {
                    robots[Integer.parseInt(r)] = true;
                }
            }

            int[] poderes = new int[n + 1];
            String poderesLinea = sc.nextLine().trim();
            if (!poderesLinea.isEmpty()) {
                String[] poderesData = poderesLinea.split(" ");
                for (int i = 0; i < poderesData.length; i += 2) {
                    int p = Integer.parseInt(poderesData[i]);
                    int s = Integer.parseInt(poderesData[i + 1]);
                    poderes[p] = s;
                }
            }

            // DP con estados
            // dp[pos][energy] = min actions para llegar a pos con energy
            // parent[pos][energy] = accion tomada para llegar a pos con energy
            // State: (pos, energy)
            // Action: (type, dest)
            // type: "C+" (walk), "S+" (power jump), "T" (teleport)
            Map<State, Integer> dp = new HashMap<>();
            Map<State, Action[]> parent = new HashMap<>();
            State start = new State(0, energiaInicial);
            dp.put(start, 0);

            // Process platforms in order (0 to n)
            for (int pos = 0; pos <= n; pos++) {
                for (int energy = 0; energy <= energiaInicial; energy++) {
                    State curr = new State(pos, energy);
                    if (!dp.containsKey(curr)) continue;
                    int actions = dp.get(curr);

                    //terminar si llego
                    if (pos == n) continue;

                    //para adelante ?
                    if (pos + 1 <= n && !robots[pos + 1]) {
                        State next = new State(pos + 1, energy);
                        if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                            dp.put(next, actions + 1);
                            parent.put(next, new Action[]{new Action("C+", pos + 1), new Action(pos + "", energy)});
                        }
                    }

                    // Power jump forward
                    if (poderes[pos] > 0) {
                        int salto = poderes[pos];
                        if (pos + salto <= n && !robots[pos + salto]) {
                            State next = new State(pos + salto, energy);
                            if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                                dp.put(next, actions + 1);
                                parent.put(next, new Action[]{new Action("S+", pos + salto), new Action(pos + "", energy)});
                            }
                        }
                    }

                    // teletransportar solo si no hay robots adelante
                    boolean canMoveForward = (pos + 1 <= n && !robots[pos + 1]) || 
                                            (poderes[pos] > 0 && pos + poderes[pos] <= n && !robots[pos + poderes[pos]]);
                    if (!canMoveForward) {
                        // teletransportar a cualquier platadelante
                        for (int dest = n; dest > pos; dest--) { // adelante onlyyyyy
                            if (!robots[dest]) {
                                int costo = Math.abs(dest - pos);
                                if (costo <= energy) {
                                    State next = new State(dest, energy - costo);
                                    if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                                        dp.put(next, actions + 1);
                                        parent.put(next, new Action[]{new Action("T", dest), new Action(pos + "", energy)});
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //min actions para llegar a n
            int minActions = Integer.MAX_VALUE;
            State finalState = null;
            for (int e = 0; e <= energiaInicial; e++) {
                State state = new State(n, e);
                if (dp.containsKey(state) && dp.get(state) < minActions) {
                    minActions = dp.get(state);
                    finalState = state;
                }
            }

            // out
            if (minActions == Integer.MAX_VALUE) {
                System.out.println("NO SE PUEDE");
            } else {
                // reconstruct
                List<String> actions = new ArrayList<>();
                State curr = finalState;
                while (curr.pos != 0 || curr.energy != energiaInicial) {
                    Action[] prev = parent.get(curr);
                    actions.add(prev[0].type);
                    curr = new State(Integer.parseInt(prev[1].type), prev[1].dest);
                }
                Collections.reverse(actions);
                System.out.print(minActions + " ");
                System.out.println(String.join(" ", actions));
            }
        }
        sc.close();
    }
}