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
            // Leer entrada
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

            // BFS
            Queue<State> queue = new LinkedList<>();
            Map<State, Integer> dp = new HashMap<>();
            Map<State, Action> parent = new HashMap<>();
            State start = new State(0, energiaInicial);
            queue.add(start);
            dp.put(start, 0);

            while (!queue.isEmpty()) {
                State curr = queue.poll();
                int pos = curr.pos;
                int energy = curr.energy;
                int actions = dp.get(curr);

                if (pos == n) {
                    // Reconstruir camino
                    List<String> path = new ArrayList<>();
                    State state = curr;
                    while (state.pos != 0 || state.energy != energiaInicial) {
                        Action action = parent.get(state);
                        path.add(action.type);
                        int prevEnergy = (action.type.equals("T")) ? state.energy + Math.abs(state.pos - action.dest) : state.energy;
                        state = new State(action.dest, prevEnergy);
                    }
                    Collections.reverse(path);
                    System.out.print(actions + " ");
                    System.out.println(String.join(" ", path));
                    break;
                }

                // Caminar adelante (C+)
                if (pos + 1 <= n && !robots[pos + 1]) {
                    State next = new State(pos + 1, energy);
                    if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                        dp.put(next, actions + 1);
                        parent.put(next, new Action("C+", pos));
                        queue.add(next);
                    }
                }

                // Caminar atrás (C-)
                if (pos - 1 >= 0 && !robots[pos - 1]) {
                    State next = new State(pos - 1, energy);
                    if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                        dp.put(next, actions + 1);
                        parent.put(next, new Action("C-", pos));
                        queue.add(next);
                    }
                }

                // Saltar adelante (S+)
                if (poderes[pos] > 0) {
                    int salto = poderes[pos];
                    if (pos + salto <= n && !robots[pos + salto]) {
                        State next = new State(pos + salto, energy);
                        if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                            dp.put(next, actions + 1);
                            parent.put(next, new Action("S+", pos));
                            queue.add(next);
                        }
                    }
                }

                // Saltar atrás (S-)
                if (poderes[pos] > 0) {
                    int salto = poderes[pos];
                    if (pos - salto >= 0 && !robots[pos - salto]) {
                        State next = new State(pos - salto, energy);
                        if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                            dp.put(next, actions + 1);
                            parent.put(next, new Action("S-", pos));
                            queue.add(next);
                        }
                    }
                }

                // Teletransportarse (T)
                for (int dest = 0; dest <= n; dest++) {
                    if (dest == pos || robots[dest]) continue;
                    int costo = Math.abs(dest - pos);
                    if (costo <= energy) {
                        State next = new State(dest, energy - costo);
                        if (!dp.containsKey(next) || dp.get(next) > actions + 1) {
                            dp.put(next, actions + 1);
                            parent.put(next, new Action("T", pos));
                            queue.add(next);
                        }
                    }
                }
            }

            if (!dp.containsKey(new State(n, energiaInicial)) && !dp.containsKey(new State(n, 0))) {
                System.out.println("NO SE PUEDE");
            }
        }
        sc.close();
    }
}