import java.util.*;

public class IdeaMaleja {

    public static ArrayList<String> BellmanFord(int n, int e, String[] plataforma) {
        ArrayList<String> decisiones = new ArrayList<String>();
        ArrayList<int[]> aristasMutables = new ArrayList<int[]>();
        //{origen,destino,energia requerida}
        HashMap<Integer,HashSet<Integer>> grafo = new HashMap<Integer,HashSet<Integer>>();
        //revisar manejo de plataforma 0 y final
        //creo que toca hacer hash set de un par (nodo siguiente, es arista cambiante o no)
        //o hacer arreglo con las aristas mutables
        for (int i = 1; i < n; i++) {
            if(!plataforma[i].equals("R")){
                grafo.put(i, new HashSet<Integer>());
                
                if(plataforma[i]==null){
                    grafo.get(i).add(i+1);
                    grafo.get(i).add(i-1);
                }
                else if(!plataforma[i].equals("FIN")){
                    if(Integer.parseInt(plataforma[i])+i<=n){
                        grafo.get(i).add(Integer.parseInt(plataforma[i])+i);
                    }
                    if(i- Integer.parseInt(plataforma[i]) >= 0){
                        grafo.get(i).add(i-(Integer.parseInt(plataforma[i])));
                    }
                }
                //añadir aristas de energia que se iran actualizando

                int energia = e;
                while(energia != 0){
                    if(energia+i <= n){
                        grafo.get(i).add(energia+i);
                        aristasMutables.add(new int[]{i,energia+i,energia});
                    }
                    if(i-energia > 0){
                        grafo.get(i).add(i-energia);
                        aristasMutables.add(new int[]{i,i-energia,energia});
                    }
                    energia--;
                }
            }   
        }

        //comenzamos bellmanford
        //distnacias (costos min) y predecesores
        //antes de tomar decision, verificar que la energia aguante para tomar esa arista


        return decisiones;
    }
    
}
